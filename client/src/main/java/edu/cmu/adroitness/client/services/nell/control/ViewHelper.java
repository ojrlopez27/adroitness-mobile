package edu.cmu.adroitness.client.services.nell.control;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import edu.cmu.adroitness.client.commons.control.Constants;
import edu.cmu.adroitness.client.services.nell.view.NELLActivity;
import edu.cmu.adroitness.comm.generic.control.MessageBroker;
import edu.cmu.adroitness.comm.generic.model.MBRequest;
import edu.cmu.adroitness.comm.nell.model.NellMacroReaderEvent;
import edu.cmu.adroitness.comm.nell.model.NellMicroReaderEvent;

/**
 * Created by oscarr on 1/3/15.
 */
public class ViewHelper {
    private static ViewHelper instance;
    private MessageBroker mMB;
    private NELLActivity mActivity;



    private ViewHelper(NELLActivity activity) {
        // Controllers
        if( activity != null ) {
            mActivity = activity;
        }
        mMB = MessageBroker.getInstance( mActivity );
        mMB.subscribe( this );
    }

    public static ViewHelper getInstance(NELLActivity a) {
        if (instance == null) {
            instance = new ViewHelper(a);
        }
        return instance;
    }



    // ****************************** CALLS TO NELL ***********************************************

    public void runMacroReading(String entity) {
        mMB.send( this, MBRequest.build( Constants.MSG_NELL_ANALIZE_ENTITY)
                .put( Constants.NELL_ENTITY, entity) );
    }

    public void runMicroReading(String document) {
        mMB.send( this, MBRequest.build( Constants.MSG_NELL_ANALIZE_DOCUMENT)
                .put( Constants.NELL_DOCUMENT, document) );
    }


    // ****************************** EVENT HANDLERS ***********************************************

    /**
     * This event handler processes the response from NEIL Service
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(NellMacroReaderEvent event){
        mActivity.getResults( ).setText( event.getResultVO().getJsonPrettyString() );
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(NellMicroReaderEvent event){
        mActivity.getResults( ).setText( event.getResultVO().getJsonPrettyString() );
    }

    public void subscribe() {
        mMB.subscribe( this );
    }

    public void unsubscribe() {
        mMB.unsubscribe( this );
    }


}
