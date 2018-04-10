package edu.cmu.adroitness.commons.rules.control;


import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import edu.cmu.adroitness.client.commons.control.Exclude;

/**
 * Created by oscarr on 10/16/15.
 */
public class AnnotationExclusionStrategy implements ExclusionStrategy {

    @Override
    public boolean shouldSkipField(FieldAttributes f) {
        return f.getAnnotation(Exclude.class) != null;
    }

    @Override
    public boolean shouldSkipClass(Class<?> clazz) {
        return false;
    }
}