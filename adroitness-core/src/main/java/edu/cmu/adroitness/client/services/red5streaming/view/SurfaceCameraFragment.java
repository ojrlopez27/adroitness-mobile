package edu.cmu.adroitness.client.services.red5streaming.view;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;

import edu.cmu.adroitness.R;
import edu.cmu.adroitness.client.commons.control.Constants;
import edu.cmu.adroitness.client.commons.control.Util;
import edu.cmu.adroitness.client.services.red5streaming.control.Red5StreamListener;

import edu.cmu.adroitness.client.services.red5streaming.control.Red5StreamingController;
import edu.cmu.adroitness.client.services.red5streaming.control.Red5StreamingService;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SurfaceCameraFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SurfaceCameraFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SurfaceCameraFragment extends Fragment implements Red5StreamListener.Red5CameraFragmentListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "enableManualStreaming";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    public static SurfaceView getSurfaceView() {
        return surfaceView;
    }

    protected static SurfaceView surfaceView;
    Red5StreamingController red5StreamingController;
    private OnFragmentInteractionListener mListener;
    Context context;
    boolean isManualStreaming= false;

    public SurfaceCameraFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param enableManualStreaming Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SurfaceCameraFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SurfaceCameraFragment newInstance(String enableManualStreaming, String param2) {
        SurfaceCameraFragment fragment = new SurfaceCameraFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, enableManualStreaming);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        if(mParam1.equals("true"))
        {
            isManualStreaming = true;
        }
        else
        {
            isManualStreaming = false;
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.services_red5_surface_camera_fragment, container, false);
        surfaceView = (SurfaceView) view.findViewById(R.id.surfaceViewInFragment);
        if(!isManualStreaming) {
            red5StreamingController = Red5StreamingController.getInstance();
        }

        //ResourceLocator.getInstance(getActivity()).lookupService(Red5StreamingService.class).createConfigVO();
        if(surfaceView!=null) {
            Log.d("SurfaceCameraFragment", "onCreateView");
        }
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle bundle)
    {
        super.onViewCreated(view, bundle);
        Log.d("StreamingService","onViewCreated");
        // we need this message to tell the top app that Streaming has added the
        // fragment and therefore can start streaming
        Red5StreamingService.sendRed5StreamingEvent(Constants.RED5_STREAMING_STATUS_READY);
        if(!isManualStreaming) {
            notifySurfaceViewCreated((SurfaceView) view.findViewById(R.id.surfaceViewInFragment));
        }
    }

    @Override
    public void onResume()
    {
        super.onResume();
        Log.d("StreamingService","onViewCreated");
        if(!isManualStreaming) {
            //notifySurfaceViewCreated(surfaceView);
        }
    }
    // TODO: Rename method, update argument and hook method into UI event
    /*public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }*/

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        this.context = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        //void onFragmentInteraction(Uri uri);
         void setSurfaceView(SurfaceView surfaceView);
    }

    @Override
    public void onPause()
    {
        super.onPause();
        Log.d("StreamingService","onPause");
        if(isManualStreaming) {
            notifyFragmentOnPause(this);
        }
    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        Log.d("StreamingService","onDestroyView");
        //red5Streaming.notifyFragmentOnPause(this);
    }

    @Override
    public synchronized void notifySurfaceViewCreated(final SurfaceView surfaceViewFromFragment) {
        Util.execute(new Runnable() {
            @Override
            public void run() {


            surfaceView = surfaceViewFromFragment;
            red5StreamingController.createConfigVO();
            red5StreamingController.startStreaming( red5StreamingController.getConfigVO(), surfaceView);
        Log.d("StreamingService","notifySurfaceViewCreated");
            }
        });

    }

    @Override
    public synchronized void notifyFragmentOnPause(Fragment fragment) {
        Util.execute(new Runnable() {
            @Override
            public void run() {
                       red5StreamingController.stopStreaming();
                Log.d("StreamingService","notifyFragmentOnPause");

                Log.d("StreamingService","notifyFragmentOnPause - closed");
            }
        });
    }

}
