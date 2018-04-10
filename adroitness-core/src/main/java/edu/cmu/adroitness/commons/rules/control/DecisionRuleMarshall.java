package edu.cmu.adroitness.commons.rules.control;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import edu.cmu.adroitness.commons.rules.model.PropositionalStatement;

import java.lang.reflect.Type;

/**
 * Created by oscarr on 10/16/15.
 */
public class DecisionRuleMarshall implements JsonDeserializer<Object> {

    @Override
    public Object deserialize(JsonElement jsonElement, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {

        if( typeOfT.equals( PropositionalStatement.class) ) {
            JsonObject jsonObj = jsonElement.getAsJsonObject();
            String type = jsonObj.get("componentName").getAsString();
            Class<? extends PropositionalStatement> clazz =
                    DecisionRuleValidator.getInstance().extractProposition( type );

            if (clazz == null) {
                return null;
            }
            PropositionalStatement proposition = context.deserialize(jsonElement, clazz);
            proposition.initialize( true );
            proposition.postCreate();
            return proposition;
        }
        return null;
    }
}
