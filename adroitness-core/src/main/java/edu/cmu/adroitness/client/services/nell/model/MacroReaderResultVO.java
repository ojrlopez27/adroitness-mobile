package edu.cmu.adroitness.client.services.nell.model;

import edu.cmu.adroitness.client.services.generic.model.DataObject;

import java.util.Collections;
import java.util.List;

/**
 * Created by oscarr on 8/29/16.
 */
public class MacroReaderResultVO extends DataObject {
    private String kind;
    private List<NellItem> items;
    private List<EntityItem> entMap;

    public MacroReaderResultVO(String kind, List<NellItem> items, List<EntityItem> entMap) {
        this.kind = kind;
        this.items = items;
        this.entMap = entMap;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public List<NellItem> getItems() {
        return items;
    }

    public void setItems(List<NellItem> items) {
        this.items = items;
    }

    public List<EntityItem> getEntMap() {
        return entMap;
    }

    public void setEntMap(List<EntityItem> entMap) {
        this.entMap = entMap;
    }

    public MacroReaderResultVO sort(){
        for( NellItem nellItem : items ){
            for( NellJustification justification : nellItem.getJustifications() ){
                //knowledge integrator with highest score
                if( justification.getAgent().equals("KI") ){
                    nellItem.setGlobalScore( justification.getScore() );
                    break;
                }
            }
        }
        Collections.sort( items );
        return this;
    }
}
