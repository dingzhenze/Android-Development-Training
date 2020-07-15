package com.example.camera;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.PathUtils;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.ExifInterface;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private static final int CAMERA_REQUEST_CODE = 1;
    private ImageView mImageView;
    private VideoView mVideoView;
    private Button btnPhoto;
    private Button btnVideo;
    private Camera mCamera;
    private SurfaceView mSurfaceView;
    private SurfaceHolder mHolder;
    private MediaRecorder mMediaRecorder;
    private boolean isRecording = false;
    private String mp4Path;

    private String[] mPermissionsArrays = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO};

    @RequiresApi(api = Build.VERSION_CODES.M)
    void PermissionCheck() {
        int permissionCheck = ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA);
        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(getApplicationContext(),"have the right to use camera",Toast.LENGTH_SHORT).show();
        } else {
            requestPermissions(mPermissionsArrays, CAMERA_REQUEST_CODE);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PermissionCheck();

        mImageView = findViewById(R.id.current_img);
        mVideoView = findViewById(R.id.current_video);
        btnPhoto = findViewById(R.id.btn_picture);
        btnVideo = findViewById(R.id.btn_video);
        mSurfaceView = findViewById(R.id.surfaceView);

        btnPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCamera.takePicture(null, null, new Camera.PictureCallback() {
                    @Override
                    public void onPictureTaken(byte[] data, Camera camera) {
                        FileOutputStream fos = null;
                        String filePath = getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath() + File.separator + "1.jpg";
                        File file = new File(filePath);
                        try {
                            fos = new FileOutputStream(file);
                            fos.write(data);
                            fos.flush();
                            Bitmap bitmap = BitmapFactory.decodeFile(filePath);
                            Bitmap rotateBitmap = rotateImage(bitmap, filePath);
                            mImageView.setVisibility(View.VISIBLE);
                            mImageView.bringToFront();
                            mVideoView.setVisibility(View.GONE);
                            mImageView.setImageBitmap(rotateBitmap);
//                            mImageView.setImageBitmap(bitmap);
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            mCamera.startPreview();
                            if(fos != null) {
                                try {
                                    fos.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                });
            }
        });

        mHolder = mSurfaceView.getHolder();
        mHolder.addCallback(new SurfaceHolder.Callback(){
            @Override
            public void surfaceCreated(SurfaceHolder holder){
                try{
                    mCamera.setPreviewDisplay(holder);
                    mCamera.startPreview();
                } catch(IOException e){
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height){
                if(holder.getSurface() == null){
                    return;
                }
                mCamera.stopPreview();
                try{
                    mCamera.setPreviewDisplay(holder);
                    mCamera.startPreview();
                } catch (IOException e){
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder){
                mCamera.stopPreview();
                mCamera.release();
                mCamera = null;
            }
        });

        btnVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                record(view);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mCamera == null) {
            initCamera();
        }
        mCamera.startPreview();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mCamera.stopPreview();
    }

    private void initCamera(){
        mCamera = Camera.open();
        Camera.Parameters parameters = mCamera.getParameters();
        parameters.setPictureFormat(ImageFormat.JPEG);
        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
        parameters.set("orientation", "portrait");
        parameters.set("rotation", 90);
        mCamera.setParameters(parameters);
        mCamera.setDisplayOrientation(90);

        mSurfaceView = findViewById(R.id.surfaceView);
    }

    private Bitmap rotateImage(Bitmap bitmap, String path){
        try{
            ExifInterface srcExif = new ExifInterface(path);
            Matrix matrix = new Matrix();
            int angle = 0;
            int orientation = srcExif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch(orientation){
                case ExifInterface.ORIENTATION_ROTATE_90:{
                    angle = 90;
                    break;
                }
                case ExifInterface.ORIENTATION_ROTATE_180:{
                    angle = 180;
                    break;
                }
                case ExifInterface.ORIENTATION_ROTATE_270:{
                    angle = 270;
                    break;
                }
                default:
                    break;
            }
            matrix.postRotate(angle);
            return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        } catch(IOException e){
            e.printStackTrace();
        }
        return null;
    }

    private boolean prepareVideoRecorder(){
        mMediaRecorder = new MediaRecorder();
        mCamera.unlock();
        mMediaRecorder.setCamera(mCamera);
        mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
        mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
        mMediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH));
        mp4Path = getOutputMediaPath();
        mMediaRecorder.setOutputFile(mp4Path);
        mMediaRecorder.setPreviewDisplay(mHolder.getSurface());
        mMediaRecorder.setOrientationHint(90);
        try{
            mMediaRecorder.prepare();
        }catch (Exception e){
            mMediaRecorder.release();
            return false;
        }
        return true;
    }

    public void record(View view){
        if(isRecording){
            btnVideo.setText("录制");
            mMediaRecorder.stop();
            mMediaRecorder.reset();
            mMediaRecorder.release();
            mMediaRecorder = null;
            mCamera.lock();
            mVideoView.setVisibility(View.VISIBLE);
            mImageView.setVisibility(View.GONE);
            mVideoView.setVideoPath(mp4Path);
            mVideoView.start();
        } else{
            if(prepareVideoRecorder()){
                btnVideo.setText("暂停");
                mMediaRecorder.start();
            }
        }
        isRecording = !isRecording;
    }

    private String getOutputMediaPath() {
        return getExternalFilesDir(Environment.DIRECTORY_MOVIES).getAbsolutePath() + File.separator + "1.mp4";
    }

}
