package edu.cmu.adroitness.client.services.generic.model;

import edu.cmu.adroitness.client.commons.control.Util;
import edu.cmu.adroitness.client.commons.control.Exclude;

/**
 * Created by oscarr on 2/24/16.
 */
public class DataObject {
    protected String id;
    /**
     * It determines whether the proposition element should be linked to this data object (for instance
     * in CalendarProposition)
     */
    @Exclude
    protected boolean isLinkedToObjectID;
    @Exclude
    protected String jsonPrettyString;

    public String getUUID() {
        if( id == null ){
            id = Util.getUUID();
        }
        return id;
    }

    public String getId() {
        return id;
    }

    public <T extends DataObject> T setId(String id) {
        this.id = id;
        return (T)this;
    }

    public String getJsonPrettyString() {
        if( jsonPrettyString == null ){
            jsonPrettyString = Util.toJsonPretty( this );
        }
        return jsonPrettyString;
    }

    public void setJsonPrettyString(String jsonPrettyString) {
        this.jsonPrettyString = jsonPrettyString;
    }

    public boolean isLinkedToObjectID() {
        return isLinkedToObjectID;
    }

    public <T extends DataObject> T setLinkedToObjectID(Boolean linkedToObjectID) {
        isLinkedToObjectID = linkedToObjectID == null? false : linkedToObjectID;
        return (T)this;
    }

    @Override
    public String toString(){
        return getJsonPrettyString();
    }
}
