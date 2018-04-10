package edu.cmu.adroitness.comm.generic.model;

import android.os.Bundle;
import android.os.Parcelable;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by oscarr on 12/22/14.
 * This class is used for interchanging data with the Message Broker
 */
public class MBRequest {
    private Multimap<String, Object> map;
    private double[] values;
    private String requestId;

    private MBRequest(String id) {
        this.map = ArrayListMultimap.create();
        this.requestId = id;
    }

    private MBRequest(String id, double... args){
        this(id);
        values = args;
    }

    public static MBRequest build(String id) {
        return new MBRequest(id);
    }

    public static MBRequest build(String id, double... args){
        return new MBRequest(id, args);
    }


    /**
     * MBRequest works like a HashMap that allows duplicated keys (Multimap). When a duplicated key
     * is given, the values are inserted into a Collection.
     * @param key
     * @param value
     * @return
     */
    public MBRequest put( String key, Object value ){
        ArrayList list = new ArrayList( map.get(key) );
        if( !list.contains( value )  ) {
            map.put(key, value);
        }
        return this;
    }

    /**
     * MBRequest works like a HashMap that allows duplicated keys (Multimap). When a duplicated key
     * is given, the returned value will be a Collection, otherwise a single object
     * @param key
     * @return
     */
    public Object get( String key ){
        ArrayList list = new ArrayList( map.get( key ) );
        if( list != null && !list.isEmpty() ){
            if( list.size() == 1 ) {
                return list.get(0);
            }else{
                return list;
            }
        }
        return null;
    }

    public <T> T get( String key, T defaultValue ){
        Object obj = get( key );
        if( obj == null ){
            obj = defaultValue;
        }
        return (T)obj;
    }

    /**
     * It returns all the values for a specified key
     * @param key
     * @return
     */
    public Collection getAll( String key ){
        return map.get( key );
    }

    public Bundle convertToBundle(){
        Bundle bundle = new Bundle();
        for (String key : map.keySet()) {
            Object value = map.get(key);
            if (value instanceof Boolean) {
                bundle.putBoolean(key, (Boolean) value);
            } else if (value instanceof String) {
                bundle.putString(key, (String) value);
            } else if (value instanceof Serializable) {
                bundle.putSerializable(key, (Serializable) value);
            } else if (value instanceof Integer) {
                bundle.putInt(key, (Integer) value);
            } else if (value instanceof Float) {
                bundle.putFloat(key, (Float) value);
            } else if (value instanceof Double) {
                bundle.putDouble(key, (Double) value);
            } else if (value instanceof Short) {
                bundle.putShort(key, (Short) value);
            } else if (value instanceof Long) {
                bundle.putLong(key, (Long) value);
            } else if (value instanceof Parcelable) {
                bundle.putParcelable(key, (Parcelable) value);
            }
        }
        return bundle;
    }

    public static HashMap<String, Object> convertToMap( Bundle bundle ){
        Iterator<String> it = bundle.keySet().iterator();
        HashMap<String, Object> map = new HashMap<>();
        while( it.hasNext() ){
            String key = it.next();
            Object value = map.get( key );
            map.put( key, value );
        }
        return map;
    }



    public String getRequestId() {
        return requestId;
    }

    public double[] getValues() {
        return values;
    }

    public void setValues(double[] values) {
        this.values = values;
    }
}
