package edu.cmu.adroitness.client.commons.control;

import java.util.concurrent.Callable;

/**
 * Created by oscarr on 11/4/15.
 */
public class ExecutableTask<T> implements Callable<T>, Runnable {
    /**
     * Use this method if you need a result to be returned after executing the command
     * @return
     * @throws Exception
     */
    @Override
    public T call() throws Exception {
        return null;
    }

    /**
     * Use this method if you don't need a result to be returned after executing the command
     */
    @Override
    public void run() {
    }
}
