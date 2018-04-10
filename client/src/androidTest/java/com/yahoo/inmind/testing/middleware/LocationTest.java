package com.yahoo.inmind.testing.middleware;

import android.content.Context;
import com.yahoo.inmind.comm.generic.control.MessageBroker;
import com.yahoo.inmind.commons.control.ResourceLocator;
import com.yahoo.inmind.services.location.control.LocationService;
import com.yahoo.inmind.services.location.control.Plugin;
import com.yahoo.inmind.services.location.model.LocationVO;

//@RunWith(AndroidJUnit4.class)
//@SmallTest
public class LocationTest {
    private static Context context;
    private static MessageBroker mb;
    private static ResourceLocator locator;
    private static LocationService locationService;
    private static LocationVO globalLocationVO;
    Plugin plugin;


    /*@BeforeClass
    public static void setup(){
        Looper.prepare();
        context = InstrumentationRegistry.getTargetContext();
        ArrayList<String> services = new ArrayList();
        services.add( Constants.ADD_SERVICE_LOCATION );
        mb = MessageBroker.getInstance( context, services );
        locator = ResourceLocator.getInstance( context );
        do {
            locationService = locator.lookupService(LocationService.class);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }while( locationService == null );
        globalLocationVO = locationService.obtainCurrentLocation();
        //validator = DecisionRuleValidator.getInstance();

    }



    public void setWifiOn()
    {
        WifiManager wifiManager = (WifiManager) this.context.getSystemService(Context.WIFI_SERVICE);
        wifiManager.setWifiEnabled(true);
    }

    public void setWifiOff()
    {
        WifiManager wifiManager = (WifiManager) this.context.getSystemService(Context.WIFI_SERVICE);
        wifiManager.setWifiEnabled(false);

    }



    public void setGpsOn()
    {
        LocationManager locationManager = (LocationManager) this.context.getSystemService(Context.LOCATION_SERVICE);
        Boolean gpsStatus =  locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        LocationProvider locationProvider = locationManager.getProvider(LocationManager.GPS_PROVIDER);

        if(!gpsStatus)
        {
            Intent intent = new Intent("android.location.GPS_ENABLED_CHANGE");
            intent.putExtra("enabled", true);
            this.context.sendBroadcast(intent);
        }
    }
    public void setGpsOff()
    {
        LocationManager locationManager = (LocationManager) this.context.getSystemService(Context.LOCATION_SERVICE);
        Boolean gpsStatus =  locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        LocationProvider locationProvider = locationManager.getProvider(LocationManager.GPS_PROVIDER);

        if(!gpsStatus)
        {
            Intent intent = new Intent("android.location.GPS_ENABLED_CHANGE");
            intent.putExtra("enabled", false);
            this.context.sendBroadcast(intent);
        }
    }


    @Test
    public void testObtainCurrentLocationWifiOn()
    {
        setWifiOn();
        LocationVO locationVO =  locationService.obtainCurrentLocation();
        assertNotNull(locationVO.getWoeidVO().toString());
        System.out.println(locationVO.getWoeidVO().toString());
    }

    @Test
    public void testObtainCurrentLocationWhenWifiOff()
    {
        setWifiOff();
        LocationVO locationVO =  locationService.obtainCurrentLocation();
        assertNotNull(locationVO.getWoeidVO().toString());
        System.out.println(locationVO.getWoeidVO().toString());
    }

    @Test
    public void testObtainCurrentLocationWhenWifiOnGPSOff()
    {
        setWifiOn();
        setGpsOff();
        LocationVO locationVO =  locationService.obtainCurrentLocation();
        assertNotNull(locationVO.getWoeidVO().toString());
        System.out.println(locationVO.getWoeidVO().toString());
    }

    @Test
    public void testObtainCurrentLocationWhenWifiOff_GPSOn()
    {
        setWifiOff();
        setGpsOn();

        LocationVO locationVO =  locationService.obtainCurrentLocation();
        assertNotNull(locationVO.getWoeidVO().toString());
        System.out.println(locationVO.getWoeidVO().toString());
    }

    @Test
    public void testObtainCurrentLocationWhenGPSOff_WifiOff()
    {
        setWifiOff();
        setGpsOff();
        LocationVO locationVO =  locationService.obtainCurrentLocation();
        assertNotNull(locationVO.getWoeidVO().toString());
        System.out.println(locationVO.getWoeidVO().toString());
    }

    @Test
    public void testGetCurrentLocationByWoeidWhenGPSOff_WifiOff()
    {
       setWifiOff();
        setGpsOff();
        boolean assertFlag = false;
        int assertCount = 0;
        //globalLocationVO =  locationService.obtainCurrentLocation();
        locationService.getCurrentLocationByWoeid(globalLocationVO.getWoeidVO());
        ArrayList<LocationEvent> locationEvents = locationService.getHistoryLocations();
       LocationEvent event = locationEvents.get(locationEvents.size() - 1);
            if(event.getWoeid().toString() == globalLocationVO.getWoeid().toString())
            {
                assertFlag = true;
                assertCount++;
            }


        assertTrue(assertFlag);
        assertTrue(String.valueOf(assertCount), isCountNotZero(assertCount));

    }

    @Test
    public void testGetCurrentLocationByWoeidWhenGPSOn_WifiOff()
    {
        setWifiOff();
        setGpsOn();
        boolean assertFlag = false;
        int assertCount = 0;
        //globalLocationVO =  locationService.obtainCurrentLocation();
        locationService.getCurrentLocationByWoeid(globalLocationVO.getWoeidVO());
        ArrayList<LocationEvent> locationEvents = locationService.getHistoryLocations();
        LocationEvent event = locationEvents.get(locationEvents.size() - 1);
        if(event.getWoeid().toString() == globalLocationVO.getWoeid().toString())
        {
            assertFlag = true;
            assertCount++;
        }



        assertTrue(assertFlag);
        assertTrue(String.valueOf(assertCount), isCountNotZero(assertCount));

    }

    @Test
    public void testGetCurrentLocationByWoeidWhenGPSOff_WifiOn()
    {
        setWifiOn();
        setGpsOff();
        boolean assertFlag = false;
        int assertCount = 0;
        //globalLocationVO =  locationService.obtainCurrentLocation();
        locationService.getCurrentLocationByWoeid(globalLocationVO.getWoeidVO());
        ArrayList<LocationEvent> locationEvents = locationService.getHistoryLocations();
        LocationEvent event = locationEvents.get(locationEvents.size() - 1);
        if(event.getWoeid().toString() == globalLocationVO.getWoeid().toString())
        {
            assertFlag = true;
            assertCount++;
        }

        assertTrue(assertFlag);
        assertTrue(String.valueOf(assertCount), isCountNotZero(assertCount));

    }

    @Test
    public void testGetCurrentLocationByWoeidWhenGPSOn_WifiOn()
    {
        setWifiOff();
        setGpsOff();
        boolean assertFlag = false;
        int assertCount = 0;
        //globalLocationVO =  locationService.obtainCurrentLocation();
        locationService.getCurrentLocationByWoeid(globalLocationVO.getWoeidVO());
        ArrayList<LocationEvent> locationEvents = locationService.getHistoryLocations();
        LocationEvent event = locationEvents.get(locationEvents.size() - 1);
        if(event.getWoeid().toString() == globalLocationVO.getWoeid().toString())
        {
            assertFlag = true;
            assertCount++;
        }

        assertTrue(assertFlag);
        assertTrue(String.valueOf(assertCount), isCountNotZero(assertCount));

    }

    @Test
    public void testGetCurrentLocationByGoogleFusedWhenGPSOff_WifiOff()
    {
        setWifiOff();
        setGpsOff();
        boolean assertFlag = false;
        int assertCount = 0;
        //globalLocationVO =  locationService.obtainCurrentLocation();
        locationService.getCurrentLocationByGoogleFused();
        ArrayList<LocationEvent> locationEvents = locationService.getHistoryLocations();
        LocationEvent event = locationEvents.get(locationEvents.size() - 1);
        if(event.getWoeid().toString() == globalLocationVO.getWoeid().toString())
        {
            assertFlag = true;
            assertCount++;
        }

        assertTrue(assertFlag);
        assertTrue(String.valueOf(assertCount), isCountNotZero(assertCount));

    }

    @Test
    public void testGetCurrentLocationByGoogleFusedWhenGPSOn_WifiOff()
    {
        setWifiOff();
        setGpsOn();
        boolean assertFlag = false;
        int assertCount = 0;
        //globalLocationVO =  locationService.obtainCurrentLocation();
        locationService.getCurrentLocationByGoogleFused();
        ArrayList<LocationEvent> locationEvents = locationService.getHistoryLocations();
        LocationEvent event = locationEvents.get(locationEvents.size() - 1);
        if(event.getWoeid().toString() == globalLocationVO.getWoeid().toString())
        {
            assertFlag = true;
            assertCount++;
        }

        assertTrue(assertFlag);
        assertTrue(String.valueOf(assertCount), isCountNotZero(assertCount));

    }

    @Test
    public void testGetCurrentLocationByGoogleFusedWhenGPSOff_WifiOn()
    {
        setWifiOn();
        setGpsOff();
        boolean assertFlag = false;
        int assertCount = 0;
        //globalLocationVO =  locationService.obtainCurrentLocation();
        locationService.getCurrentLocationByGoogleFused();
        ArrayList<LocationEvent> locationEvents = locationService.getHistoryLocations();
        if(locationEvents != null)
        {
            if(locationEvents.size() != 0)
            {
                LocationEvent event = locationEvents.get(locationEvents.size() - 1);
                if(event.getWoeid().toString() == globalLocationVO.getWoeid().toString())
                {
                    assertFlag = true;
                    assertCount++;
                }
                else
                {
                    assertNotSame(event, globalLocationVO);
                }
            }
        }
        if(assertFlag) {

            assertTrue(String.valueOf(assertCount), isCountNotZero(assertCount));
        }


    }

    @Test
    public void testGetCurrentLocationByGoogleFusedWhenGPSOn_WifiOn()
    {
        setWifiOn();
        setGpsOn();
        boolean assertFlag = false;
        int assertCount = 0;
        //globalLocationVO =  locationService.obtainCurrentLocation();
        locationService.getCurrentLocationByGoogleFused();
        ArrayList<LocationEvent> locationEvents = locationService.getHistoryLocations();
        LocationEvent event = locationEvents.get(locationEvents.size() - 1);
        if(event.getWoeid().toString() == globalLocationVO.getWoeid().toString())
        {
            assertFlag = true;
            assertCount++;
        }

        assertTrue(assertFlag);
        assertTrue(String.valueOf(assertCount), isCountNotZero(assertCount));

    }

    @Test
    public void testGetCurrentLocationByCoordinatesWhenGPSOff_WifiOff()
    {
        setWifiOff();
        setGpsOff();
        boolean assertFlag = false;
        int assertCount = 0;
        //globalLocationVO =  locationService.obtainCurrentLocation();
        locationService.getCurrentLocationByCoordinates(globalLocationVO.getLatitude(), globalLocationVO.getLongitude());
        ArrayList<LocationEvent> locationEvents = locationService.getHistoryLocations();
        LocationEvent event = locationEvents.get(locationEvents.size() - 1);
        if(event.getWoeid().toString() == globalLocationVO.getWoeid().toString())
        {
            assertFlag = true;
            assertCount++;
        }

        assertTrue(assertFlag);
        assertTrue(String.valueOf(assertCount), isCountNotZero(assertCount));

    }

    @Test
    public void testGetCurrentLocationByCoordinatesWhenGPSOn_WifiOff()
    {
        setWifiOff();
        setGpsOn();
        boolean assertFlag = false;
        int assertCount = 0;
        //globalLocationVO =  locationService.obtainCurrentLocation();
        locationService.getCurrentLocationByCoordinates(globalLocationVO.getLatitude(), globalLocationVO.getLongitude());
        ArrayList<LocationEvent> locationEvents = locationService.getHistoryLocations();
        LocationEvent event = locationEvents.get(locationEvents.size() - 1);
        if(event.getWoeid().toString() == globalLocationVO.getWoeid().toString())
        {
            assertFlag = true;
            assertCount++;
        }

        assertTrue(assertFlag);
        assertTrue(String.valueOf(assertCount), isCountNotZero(assertCount));

    }

    @Test
    public void testGetCurrentLocationByCoordinatesWhenGPSOff_WifiOn()
    {
        setWifiOn();
        setGpsOff();
        boolean assertFlag = false;
        int assertCount = 0;
        //globalLocationVO =  locationService.obtainCurrentLocation();
        locationService.getCurrentLocationByCoordinates(globalLocationVO.getLatitude(), globalLocationVO.getLongitude());
        ArrayList<LocationEvent> locationEvents = locationService.getHistoryLocations();
        LocationEvent event = locationEvents.get(locationEvents.size() - 1);
        if(event.getWoeid().toString() == globalLocationVO.getWoeid().toString())
        {
            assertFlag = true;
            assertCount++;
        }

        assertTrue(assertFlag);
        assertTrue(String.valueOf(assertCount), isCountNotZero(assertCount));

    }

    @Test
    public void testGetCurrentLocationByCoordinatesWhenGPSOn_WifiOn()
    {
        setWifiOn();
        setGpsOn();
        boolean assertFlag = false;
        int assertCount = 0;
        //globalLocationVO =  locationService.obtainCurrentLocation();
        locationService.getCurrentLocationByCoordinates(globalLocationVO.getLatitude(), globalLocationVO.getLongitude());
        ArrayList<LocationEvent> locationEvents = locationService.getHistoryLocations();
        LocationEvent event = locationEvents.get(locationEvents.size() - 1);
        if(event.getWoeid().toString() == globalLocationVO.getWoeid().toString())
        {
            assertFlag = true;
            assertCount++;
        }

        assertTrue(assertFlag);
        assertTrue(String.valueOf(assertCount), isCountNotZero(assertCount));

    }

    @Test
    public void testSetLocationByAddressWhenGPSOn_WifiOn()
    {
        setWifiOn();
        setGpsOn();
        boolean assertFlag = false;
        int assertCount = 0;
        //globalLocationVO =  locationService.obtainCurrentLocation();
        locationService.setCurrentLocationVO(globalLocationVO);
        ArrayList<LocationEvent> locationEvents = locationService.getHistoryLocations();
        LocationEvent event = locationEvents.get(locationEvents.size() - 1);
        /*if(event.getWoeid().toString() == globalLocationVO.getWoeid().toString())
        {
            assertFlag = true;
            assertCount++;
        }*/
        /*if( event.getPlaceName() == globalLocationVO.getPlaceName())
        {
            assertFlag = true;
            assertCount++;
        }

        assertTrue(assertFlag);
        assertTrue(String.valueOf(assertCount), isCountNotZero(assertCount));

    }

    public boolean isCountNotZero(int count)
    {
        if(count >0)
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    @AfterClass
    public static void teardown(){
        mb.destroy();
    }

*/

}
