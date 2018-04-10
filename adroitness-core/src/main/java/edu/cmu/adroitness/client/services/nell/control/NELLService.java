package edu.cmu.adroitness.client.services.nell.control;

import android.content.Intent;
import android.os.IBinder;

import edu.cmu.adroitness.comm.nell.model.NellMacroReaderEvent;
import edu.cmu.adroitness.comm.nell.model.NellMicroReaderEvent;
import edu.cmu.adroitness.client.commons.control.Constants;
import edu.cmu.adroitness.client.commons.control.HttpController;
import edu.cmu.adroitness.client.commons.control.HttpResponseListener;
import edu.cmu.adroitness.client.commons.control.Util;
import edu.cmu.adroitness.client.services.nell.model.MicroReaderResultVO;
import edu.cmu.adroitness.client.services.generic.control.GenericService;
import edu.cmu.adroitness.client.services.nell.model.MacroReaderResultVO;

import java.text.Normalizer;
import java.util.HashMap;
import java.util.Map;

public class NELLService extends GenericService {
    private static final String TAG = NELLService.class.getSimpleName();
    private String urlMacroReader;
    private String urlMicroReader;

    public NELLService() {
        super(null);
        if( actions.isEmpty() ) {
            this.actions.add(Constants.ACTION_NELL);
        }
        urlMacroReader = resourceLocator.getConfigProperty(Constants.ENDPOINT_NELL_MACROREADER);
        urlMicroReader = resourceLocator.getConfigProperty(Constants.ENDPOINT_NELL_MICROREADER);
    }

    @Override
    public IBinder onBind(Intent intent){
        return super.onBind(intent);
    }

    @Override
    public void doAfterBind(){
        super.doAfterBind();
    }

    /**
     * Displays the categories, reasons and relations of a given entity
     *
     * @param entity the entity to be searched in askNell
     */
    public void executeMacroReading(final String entity){
        Util.execute(new Runnable() {
            @Override
            public void run() {
                String ent = entity.replaceAll("[^a-zA-Z \\.]", "");
                Map<String, Object> payload = new HashMap<>();
                payload.put("predicate", "*");
                payload.put("ent2", "");
                payload.put("lit2", "");
                payload.put("agent", "KI,CKB,OPRA,OCMC");
                payload.put("ent1", ent);
                payload.put("lit1", ent);

                HttpController.getHttpGetResponse(urlMacroReader, payload, null,
                        new HttpResponseListener() {
                    @Override
                    public void onSucess(String response) {
                        mb.send(NELLService.this, NellMacroReaderEvent.build().setResultVO(
                                Util.fromJson( response, MacroReaderResultVO.class).sort() ));
                    }
                });
            }
        });
    }

    /**
     * Knowledge on demand
     * @param document
     */
    public void executeMicroReading(final String document){
        Util.execute(new Runnable() {
            @Override
            public void run() {
                String doc = Normalizer.normalize( document, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
                Map payload = new HashMap();
                payload.put("text", doc);
                HttpController.getHttpGetResponse(urlMicroReader, payload, null, new HttpResponseListener() {
                    @Override
                    public void onSucess(String response) {
                        response = response.replace("}\n{\"spanStart\"", "}, {\"spanStart\"");
                        response = "{\"results\":[" + response + "]}";
                        mb.send( NELLService.this, NellMicroReaderEvent.build().setResultVO(
                                Util.fromJson(response, MicroReaderResultVO.class)) );
                    }
                });
            }
        });
    }
}
