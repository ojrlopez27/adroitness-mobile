package edu.cmu.adroitness.client.services.nell.model;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import edu.cmu.adroitness.client.commons.control.Util;

import java.lang.reflect.Type;

/**
 * Created by oscarr on 9/26/16.
 */

public class MicroReaderSlotDeserializer implements JsonDeserializer<MicroReaderSlotValue> {
    @Override
    public MicroReaderSlotValue deserialize(JsonElement json, Type typeOfT,
                                            JsonDeserializationContext context) throws JsonParseException {
        if( !json.isJsonNull() ){
            String jsonString = json.toString();
            if( jsonString.contains("|") ){
                return new NELLEntity( jsonString.replace("\"", "").replace("||", "|").split("\\|") );
            }else if( jsonString.contains("\"str\":") && jsonString.contains("\"s\":") &&
                    jsonString.contains("\"e\":")){
                return Util.fromJson( jsonString, TokenSlotValue.class );
            }else if( jsonString.contains("\"spans\":") && jsonString.contains("\"sI\":")){
                return Util.fromJson( jsonString, CorefSlotValue.class );
            }else if( jsonString.contains("(ROOT ") ){
                return new ConParseSlotValue( jsonString );
            }else if( jsonString.contains("root(ROOT-") ){
                return new DepParseSlotValue( jsonString );
            }else{
                return new SimpleNellValue( jsonString );
            }
        }
        return null;
    }
}
