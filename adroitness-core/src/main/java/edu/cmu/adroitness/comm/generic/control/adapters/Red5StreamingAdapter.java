package edu.cmu.adroitness.comm.generic.control.adapters;

import java.util.ArrayList;

import edu.cmu.adroitness.comm.generic.model.MBRequest;
import edu.cmu.adroitness.client.commons.control.Constants;
import edu.cmu.adroitness.client.services.red5streaming.control.Red5StreamingService;

/**
 * Created by sakoju on 7/12/16.
 */
public class Red5StreamingAdapter extends ChannelAdapter {
    private static Red5StreamingAdapter instance;
    public Red5StreamingAdapter() {
        super();
    }

    public static Red5StreamingAdapter getInstance()
    {
        if(instance == null)
        {
            instance = new Red5StreamingAdapter();
        }
        return instance;
    }

    public void startStreaming( MBRequest mbRequest){
        mResourceLocator.addRequest( Red5StreamingService.class, "startStreaming",
                mbRequest.get(Constants.RED5STREAMING_SURFACEVIEW),
                mbRequest.get(Constants.RED5STREAMING_CONFIG));
    }


    public void stopStreaming( MBRequest request){
        mResourceLocator.addRequest( Red5StreamingService.class, "stopStreaming");
    }

    public void stopCamera( MBRequest request){
        mResourceLocator.addRequest( Red5StreamingService.class, "stopCamera");
     }

    public  ArrayList<String> getCameraSizes(MBRequest mbRequest){
        return mResourceLocator.lookupService(Red5StreamingService.class).getCameraSizes();
    }

    public void toggleCamera( MBRequest request){
        mResourceLocator.addRequest( Red5StreamingService.class, "toggleCamera");
     }

    public String getIPAddressOfRed5Host(MBRequest request){
        String urlString = (String) request.get(Constants.RED5STREAMING_SERVER_URL);
        return mResourceLocator.lookupService(Red5StreamingService.class).getIPAddressOfRed5Host(urlString);
    }

    public void attachFragment(MBRequest request){
        mResourceLocator.addRequest(Red5StreamingService.class, "attachFragment", request);
    }
}
