package com.yahoo.inmind.testing.middleware;

import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import android.content.Context;
import android.os.Looper;
import android.support.test.InstrumentationRegistry;
import android.test.suitebuilder.annotation.SmallTest;
import com.yahoo.inmind.comm.generic.control.MessageBroker;
import com.yahoo.inmind.comm.zmq.model.SessionMessage;
import com.yahoo.inmind.commons.control.Constants;
import com.yahoo.inmind.commons.control.ResourceLocator;
import com.yahoo.inmind.commons.control.UtilServiceAPIs;
import com.yahoo.inmind.services.multiuser.control.MultiuserService;

/**
 * Created by sakoju on 11/30/17.
 */

@SmallTest
public class MultiuserTest {

    protected static final String TAG = "Gmailtest";
    protected static Context context;
    protected static MessageBroker mb;
    protected static ResourceLocator locator;
    protected static MultiuserService multiuserService;
    protected static HashMap attributes;
    protected static String accountName = "inmind.yahoo.test@gmail.com";
    protected static String defaultUserID = "me";
    protected static String messageID="";

    @BeforeClass
    public static void setup() {
        Looper.prepare();
        context = InstrumentationRegistry.getTargetContext();
        ArrayList<String> services = new ArrayList();
        services.add(Constants.ADD_SERVICE_MULTIUSER);
        mb = MessageBroker.getInstance(context, services);
        locator = ResourceLocator.getInstance(context);
        mb.subscribe(context);
        UtilServiceAPIs.initializeCredentials(context, accountName);
        ResourceLocator.getExistingInstance().setAccount(Constants.GOOGLE_ACCOUNT, accountName);
        do {
            multiuserService = locator.lookupService(MultiuserService.class);
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } while (multiuserService == null);
        //com.jayway.awaitility.Awaitility.await().atMost(30, TimeUnit.SECONDS);
    }

    @Test
    public void startSendStopMultiuser()
    {
        String ipAddress = "tcp://127.0.0.1:5555";
        multiuserService.start(ipAddress);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        SessionMessage message = new SessionMessage("MSG_ASR", "I like Action movies");
        multiuserService.send(message);
        multiuserService.stop();
    }
}
