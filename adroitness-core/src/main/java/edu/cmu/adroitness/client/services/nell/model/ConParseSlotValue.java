package edu.cmu.adroitness.client.services.nell.model;

import java.util.Arrays;
import java.util.LinkedList;

/**
 * Created by oscarr on 9/27/16.
 */

public class ConParseSlotValue implements MicroReaderSlotValue {
    private SyntaxNode root;

    public ConParseSlotValue(String jsonString) {
        String[] units = jsonString.replace("\" (", "(").replace("\"", "")
                .replaceAll("[\\s]+", " ").split(" \\(");

        LinkedList<String> queue = new LinkedList<>(Arrays.asList(units) );
        root = new SyntaxNode();
        createTree( queue, root );
    }

    private void createTree( LinkedList<String> queue, SyntaxNode currentNode ){
        String value = queue.poll();
        if( value.contains(" ") ){
            currentNode.setValues(value);
        }else{
            currentNode.setLabel( value );
            do{
                createTree( queue, currentNode.addChild( null ) );
            }while( currentNode.getLastChildren().hasNext() && !queue.isEmpty() );
        }
    }
}
