package edu.cmu.adroitness.client.services.generic.control;

/**
 * Created by oscarr on 9/6/16.
 */
public class GenericContainer<T>{
    private T obj;

    public GenericContainer(T obj) {
        this.obj = obj;
    }

    public T getObj() {
        return obj;
    }

    public void setObj(T obj) {
        this.obj = obj;
    }
}
