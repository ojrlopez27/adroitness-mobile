package edu.cmu.adroitness.client.commons.control;

import android.util.Log;

/**
 * Created by oscarr on 12/6/16.
 */

public abstract class HttpResponseListener {
    public static final String DONE = "-DONE";
    public String requestId = "";
    public abstract void onSucess(String response);
    public void onError(String message){
        if(message!=null) {
            Log.e(this.getClass().getSimpleName(), message);
        }
    }
}
