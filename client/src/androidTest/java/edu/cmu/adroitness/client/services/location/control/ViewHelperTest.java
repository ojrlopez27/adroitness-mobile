package edu.cmu.adroitness.client.services.location.control;

/**
 * Created by oromero on 3/18/16.
 */
//@RunWith(AndroidJUnit4.class)
//@SmallTest
public class ViewHelperTest {
        //extends ActivityInstrumentationTestCase2<LocationActivity> {
   /* private static final String TAG = "RuleValidationCalendarTest";
    private static Context context;
    private static MessageBroker mb;
    private static ResourceLocator locator;
    private static LocationService locationService;
    private static LocationVO eventVO;
    private static HashMap attributes;
    private static ViewHelper locationViewHelper;
    private static ArrayList<LocationEvent> locationVOs;
    private static LocationEvent locationEvent;

    public ViewHelperTest() {
        super(LocationActivity.class);
    }

    @BeforeClass
    public void setup() throws Exception
    {
        super.setUp();
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

        locationViewHelper = ViewHelper.getInstance(getActivity());
        locationEvent = locationViewHelper.getCurrentLocation();
        locationVOs =  locationService.getHistoryLocations();

        attributes = new HashMap();
        attributes.put(Constants.ACTION_TYPE, Constants.TOAST);
        attributes.put(Constants.TOAST_MESSAGE, "There is a pending location event");
    }

    public void testisLocationDataComplete()
    {


    }


    public void testSetDefaultCurrentCoordinates() throws Exception {

    }

    public void testSetWriteReadLocationFromFile() throws Exception {

    }

    public void testGetCurrentLocation() throws Exception {

    }

    public void testGetLocation() throws Exception {

    }

    public void testGetLocation1() throws Exception {

    }

    public void testResetCurrentLocation() throws Exception {
        LocationEvent oldLocation = locationEvent;
        locationViewHelper.resetCurrentLocation();
        locationEvent = locationViewHelper.getCurrentLocation();
        assertEquals((Object)oldLocation,(Object)locationEvent);
    }

    public void testEnableHistoryRecord() throws Exception {

    }

    public void testGetHistoryLocations() throws Exception {

    }

    public void testResetHistoryLocations() throws Exception {

    }

    public void testSetMaxNumHistoryLocations() throws Exception {

    }
*/
}