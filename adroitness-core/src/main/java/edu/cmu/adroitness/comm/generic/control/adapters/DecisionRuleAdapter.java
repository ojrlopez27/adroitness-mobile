package edu.cmu.adroitness.comm.generic.control.adapters;

import edu.cmu.adroitness.comm.generic.model.MBRequest;
import edu.cmu.adroitness.client.commons.control.Constants;
import edu.cmu.adroitness.commons.rules.control.DecisionRuleValidator;
import edu.cmu.adroitness.commons.rules.model.DecisionRule;

/**
 * Created by oscarr on 3/15/16.
 */
public final class DecisionRuleAdapter extends ChannelAdapter {
    private static DecisionRuleAdapter instance;

    private DecisionRuleAdapter() {
        super();
    }

    public static DecisionRuleAdapter getInstance() {
        if (instance == null) {
            instance = new DecisionRuleAdapter();
        }
        return instance;
    }

    public void createDecisionRule(MBRequest mbRequest) {
        String ruleID = (String) mbRequest.get(Constants.DECISION_RULE_ID );
        DecisionRuleValidator.getInstance().createRule( ruleID, (String) mbRequest.get(
                Constants.DECISION_RULE_JSON ), (DecisionRule) mbRequest.get(Constants.DECISION_RULE));
    }

    public void removeDecisionRule(MBRequest mbRequest) {
        String ruleID = (String) mbRequest.get(Constants.DECISION_RULE_ID);
        DecisionRuleValidator.getInstance().unregisterRule( ruleID );
    }

    public void removeAllDecisionRules(MBRequest mbRequest) {
        DecisionRuleValidator.getInstance().unregisterAllRules( );
    }
}
