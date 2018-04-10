package com.yahoo.inmind.testing.middleware;

import android.content.Context;
import android.os.Looper;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.SmallTest;

import com.yahoo.inmind.comm.generic.control.MessageBroker;
import com.yahoo.inmind.commons.control.Constants;
import com.yahoo.inmind.commons.control.ResourceLocator;
import com.yahoo.inmind.services.neil.control.NEILService;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

/**
 * Created by oscarr on 3/1/16.
 */
@RunWith(AndroidJUnit4.class)
@SmallTest
public class NeilTest {
    private static Context context;
    private static MessageBroker mb;
    private static ResourceLocator locator;
    private static NEILService neilService;

    @BeforeClass
    public static void setup(){
        Looper.prepare();
        context = InstrumentationRegistry.getTargetContext();
        ArrayList<String> services = new ArrayList();
        services.add( Constants.ADD_SERVICE_NEIL );
        mb = MessageBroker.getInstance( context, services );
        locator = ResourceLocator.getInstance( context );
        do {
            neilService = locator.lookupService(NEILService.class);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }while( neilService == null );
    }

    @AfterClass
    public static void teardown(){
        mb.destroy();
    }

    @Test
    public void testGetEntities(){
        neilService.getEntitiesFromURL( "http://blogs.reuters.com/great-debate/files/2013/07/obama-best.jpg");
    }
}
