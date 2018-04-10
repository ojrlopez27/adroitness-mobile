package edu.cmu.adroitness.client.commons.control;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import android.util.Log;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.Volley;


/**
 * Created by oscarr on 2/25/15.
 */
public class HttpController {

    private static RequestQueue requestQueue;
    private static String TAG = "HttpController";

    private static void initialize(){
        if( requestQueue == null ){
            requestQueue = Volley.newRequestQueue( ResourceLocator.getContext() );
            requestQueue.start();
        }
    }


    public static void getHttpPostResponse( String url, Map<String, Object> params, String body,
                                            HttpResponseListener listener){
        getHttpPostResponse(url, null, body, params, listener);
    }

    public static void getHttpPostResponse( final String url, final String contentType, final String body,
                                            final Map<String, Object> params, final HttpResponseListener listener ){
        getHttpResponse(Request.Method.POST, url, contentType, body, params, listener);
    }

    private static void getHttpResponse(int method, String url, final String contentType, String body,
                                        final Map<String, Object> params, HttpResponseListener listener) {
        try {
            initialize();
            url = checkUrl(method, url, params);
            JsonObjectRequest request = new JsonObjectRequest(method, url, body == null? null :
                    new JSONObject(body), createReqSuccessListener(listener), createReqErrorListener(listener)) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    return createParams(params);
                }

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    return createHeaders( contentType );
                }
            };
            request.setRetryPolicy(new DefaultRetryPolicy(300000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add( request );
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void getHttpPostResponseWithFuture( final String url, final String contentType, final String body,
                                            final Map<String, Object> params, RequestFuture requestFuture, RequestFuture future ) {
        getHttpResponseWithFuture(Request.Method.POST, url, contentType, body, params, requestFuture, future);

    }
    private static void getHttpResponseWithFuture(int method, String url, final String contentType, String body,
                                                  final Map<String, Object> params, RequestFuture requestFuture, RequestFuture requestFutureNew) {
        try {
            initialize();
            url = checkUrl(method, url, params);
            JsonObjectRequest request = new JsonObjectRequest(method, url, body == null? null :
                    new JSONObject(body), requestFuture, requestFutureNew) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    return createParams(params);
                }

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    return createHeaders( contentType );
                }
            };
            request.setRetryPolicy(new DefaultRetryPolicy(300000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add( request );
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private static String checkUrl(int method, String url, Map<String, Object> params) {
        if( method == Request.Method.GET ){
            if( params != null ){
                url += "?";
                for( String key : params.keySet() ){
                    url += key + "=" + params.get(key) + "&";
                }
            }
        }
        return url;
    }

    private static Map<String, String> createHeaders(String contentType) {
        HashMap<String, String> headers = new HashMap<>();
        if( contentType != null ) {
            headers.put("Content-Type", contentType);
        }
        return headers;
    }

    private static Map<String, String> createParams(Map<String, Object> params) {
        Map<String, String> parameters = new HashMap<>();
        if( params != null ){
            for( String key : params.keySet() ){
                Object value  = params.get(key);
                parameters.put( key, value instanceof String? (String)value : Util.toJsonPretty(value));
            }
        }
        return parameters;
    }

    private static Response.ErrorListener createReqErrorListener(final HttpResponseListener listener){
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                if( listener != null ) {
                    listener.onError(error.getMessage());
                }else{
                    Log.e(TAG, "Error: " + error.getMessage());
                }
            }
        };
    }

    private static Response.Listener<JSONObject> createReqSuccessListener(final HttpResponseListener listener) {
        return new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if( listener != null ) {
                    listener.requestId = listener.requestId + HttpResponseListener.DONE;
                    listener.onSucess(response.toString());
                }else{
                    Log.d(TAG, "Success Response: " + response.toString());
                }
            }
        };
    }


    public static void getHttpGetResponse( String url, Map<String, Object> params, String body,
                                             HttpResponseListener listener ){

        getHttpResponse( Request.Method.GET, url, null, body, params, listener );
    }

    public static void getHttpGetResponse( String urlString, Map<String, Object> payload,
                                           HttpResponseListener listener ){

        getHttpResponse(Request.Method.GET, urlString, null, null, payload, listener);
    }
}


