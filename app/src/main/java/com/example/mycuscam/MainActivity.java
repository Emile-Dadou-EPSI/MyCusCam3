package com.example.mycuscam;

import android.content.Context;
import android.graphics.ImageFormat;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.TotalCaptureResult;
import android.media.Image;
import android.media.ImageReader;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Size;
import android.util.SparseIntArray;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;


public class MainActivity extends AppCompatActivity {

    private Button btnCapture;
    private TextureView txtV;

    // Check state orientation of output image
    private static final SparseIntArray ORIENTATIONS = new SparseIntArray();
    static {
        ORIENTATIONS.append(Surface.ROTATION_0, 90);
        ORIENTATIONS.append(Surface.ROTATION_90, 0);
        ORIENTATIONS.append(Surface.ROTATION_180, 270);
        ORIENTATIONS.append(Surface.ROTATION_270, 180);
    }

    private String cameraId;
    private CameraDevice cameraDevice;
    private CameraCaptureSession cameraCaptureSessions;
    private CaptureRequest.Builder captureRequestBuilder;
    private Size imgDimension;
    private ImageReader imageReader;

    // Save to file
    private File file;
    private static final int REQUEST_CAMERA_PERMISSION = 200;
    private Boolean mFlashSupported;
    private Handler mBackgroundHandler;
    private HandlerThread mBackgroundthread;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtV = (TextureView) findViewById(R.id.txtureV);
        assert txtV != null;
        txtV.setSurfaceTextureListener(new TextureView.SurfaceTextureListener() {
            @Override
            public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
                openCamera();
            }

            @Override
            public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

            }

            @Override
            public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
                return false;
            }

            @Override
            public void onSurfaceTextureUpdated(SurfaceTexture surface) {

            }
        });
        btnCapture = (Button) findViewById(R.id.button);
        assert btnCapture != null;
        btnCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    takePicture();
                } catch (CameraAccessException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void takePicture() throws CameraAccessException {
        if (cameraDevice == null) return;
        CameraManager manager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try {
            CameraCharacteristics characteristics = manager.getCameraCharacteristics(cameraDevice.getId());
            Size[] jpegsizes = null;
            if (characteristics != null) {
                jpegsizes = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)
                        .getOutputSizes(ImageFormat.JPEG);

                // Capture image with custom size
                int width = 640;
                int height = 450;
                if (jpegsizes != null && jpegsizes.length > 0) {
                    width = jpegsizes[0].getWidth() ;
                    height = jpegsizes[0].getHeight();
                }

                ImageReader reader = ImageReader.newInstance(width, height, ImageFormat.JPEG, 1);
                List<Surface> outputsurface = new ArrayList<>(2);
                outputsurface.add(reader.getSurface());
                outputsurface.add(new Surface(txtV.getSurfaceTexture()));

                final CaptureRequest.Builder captureBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);
                captureBuilder.addTarget(reader.getSurface());
                captureBuilder.set(CaptureRequest.CONTROL_AE_MODE, CameraMetadata.CONTROL_AF_MODE_AUTO);

                // Check orientation on device
                int rotation = getWindowManager().getDefaultDisplay().getRotation();
                captureBuilder.set(CaptureRequest.JPEG_ORIENTATION, ORIENTATIONS.get(rotation));

                file = new File(String.valueOf(Environment.getExternalStorageDirectory()) + "/" + new UUID().randomUUID().toString()+ ".jpg");
                ImageReader.OnImageAvailableListener readerListener = new ImageReader.OnImageAvailableListener() {
                    @Override
                    public void onImageAvailable(ImageReader reader) {
                        Image image = null;
                        try {
                            image = reader.acquireLatestImage();
                            ByteBuffer buffer = image.getPlanes()[0].getBuffer();
                            byte[] bytes = new byte[buffer.capacity()];
                            buffer.get(bytes);
                        } finally {
                            if (image != null) image.close();
                        }
                    }
                    private void save(byte[] bytes) throws IOException {
                        OutputStream outputStream = null;
                        try {
                            outputStream = new FileOutputStream(file);
                            outputStream.write(bytes);
                        } finally {
                            if (outputStream != null) {
                                outputStream.close();
                            }
                        }
                    }
                };
                reader.setOnImageAvailableListener(readerListener, mBackgroundHandler);
                final CameraCaptureSession.CaptureCallback captureCallback = new CameraCaptureSession.CaptureCallback() {
                    @Override
                    public void onCaptureCompleted(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull TotalCaptureResult result) {
                        super.onCaptureCompleted(session, request, result);
                        Toast.makeText(MainActivity.this, "Saved" + file , Toast.LENGTH_LONG).show();
                        try {
                            createCameraPreview();
                        } catch (CameraAccessException e) {
                            e.printStackTrace();
                        }
                    }
                };
                cameraDevice.createCaptureSession(outputsurface, new CameraCaptureSession.StateCallback() {
                    @Override
                    public void onConfigured(@NonNull CameraCaptureSession session) {
                        try {
                            cameraCaptureSessions.capture(captureBuilder.build(), captureCallback, mBackgroundHandler);
                        } catch (CameraAccessException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onConfigureFailed(@NonNull CameraCaptureSession session) {

                    }
                }, mBackgroundHandler   );
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private void createCameraPreview() throws CameraAccessException {
        SurfaceTexture texture = txtV.getSurfaceTexture();
        assert texture != null;
        texture.setDefaultBufferSize(imgDimension.getWidth(), imgDimension.getHeight());
        Surface surface = new Surface(texture);
        captureRequestBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
        cameraDevice.createCaptureSession(Arrays.asList(surface), new CameraCaptureSession.StateCallback() {
            @Override
            public void onConfigured(@NonNull CameraCaptureSession session) {
                if (cameraDevice == null) {
                    return;
                }
                cameraCaptureSessions = session;
                updatePreview();
            }

            @Override
            public void onConfigureFailed(@NonNull CameraCaptureSession session) {
                Toast.makeText(MainActivity.this, "Changed", Toast.LENGTH_LONG).show();
            }
        }, null);
    }

    private void updatePreview() {
        if (cameraDevice == null) {
            Toast.makeText(this, "Error", Toast.LENGTH_LONG).show();
        }
        captureRequestBuilder.set(CaptureRequest.CONTROL_AE_MODE, CaptureRequest.CONTROL_AF_MODE_AUTO);
        try {
            cameraCaptureSessions.setRepeatingRequest(captureRequestBuilder.build(), null, mBackgroundHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private void openCamera() {
        CameraManager manager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try {

        }
    }
}
