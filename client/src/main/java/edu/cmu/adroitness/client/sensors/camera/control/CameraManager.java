package edu.cmu.adroitness.client.sensors.camera.control;

import android.content.Context;
import android.hardware.Camera;
import android.widget.Toast;

public class CameraManager {
	private Camera mCamera;
	private Context mContext;

	
	public CameraManager(Context context) {
		mContext = context;
		// Create an instance of Camera
        mCamera = getCameraInstance();
	}

	public Camera getCamera() {
		return mCamera;
	}

	private void releaseCamera() {
		if (mCamera != null) {
			mCamera.release(); // release the camera for other applications
			mCamera = null;
		}
	}
	
	public void onPause() {
		releaseCamera();
	}
	
	public void onResume() {
		if (mCamera == null) {
			mCamera = getCameraInstance();
		}
		
		Toast.makeText(mContext, "preview size = " + mCamera.getParameters().getPreviewSize().width + 
				", " + mCamera.getParameters().getPreviewSize().height, Toast.LENGTH_LONG).show(); 
	}
	
	/** A safe way to get an instance of the Camera object. */
	private static Camera getCameraInstance(){
	    Camera c = null;
	    try {
            // for front camera use either Method 1 or 2
            // Method 1
            Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
            for (int camNo = 0; camNo < Camera.getNumberOfCameras(); camNo++) {
                Camera.CameraInfo camInfo = new Camera.CameraInfo();
                Camera.getCameraInfo(camNo, camInfo);

                if (camInfo.facing == (Camera.CameraInfo.CAMERA_FACING_FRONT)) {
                    c = Camera.open(camNo);
                }
            }
            if (c == null) {
                // no front-facing camera, use the first back-facing camera instead.
                // you may instead wish to inform the user of an error here...
                c = Camera.open();
            }

            /*
            // Method 2
            c = Camera.open(); // attempt to get a Camera instance
            Camera.Parameters p = c.getParameters();
            p.set("camera-id",2); // if this doesn't work. Comment this line and uncomment next line
            //p.set("camera_id,2"); // some phones use camera-id, some camera_id
            c.setParameters(p);
            */
        }
	    catch (Exception e){
	        // Camera is not available (in use or does not exist)
	    }
	    return c; // returns null if camera is unavailable
	}
	
}
