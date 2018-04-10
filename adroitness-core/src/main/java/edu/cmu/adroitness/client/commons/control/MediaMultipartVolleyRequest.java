package edu.cmu.adroitness.client.commons.control;

/**
 * Created by sakoju on 12/16/16.
 */

import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;

public class MediaMultipartVolleyRequest<T> extends Request<T> {

    private final Gson gson = new Gson();
    private Class<T> clazz=null;
    private String FILE_PART_NAME = "imagefile"; //default value, can be video or image

    private MultipartEntityBuilder mBuilder = MultipartEntityBuilder.create();
    private final Response.Listener<T> mListener;
    private final File mImageFile;
    protected Map<String, String> headers;

    public MediaMultipartVolleyRequest(String url, ErrorListener errorListener, Listener<T> listener, File imageFile, Class<T> clazz, String FILE_PART_NAME, String contentType){
        super(Method.POST, url, errorListener);

        mListener = listener;
        mImageFile = imageFile;
        this.clazz= clazz;
        this.FILE_PART_NAME =FILE_PART_NAME;

        buildMultipartEntity(contentType);
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> headers = super.getHeaders();

        if (headers == null
                || headers.equals(Collections.emptyMap())) {
            headers = new HashMap<String, String>();
        }

        headers.put("Accept", "application/json");

        return headers;
    }

    private void buildMultipartEntity(String contentType){
        mBuilder.addBinaryBody(FILE_PART_NAME, mImageFile, ContentType.create("image/jpg"), mImageFile.getName());
        mBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        mBuilder.setLaxMode().setBoundary("xx").setCharset(Charset.forName("UTF-8"));
    }

    @Override
    public String getBodyContentType(){
        String contentTypeHeader = mBuilder.build().getContentType().getValue();
        return contentTypeHeader;
    }

    @Override
    public byte[] getBody() throws AuthFailureError{
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            mBuilder.build().writeTo(bos);
        } catch (IOException e) {
            VolleyLog.e("IOException writing to ByteArrayOutputStream bos, building the multipart request.");
        }

        return bos.toByteArray();
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        T result = null;
        String json ="";
        System.out.println("Status code: "+response.statusCode);
        System.out.println("headers : "+response.headers.toString());
        System.out.println("size of headers array : "+response.headers.values().toArray());
        //Response<T> tResponse = Response.success(result, HttpHeaderParser.parseCacheHeaders(response));
        try {
            Thread.sleep(5000);
        }catch(InterruptedException ex)
        {
            ex.printStackTrace();
        }
        System.out.println("result: " + response.data);
        try {
            json = new String(
                    response.data,
                    HttpHeaderParser.parseCharset(response.headers));
        }catch(UnsupportedEncodingException exc)
        {
            exc.printStackTrace();
        }

        return Response.success(gson.fromJson(json, clazz), HttpHeaderParser.parseCacheHeaders(response));
    }

    public static String parseToString(NetworkResponse response) {
        String parsed;
        try {
            parsed = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
        } catch (UnsupportedEncodingException e) {
            parsed = new String(response.data);
        }
        return parsed;
    }

    @Override
    protected void deliverResponse(T response) {
        System.out.println(response);
        mListener.onResponse(response);
    }
}
