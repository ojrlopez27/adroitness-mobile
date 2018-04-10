package edu.cmu.adroitness.client.commons.control;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by oscarr on 2/25/16.
 */
public class MiddlewareException extends RuntimeException {

    public MiddlewareException(String detailMessage) {
        super(detailMessage);
    }

    public MiddlewareException(String detailMessage, Context app) {
        super(detailMessage);
        Toast.makeText(app.getApplicationContext(), detailMessage, Toast.LENGTH_LONG).show();
    }
}
