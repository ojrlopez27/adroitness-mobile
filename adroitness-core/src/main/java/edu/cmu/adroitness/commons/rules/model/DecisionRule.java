package edu.cmu.adroitness.commons.rules.model;

import edu.cmu.adroitness.client.commons.control.Util;
import edu.cmu.adroitness.client.commons.control.Exclude;
import edu.cmu.adroitness.client.services.generic.model.DataObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by oscarr on 9/28/15.
 */
public final class DecisionRule {
    private List<ConditionElement> conditions;
    private String regularExpression;
    private ArrayList<ActionElement> actions;
    private String ruleID;
    /**
     * Control variable to determine whether all preconditions are true, if so trigger the action
     */
    private int numCondAreTrue;
    @Exclude
    private HashMap<String, HashMap<String, Object>> cacheMemory;

    public DecisionRule() {
        conditions = new ArrayList<>();
        actions = new ArrayList<>();
        cacheMemory = new HashMap<>();
        numCondAreTrue = 0;
    }

    public String getRuleID() {
        if( ruleID == null ){
            ruleID = Util.getUUID();
        }
        return ruleID;
    }

    public void setRuleID(String ruleID) {
        this.ruleID = ruleID;
    }

    public List<ConditionElement> getConditions() {
        return conditions;
    }

    public void setConditions(ArrayList<ConditionElement> conditions) {
        this.conditions = conditions;
    }

    public ArrayList<ActionElement> getActions() {
        return actions;
    }

    public void setActions(ArrayList<ActionElement> actions) {
        this.actions = actions;
    }

    public String getRegularExpression() {
        return regularExpression;
    }

    public void setRegularExpression(String regularExpression) {
        this.regularExpression = regularExpression;
    }

    public boolean isValid() {
        return numCondAreTrue == conditions.size();
    }

    public int getNumCondAreTrue() {
        return numCondAreTrue;
    }

    public void setNumCondAreTrue(int numCondAreTrue) {
        this.numCondAreTrue = numCondAreTrue;
    }

    public void addCondition(String nameOfTerm, PropositionalStatement propositionalStatement ){
        ConditionElement element = new ConditionElement(nameOfTerm, propositionalStatement);
        conditions.add( element );
    }

    public void addAction( String nameOfComponent, HashMap<String, Object> attributes ){
        actions.add( new ActionElement( nameOfComponent, attributes ) );
    }

    public List<ActionElement> extractActions( String componentName){
        List<ActionElement> actions = new ArrayList<>();
        for( ActionElement actionElement : this.actions ){
            if( actionElement.componentName.equals( componentName )){
                actions.add( actionElement );
            }
        }
        return actions;
    }

    public List<ConditionElement> extractConditions( String componentName ){
        List<ConditionElement> conditions = new ArrayList<>();
        for( ConditionElement conditionElement : this.conditions ){
            if( conditionElement.getProposition().getComponentName().equals(componentName)){
                conditions.add( conditionElement );
            }
        }
        return conditions;
    }


    public HashMap<String, HashMap<String, Object>> getCacheMemory() {
        return cacheMemory;
    }

    /**
     * It determines whether rule is triggered or not according to conditions truth value
     * @param proposition
     * @param flag
     * @param dataObjects
     * @return whether all conditions are true
     */
    public <T extends DataObject> boolean setPropositionFlag(PropositionalStatement proposition,
                                                             boolean flag, List<T> dataObjects) {
        //check whether all flags are true
        boolean isModified = false;

        if( dataObjects == null || dataObjects.isEmpty() ){
            isModified = proposition.getCondition().setFlag( flag );
            if( flag && isModified ){
                numCondAreTrue++;
            }
        }else {
            ConditionElement conditionElement = proposition.getCondition();
            for (DataObject event : dataObjects) {
                String eventId = event.getUUID();
                if ((proposition.getLinkedToObjectId() == null && !conditionElement.isFlag() && flag)
                        || (proposition.getLinkedToObjectId() != null
                        && proposition.getLinkedToObjectId().equals(eventId))) {
                    if (conditionElement.setFlag(flag)
                            && proposition.setLinkedToEventId(eventId)) {
                        isModified = true;
                    }
                    if (flag && isModified) {
                        numCondAreTrue++;
                    }
                }
            }
        }

        //if nothing is modified then do not trigger actions
        if( !isModified || numCondAreTrue != conditions.size() ) {
            return false;
        }
        return true;
    }


    public void unlinkElement( String elementUUID ){
        for (ConditionElement conditionElement : conditions) {
            String linkedTo = conditionElement.proposition.getLinkedToObjectId();
            if( linkedTo != null && linkedTo.equals(elementUUID) ){
                conditionElement.proposition.setLinkedToEventId( null );
                conditionElement.setFlag( false );
                numCondAreTrue--;
            }
        }
    }

    public void destroy() {
        for( ConditionElement conditionElement : conditions ){
            conditionElement.destroy();
        }
        for( ActionElement actionElement : actions ){
            actionElement.destroy();
        }
        conditions = null;
        actions = null;
    }



    /***************************************** HELPER CLASSES *************************************/

    public class ConditionElement {
        private String term;
        private PropositionalStatement proposition;
        @Exclude private boolean flag;

        public ConditionElement(String term, PropositionalStatement proposition) {
            this.term = term;
            this.proposition = proposition;
            this.proposition.addRule( DecisionRule.this );
            this.proposition.addCondition( this );
        }

        public String getTerm() {
            return term;
        }

        public void setTerm(String term) {
            this.term = term;
        }

        public PropositionalStatement getProposition() {
            return proposition;
        }

        public void setProposition(PropositionalStatement proposition) {
            this.proposition = proposition;
        }

        public boolean isFlag() {
            return flag;
        }

        /**
         * It returns true if the flag has been modified (either to true or false) and false otherwise
         * @param flag
         * @return
         */
        public boolean setFlag(boolean flag) {
            boolean temp = this.flag;
            this.flag = flag;
            return temp != flag;
        }

        public void destroy() {
            proposition.destroy();
            proposition = null;
        }
    }

    public class ActionElement{
        private String componentName;
        private HashMap<String, Object> attributes;

        public ActionElement(String component, HashMap attributes) {
            this.componentName = component;
            this.attributes = attributes;
        }

        public String getComponentName() {
            return componentName;
        }

        public void setComponentName(String componentName) {
            this.componentName = componentName;
        }

        public HashMap<String, Object> getAttributes() {
            return attributes;
        }

        public void setAttributes(HashMap<String, Object> attributes) {
            this.attributes = attributes;
        }

        public void destroy() {
            attributes.clear();
            attributes = null;
        }
    }
}

