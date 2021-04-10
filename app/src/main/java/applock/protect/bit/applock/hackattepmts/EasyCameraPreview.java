package applock.protect.bit.applock.hackattepmts;

import android.content.Context;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;


public class EasyCameraPreview extends SurfaceView implements  SurfaceHolder.Callback {

    private SurfaceHolder mSurfaceHolder;
    private CameraPreviewHandler cameraPreviewHandler;




    public EasyCameraPreview(Context context,CameraPreviewHandler cameraPreviewHandler )
    {
        super(context);
        this.cameraPreviewHandler = cameraPreviewHandler;
        this.mSurfaceHolder = this.getHolder();
        this.mSurfaceHolder.addCallback(this);
        this.mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        Log.i("hackatempt","EasyCameraPreview constuctor");
    }


    @Override
    public void surfaceCreated(SurfaceHolder holder)
    {
        // open it if code doesnt work correctly

        /*try {
            cameraPreviewHandler.startPreview(mSurfaceHolder);
            Log.i("hackatempt","surface created");
        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
    {
        try {
            cameraPreviewHandler.startPreview(mSurfaceHolder);
            Log.i("hackatempt","surface changed");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder)
    {

    }


}


interface CameraPreviewHandler
{
    void startPreview(SurfaceHolder mSurfaceHolder);

}