package edu.cmu.adroitness.client.services.nell.model;

import edu.cmu.adroitness.client.commons.control.Util;

import java.util.Date;

/**
 * Created by oscarr on 9/22/16.
 */

public class NellJustification {
    private double score;
    private String agent;
    private String agentType;
    private String date;
    private int iteration;
    private String comment;
    private int updateIteration;

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public String getAgent() {
        return agent;
    }

    public void setAgent(String agent) {
        this.agent = agent;
    }

    public String getAgentType() {
        return agentType;
    }

    public void setAgentType(String agentType) {
        this.agentType = agentType;
    }

    public Date getDate() {
        return Util.getDate( date.replace(" ", "T") );
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getIteration() {
        return iteration;
    }

    public void setIteration(int iteration) {
        this.iteration = iteration;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getUpdateIteration() {
        return updateIteration;
    }

    public void setUpdateIteration(int updateIteration) {
        this.updateIteration = updateIteration;
    }
}
