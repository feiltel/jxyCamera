package com.jinxiaoyu.camera;

import android.hardware.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

/**
 * Created by Administrator on 2018/7/11.
 */

public class CameraActivity extends AppCompatActivity {
    private CameraSurfaceView cameraSurfaceView;
    private Button takePhoto_bt;
    private ImageButton revert_ib;
    private boolean isClick = true;

    private static final String PATH_IMAGES = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "easy_check";
    public int CAMERA_POS = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        initData();
    }

    private void initData() {
        cameraSurfaceView = findViewById(R.id.sv_camera);
        takePhoto_bt = findViewById(R.id.take_photo_bt);
        revert_ib = findViewById(R.id.revert_ib);
        takePhoto_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePhoto();
            }
        });

        revert_ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (CAMERA_POS == 0) {
                    cameraSurfaceView.safeCameraOpen(Camera.CameraInfo.CAMERA_FACING_FRONT);
                    CAMERA_POS = 1;
                    cameraSurfaceView.startPreview();
                } else {
                    cameraSurfaceView.safeCameraOpen(Camera.CameraInfo.CAMERA_FACING_BACK);
                    CAMERA_POS = 0;
                    cameraSurfaceView.startPreview();
                }
                //revertCamera();
            }
        });
    }

//    private void revertCamera() {
//        int cameraCount = 0;
//        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
//        cameraCount = Camera.getNumberOfCameras();
//        for (int i = 0; i < cameraCount; i++) {
//            Camera.getCameraInfo(i, cameraInfo);
//            if (cameraPosition == 1) {
//                if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
//                    cameraSurfaceView.startPreview();
//                }
//            }
//        }
//    }

    public void takePhoto() {
        if (isClick) {
            isClick = false;
            cameraSurfaceView.takePicture(mShutterCallback, rawPictureCallback, jpegPictureCallback);
        }
    }

    //拍照快门的回调
    private Camera.ShutterCallback mShutterCallback = new Camera.ShutterCallback() {
        @Override
        public void onShutter() {

        }
    };
    //拍照完成之后返回原始数据的回调
    private Camera.PictureCallback rawPictureCallback = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {

        }
    };
    //拍照完成之后返回压缩数据的回调
    private Camera.PictureCallback jpegPictureCallback = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            cameraSurfaceView.startPreview();
            saveFile(data);
            Toast.makeText(CameraActivity.this, "拍照成功", Toast.LENGTH_SHORT).show();
            isClick = true;

        }
    };

    //保存图片到硬盘
    public void saveFile(byte[] data) {
        String fileName = UUID.randomUUID().toString() + ".jpg";
        FileOutputStream outputStream = null;
        try {
            File file = new File(PATH_IMAGES);
            if (!file.exists()) {
                file.mkdirs();
            }
            outputStream = new FileOutputStream(PATH_IMAGES + File.separator + fileName);
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream);
            bufferedOutputStream.write(data, 0, data.length);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
