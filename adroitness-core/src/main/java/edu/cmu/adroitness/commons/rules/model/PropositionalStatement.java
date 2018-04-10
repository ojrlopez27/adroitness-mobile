package edu.cmu.adroitness.commons.rules.model;

import edu.cmu.adroitness.comm.generic.control.MessageBroker;
import edu.cmu.adroitness.client.commons.control.ResourceLocator;
import edu.cmu.adroitness.client.commons.control.Util;
import edu.cmu.adroitness.client.commons.control.Constants;
import edu.cmu.adroitness.commons.rules.control.DecisionRuleValidator;
import edu.cmu.adroitness.client.commons.control.Exclude;
import edu.cmu.adroitness.client.services.generic.model.DataObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by oscarr on 9/15/15.
 */
public abstract class PropositionalStatement {
    protected String componentName;
    protected String attribute;
    protected String operator;
    protected String value;
    protected String refAttribute;
    protected String linkedToObjectId;
    protected String uuid;

    /** a propositional statement can be related to a rule**/
    @Exclude //we need to exclude this for json parsing
    protected DecisionRule rule;
    @Exclude //we need to exclude this for json parsing
    protected DecisionRule.ConditionElement condition;
    @Exclude //we need to exclude this for json parsing
    protected MessageBroker mb;
    @Exclude
    protected ResourceLocator resourceLocator;

    public PropositionalStatement() {
        uuid = Util.getUUID();
        resourceLocator = ResourceLocator.getExistingInstance();
        mb = MessageBroker.getExistingInstance( this );
        postCreate();
    }

    public PropositionalStatement(String attribute, String operator, String value) {
        this();
        this.attribute = attribute;
        this.operator = operator;
        this.value = value;
    }

    public PropositionalStatement(String attribute, String operator, String value, String refAttribute) {
        this(attribute, operator, value.toString());
        this.refAttribute = refAttribute;
    }

    public void initialize(boolean subscribe){
        if( subscribe ) {
            subscribe();
        }
    }

    /**
     * In those cases where the preposition is listening to events in order to validate some data
     * you should implement this method and subscribe to the MessageBroker, otherwise just leave it empty
     */
    public void subscribe(){
        mb.subscribe(this);
    }

    public void unsubscribe(){
        if( mb != null ) {
            mb.unsubscribe(this);
        }
    }

    /**
     * It validates all the objects associated to this proposition, for instance, CalendarProposition
     * will validate all user's calendar events.
     * @return
     */
    public abstract ArrayList validate();

    abstract public Object validate( Object objValue );

    abstract public void postCreate();


    public boolean validateNumbers(Number attribute, Number value){
        if( attribute != null && value != null ) {
            if (this.operator.equals(Constants.OPERATOR_EQUALS_TO)
                    && attribute.doubleValue() == value.doubleValue()) {
                return true;
            } else if (this.operator.equals(Constants.OPERATOR_HIGHER_THAN)
                    && attribute.doubleValue() > value.doubleValue()) {
                return true;
            } else if (this.operator.equals(Constants.OPERATOR_LOWER_THAN)
                    && attribute.doubleValue() < value.doubleValue()) {
                return true;
            }
        }
        return false;
    }

    public boolean validateStrings(String attribute){
        if (this.operator.equals(Constants.OPERATOR_EQUALS_TO)
                && attribute.equalsIgnoreCase( this.value ) ) {
            return true;
        } else if (this.operator.equals(Constants.OPERATOR_CONTAINS_STRING)
                && attribute.contains( this.value ) ) {
            return true;
        }
        return false;
    }


    public void addRule(DecisionRule rule){
        this.rule = rule;
    }

    public void addCondition(DecisionRule.ConditionElement condition){
        this.condition = condition;
    }

    public ArrayList getList( Object validatedObject ){
        ArrayList list = new ArrayList();
        if( validatedObject != null ) {
            if( validatedObject instanceof List){
                for( Object element : (List) validatedObject ){
                    Object result = validate(element);
                    if (result != null && ( result instanceof Boolean? (Boolean)result : true) ) {
                        list.add(result);
                    }
                }
            }else {
                Object result = validate(validatedObject);
                if (result != null && ( result instanceof Boolean? (Boolean)result : true) ) {
                    list.add(result);
                }
            }
        }
        return list;
    }

    public DecisionRule.ConditionElement getCondition(){
        return this.condition;
    }

    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getRefAttribute() {
        return refAttribute;
    }

    public void setRefAttribute(String refAttribute) {
        this.refAttribute = refAttribute;
    }

    public String getComponentName() {
        return componentName;
    }

    public void setComponentName(String componentName) {
        this.componentName = componentName;
    }

    public String getLinkedToObjectId() {
        return linkedToObjectId;
    }

    public boolean setLinkedToEventId(String linkedToEventId) {
        String temp = this.linkedToObjectId;
        this.linkedToObjectId = linkedToEventId;
        return temp == null? true : temp.equals( linkedToEventId );
    }

    public String getUuid() {
        return uuid;
    }

    public void destroy(){
        unsubscribe();
        this.rule = null;
    }

    public <T extends DataObject> void onEvent(List<T> elementsToValidate, List<T> elementsToRemove) {
        if (rule != null) {
            if( elementsToRemove != null ) {
                for (DataObject eventVO : elementsToRemove) {
                    rule.unlinkElement(eventVO.getUUID());
                }
            }
            for (DataObject eventVO : elementsToValidate) {
                Object flag = validate(eventVO);
                List list = null;
                boolean flagValue = !(flag == null || (flag instanceof Boolean && !(Boolean) flag));
                if (flagValue && eventVO.isLinkedToObjectID()) {
                    list = new ArrayList();
                    list.add(eventVO);
                }
                if (rule.setPropositionFlag(this, flagValue, list)) {
                    DecisionRuleValidator.getInstance().triggerActions(rule, null);
                    rule.setNumCondAreTrue(0);
                }
            }
        }
    }
}
