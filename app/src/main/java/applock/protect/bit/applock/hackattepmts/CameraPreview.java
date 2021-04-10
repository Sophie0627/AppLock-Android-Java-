package applock.protect.bit.applock.hackattepmts;

import android.content.Context;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

//@SuppressWarnings("deprecation")
public class CameraPreview extends SurfaceView implements
SurfaceHolder.Callback {
private SurfaceHolder mSurfaceHolder;
private Camera mCamera;

// Constructor that obtains context and camera
@SuppressWarnings("deprecation")
public CameraPreview(Context context, Camera camera) {
super(context);
this.mCamera = camera;
this.mSurfaceHolder = this.getHolder();
this.mSurfaceHolder.addCallback(this);
this.mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

}

@Override
public void surfaceCreated(SurfaceHolder surfaceHolder) {
try {
    mCamera.setPreviewDisplay(surfaceHolder);
    mCamera.startPreview();
} catch (Exception e) {
    // left blank for now
}
}

@Override
public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
	try
	{
		if(mCamera != null){
			this.getHolder().removeCallback(this);
			mCamera.stopPreview();
			mCamera.release();
		}
	}
	catch(Exception ex){
		Log.v("The Exception is:", ex.toString());
	}
}

@Override
public void surfaceChanged(SurfaceHolder surfaceHolder, int format,
    int width, int height) {
// start preview with new settings
try {
    mCamera.setPreviewDisplay(surfaceHolder);
    mCamera.startPreview();
} catch (Exception e) {
    // intentionally left blank for a test
}
}
}