package applock.protect.bit.applock.hackattepmts;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;
import android.view.SurfaceHolder;
import android.widget.Toast;

import net.bozho.easycamera.DefaultEasyCamera;
import net.bozho.easycamera.EasyCamera;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import applock.protect.bit.applock.Security.SecurityLocksCommon;
import applock.protect.bit.applock.Security.SecurityLocksSharedPreferences;


public class   HackAttempt implements CameraPreviewHandler
{
    private HackAttempt()
    {
    }
    private static HackAttempt instance = new HackAttempt();

    private ArrayList<HackAttemptEntity> HackAttemptEntitys = null;

    private EasyCamera camera;
    private EasyCameraPreview cameraPreview;
    private EasyCamera.CameraActions actions;
    private EasyCamera.PictureCallback callback;
    private SurfaceHolderHandler surfaceHolderHandler;
    private String wrongPassword = "";

    public static HackAttempt getInstance()
    {
       return instance;
    }

    public void destroyCamera()
    {
        try
        {
            cameraPreview = null;
            actions = null;
            surfaceHolderHandler = null;
            callback = null;
            //camera.stopPreview();
            camera.getRawCamera().release();
            camera = null;
            Log.i("hackatempt","hackattempt destroyed");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

     public void initCamera(final Context context,SurfaceHolderHandler surfaceHolderHandler)
    {
           if (camera == null) {

            try {
                PackageManager packageManager = context.getPackageManager();

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD && packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA))
                {
                    this.surfaceHolderHandler = surfaceHolderHandler;
                    camera = DefaultEasyCamera.open(1); // front camera is 1
                    cameraPreview = new EasyCameraPreview(context,this);
                    //surfaceHolderHandler.addSurfaceHolderToView(cameraPreview); // implement in login

                     callback = new EasyCamera.PictureCallback() {
                        public void onPictureTaken(byte[] data, EasyCamera.CameraActions actions) {
                            savePicture(context,data,wrongPassword);
                            Log.i("hackatempt","savepicture in onpictureTaken Callback");
                        }
                    };

                // interface implement to findview in layout and add it
                //SecurityLocksCommon.IsPreviewStarted = true;
                }

            } catch (Exception ex) {
                //SecurityLocksCommon.IsPreviewStarted = false;
                ex.printStackTrace();
            }
        }
    }

    public void addSurfaceHolderToView()
    {
        try
        {
            surfaceHolderHandler.addSurfaceHolderToView(cameraPreview);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void startPreview(SurfaceHolder mSurfaceHolder)
    {
        try
        {
            actions = camera.startPreview(mSurfaceHolder);
            Log.i("hackatempt","startpreview callback");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }




    public void takeHackAttemptPicture(String wrongPassword)
    {
        try
        {
            this.wrongPassword = wrongPassword;
            actions.takePicture(EasyCamera.Callbacks.create().withRestartPreviewAfterCallbacks(true).withJpegCallback(callback));
            Log.i("hackatempt","takepicture after call from action");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void savePicture(Context context, byte[] data, String wrongPassword)
    {

        String hackAttemptPath = "";
        File pictureFileDir = new File(SecurityLocksCommon.StoragePath + SecurityLocksCommon.HackAttempts);

        if(!pictureFileDir.exists()){
            pictureFileDir.mkdirs();
        }

        UUID uuid = UUID.randomUUID();
        String randomUUIDString = uuid.toString();

        File pictureFile = new File(SecurityLocksCommon.StoragePath + SecurityLocksCommon.HackAttempts + randomUUIDString + "#jpg");

        hackAttemptPath = SecurityLocksCommon.StoragePath + SecurityLocksCommon.HackAttempts + randomUUIDString + "#jpg";

        if(!pictureFile.exists())
            try {
                pictureFile.createNewFile();
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }

        if (pictureFile != null)
        {
            try
            {
                FileOutputStream fos = new FileOutputStream(pictureFile);
                fos.write(data);
                fos.close();

                AddHackAttempToSharedPreference(context,wrongPassword,hackAttemptPath);
            }
            catch (FileNotFoundException e)
            {
                Toast.makeText(context, "File not found exception", Toast.LENGTH_SHORT).show();
            }
            catch (IOException e)
            {
                Toast.makeText(context, "IO Exception", Toast.LENGTH_SHORT).show();
            }

            //camera.startPreview();
        }

        Log.i("hackatempt","savePicture method");
    }

    private void AddHackAttempToSharedPreference(Context con, String WrongPassword, String hackAttemptPath)
    {

        SecurityLocksSharedPreferences securityCredentialsSharedPreferences = SecurityLocksSharedPreferences.GetObject(con);

        long yourmilliseconds = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy HH:mm");

        Date resultdate = new Date(yourmilliseconds);
        System.out.println(sdf.format(resultdate));

        HackAttemptEntity hackAttemptEntity = new HackAttemptEntity();
        hackAttemptEntity.SetLoginOption(securityCredentialsSharedPreferences.GetLoginType());
        hackAttemptEntity.SetWrongPassword(WrongPassword);
        hackAttemptEntity.SetImagePath(hackAttemptPath);
        hackAttemptEntity.SetHackAttemptTime(resultdate.toString());
        hackAttemptEntity.SetIsCheck(false);


        HackAttemptEntitys = new ArrayList<HackAttemptEntity>();

        HackAttemptsSharedPreferences hackAttemptsSharedPreferences = HackAttemptsSharedPreferences.GetObject(con);
        HackAttemptEntitys = hackAttemptsSharedPreferences.GetHackAttemptObject();
        if(HackAttemptEntitys == null){
            HackAttemptEntitys = new ArrayList<HackAttemptEntity>();
            HackAttemptEntitys.add(hackAttemptEntity);
        }else{
            HackAttemptEntitys.add(hackAttemptEntity);
        }

        hackAttemptsSharedPreferences.SetHackAttemptObject(HackAttemptEntitys);

    }



}

