package edu.cmu.adroitness.comm.tests.model;

import android.util.Log;

/**
 * Created by oscarr on 9/5/17.
 */

public class PerformanceTestVO {
    private String messageId;
    private long time;

    public PerformanceTestVO(String messageId) {
        super();
        time = System.currentTimeMillis();
        this.messageId = messageId;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public long getTotalTime(){
        time = System.currentTimeMillis() - time;
        Log.d("PerformanceTest",String.format("%s\t%s", messageId, time));
        return time;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }
}
