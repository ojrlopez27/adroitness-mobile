package edu.cmu.adroitness.client.services.nell.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by oscarr on 9/27/16.
 */

public class SyntaxNode {
    private String label;
    private String value;
    private boolean hasNext = true;
    private List<SyntaxNode> children;

    public SyntaxNode(String values) {
        setValue( values );
    }

    public SyntaxNode() {}

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label.replace("(", "");
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public List<SyntaxNode> getChildren() {
        return children;
    }

    public void setChildren(List<SyntaxNode> children) {
        this.children = children;
    }

    public boolean hasNext() {
        return hasNext;
    }

    public void setHasNext(boolean hasNext) {
        this.hasNext = hasNext;
    }

    public SyntaxNode addChild(String values) {
        if( children == null ){
            children = new ArrayList<>();
        }
        if( values == null ){
            children.add( new SyntaxNode() );
        }else {
            children.add(new SyntaxNode(values));
        }
        return getLastChildren();
    }

    public SyntaxNode getLastChildren() {
        return children.get( children.size() - 1);
    }

    public SyntaxNode setValues(String values) {
        String[] vls = values.replace("(", "").replace(")", "").split(" ");
        setLabel( vls[0] );
        setValue( vls[1] );
        if( values.endsWith("))") ){
            hasNext = false;
        }
        return this;
    }
}

