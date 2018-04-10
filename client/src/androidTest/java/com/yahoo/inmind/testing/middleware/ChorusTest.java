package com.yahoo.inmind.testing.middleware;

import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;

import android.content.Context;
import android.os.Looper;
import android.support.test.InstrumentationRegistry;

import android.test.suitebuilder.annotation.SmallTest;
import com.google.common.eventbus.Subscribe;
import com.yahoo.inmind.comm.chorus.model.ChorusEvent;
import com.yahoo.inmind.comm.generic.control.MessageBroker;
import com.yahoo.inmind.commons.control.Constants;
import com.yahoo.inmind.commons.control.ResourceLocator;
import com.yahoo.inmind.commons.control.UtilServiceAPIs;
import com.yahoo.inmind.services.chorus.control.ChorusService;

/**
 * Created by sakoju on 3/17/17.
 */

@SmallTest
public class ChorusTest {
    protected static final String TAG = "Gmailtest";
    protected static Context context;
    protected static MessageBroker mb;
    protected static ResourceLocator locator;
    protected static ChorusService chorusService;
    protected static ChorusEvent chorusEvent;
    protected static String accountName = "inmind.yahoo.test@gmail.com";

    @BeforeClass
    public static void setup() {
        Looper.prepare();
        context = InstrumentationRegistry.getTargetContext();
        ArrayList<String> services = new ArrayList();
        services.add(Constants.ADD_SERVICE_GMAIL_SERVICE);
        mb = MessageBroker.getInstance(context, services);
        locator = ResourceLocator.getInstance(context);
        UtilServiceAPIs.initializeCredentials(context, accountName);
        ResourceLocator.getExistingInstance().setAccount(Constants.GOOGLE_ACCOUNT, accountName);
        do {
            chorusService = locator.lookupService(ChorusService.class);
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } while (chorusService == null);

    }

    @Test
    public void startConversation()
    {
        chorusService.getConvIdStatus("\"Hi, what is weather like in New York\"", Constants.TypeOfQuery.QUERY_CONV_ID);
        org.junit.Assert.assertNotNull(chorusService.getChorusServiceData().getChorusQueryResultVO());
        org.junit.Assert.assertNotNull(chorusService.getChorusServiceData().getBeginChatResultVO());
        org.junit.Assert.assertNotNull(chorusService.getChorusServiceData().getChorusReceiveAllChatResultVO());
    }

    @Test
    public void endConversation() {

        chorusService.endConversation(new Boolean(false));
        //org.junit.Assert.assertTrue(sentEmails.size() > 0);
    }

    @Subscribe
    public void onEventMainThread(ChorusEvent event)
    {

    }
}