package phone.number.scanner.activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraX;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageAnalysisConfig;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.core.PreviewConfig;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.TextureView;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraX;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureConfig;
import androidx.camera.core.Preview;
import androidx.camera.core.PreviewConfig;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.content.pm.PackageManager;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Environment;
import android.util.Rational;
import android.util.Size;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;

import java.io.File;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import phone.number.scanner.R;
import phone.number.scanner.adapters.EmailAdapter;
import phone.number.scanner.adapters.PhoneNumberAdapter;
import phone.number.scanner.models.Email;
import phone.number.scanner.models.PhoneNumber;

public class ScannerActivity extends AppCompatActivity {
String task;
    private int REQUEST_CODE_PERMISSIONS = 101;
    private final String[] REQUIRED_PERMISSIONS = new String[]{"android.permission.CAMERA", "android.permission.WRITE_EXTERNAL_STORAGE"};
    TextureView textureView;
    Bitmap imageBmp;
    EmailAdapter emailAdapter;
    PhoneNumberAdapter phoneNumberAdapter;
    TextRecognizer recognizer;
RecyclerView recyclerView;
    List<Email> emailList = new ArrayList<>();
    List<PhoneNumber> phoneNumberList = new ArrayList<>();
    int sdf;
    Handler handler;
    Runnable r;
    String MyTask = "";
    Boolean isTextRecognizingRunnableRuunning;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scaner);


        getStringFromExtras();

        textureView = findViewById(R.id.view_finder);

        if(allPermissionsGranted()){
            startCamera(); //start camera if permission has been granted by user
        } else{
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS);
        }


        initRecyclerView();

    }

    private void initRecyclerView() {
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        emailAdapter = new EmailAdapter(emailList);
//        phoneNumberAdapter = new PhoneNumberAdapter(phoneNumberList);

        if (MyTask.equalsIgnoreCase("email")) {
            recyclerView.setAdapter(emailAdapter);
        }

//        else if (MyTask.equalsIgnoreCase("website")) {
//            recyclerView.setAdapter(mAdapter);
//        } else if (MyTask.equalsIgnoreCase("bar_code")) {
//            recyclerView.setAdapter(mAdapter);
//        } else  if (MyTask.equalsIgnoreCase("phone_number")) {
//
//        }


    }

    private void getStringFromExtras() {

        Intent intent = getIntent();
        if (intent.getStringExtra("task") == null) {
            
        }else {
            if (intent.getStringExtra("task").equalsIgnoreCase("phone_number")) {

                startPhoneNumberScanninng();
                MyTask = "phone_number";
                
            } else  if (intent.getStringExtra("task").equalsIgnoreCase("email")){
                startEmailScanning();
                MyTask = "email";
            } else  if (intent.getStringExtra("task").equalsIgnoreCase("website")){
                startWebsiteScanning();
                MyTask = "website";
            } else  if (intent.getStringExtra("task").equalsIgnoreCase("bar_code")){
                startBarCodeScanning();
                MyTask = "bar_code";
            }
        }
    }

    private void startBarCodeScanning() {
    }

    private void startWebsiteScanning() {
    }

    private void startEmailScanning() {
    }

    private void startPhoneNumberScanninng() {
    }



    private void startCameraSource() {

//        TextRecognizer recognizer = TextRecognition.getClient();
//        PreviewConfig preview = new PreviewConfig.Builder<>()

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void startCamera() {

        CameraX.unbindAll();

        Rational aspectRatio = new Rational (textureView.getWidth(), textureView.getHeight());
        Size screen = new Size(textureView.getWidth(), textureView.getHeight()); //size of the screen


        PreviewConfig pConfig = new PreviewConfig.Builder().setTargetAspectRatio(aspectRatio).setTargetResolution(screen).build();
        Preview preview = new Preview(pConfig);

        preview.setOnPreviewOutputUpdateListener(
                new Preview.OnPreviewOutputUpdateListener() {
                    //to update the surface texture we  have to destroy it first then re-add it
                    @Override
                    public void onUpdated(Preview.PreviewOutput output){
                        ViewGroup parent = (ViewGroup) textureView.getParent();
                        parent.removeView(textureView);
                        parent.addView(textureView, 0);

                        textureView.setSurfaceTexture(output.getSurfaceTexture());
                        updateTransform();

                        startTextRecognizinngRunnable();
                    }
                });




        ImageAnalysisConfig imgAConfig = new ImageAnalysisConfig.Builder().setImageReaderMode(ImageAnalysis.ImageReaderMode.ACQUIRE_LATEST_IMAGE).build();
        ImageAnalysis analysis = new ImageAnalysis(imgAConfig);



        ImageCaptureConfig imageCaptureConfig = new ImageCaptureConfig.Builder().setCaptureMode(ImageCapture.CaptureMode.MIN_LATENCY)
                .setTargetRotation(getWindowManager().getDefaultDisplay().getRotation()).build();
        final ImageCapture imgCap = new ImageCapture(imageCaptureConfig);




        findViewById(R.id.imgCapture).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File file = new File(Environment.getExternalStorageDirectory() + "/" + System.currentTimeMillis() + ".png");



                imgCap.takePicture(file, new ImageCapture.OnImageSavedListener() {
                    @Override
                    public void onImageSaved(@NonNull File file) {
                        String msg = "Pic captured at " + file.getAbsolutePath();
                        Toast.makeText(getBaseContext(), msg,Toast.LENGTH_LONG).show();

                       // InputImage image = InputImage.fromBitmap(mSelectedImage, 0);
                        runTextRecognition();

                    }

                    @Override
                    public void onError(@NonNull ImageCapture.UseCaseError useCaseError, @NonNull String message, @Nullable Throwable cause) {
                        String msg = "Pic capture failed : " + message;
                        Toast.makeText(getBaseContext(), msg,Toast.LENGTH_LONG).show();
                        if(cause != null){
                            cause.printStackTrace();
                            Log.e("TAG", "onError: Error");
                        }
                    }
                });

                imgCap.takePicture(new ImageCapture.OnImageCapturedListener() {
                    @Override
                    public void onCaptureSuccess(ImageProxy image, int rotationDegrees) {
                        super.onCaptureSuccess(image, rotationDegrees);
                    }

                    @Override
                    public void onError(ImageCapture.UseCaseError useCaseError, String message, @Nullable Throwable cause) {
                        super.onError(useCaseError, message, cause);
                    }
                });
            }
        });



        //bind to lifecycle:
        CameraX.bindToLifecycle((LifecycleOwner)this, preview, imgCap);
    }

    private void startTextRecognizinngRunnable() {
        imageBmp = textureView.getBitmap();




        handler = new Handler();
        r = new Runnable() {
            @Override
            public void run() {
                Log.e("TAG", "run: Count = "+ sdf);
                sdf = sdf+1;
                imageBmp = textureView.getBitmap();
                runTextRecognition();
                isTextRecognizingRunnableRuunning = true;
                handler.postDelayed(this, 2000);
            }
        };
        handler.postDelayed(r, 2000);

    }

    private void updateTransform(){
        Matrix mx = new Matrix();
        float w = textureView.getMeasuredWidth();
        float h = textureView.getMeasuredHeight();

        float cX = w / 2f;
        float cY = h / 2f;

        int rotationDgr;
        int rotation = (int)textureView.getRotation();

        switch(rotation){
            case Surface.ROTATION_0:
                rotationDgr = 0;
                break;
            case Surface.ROTATION_90:
                rotationDgr = 90;
                break;
            case Surface.ROTATION_180:
                rotationDgr = 180;
                break;
            case Surface.ROTATION_270:
                rotationDgr = 270;
                break;
            default:
                return;
        }

        mx.postRotate((float)rotationDgr, cX, cY);
        textureView.setTransform(mx);
    }


    private Bitmap getBitmap(ImageProxy image) {
        ByteBuffer buffer = image.getPlanes()[0].getBuffer();
        buffer.rewind();
        byte[] bytes = new byte[buffer.capacity()];
        buffer.get(bytes);
        byte[] clonedBytes = bytes.clone();
        return BitmapFactory.decodeByteArray(clonedBytes, 0, clonedBytes.length);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(requestCode == REQUEST_CODE_PERMISSIONS){
            if(allPermissionsGranted()){
                startCamera();
            } else{
                Toast.makeText(this, "Permissions not granted by the user.", Toast.LENGTH_SHORT).show();
               // startCamera();

            }
        }
    }

    private boolean allPermissionsGranted(){

        for(String permission : REQUIRED_PERMISSIONS){
            if(ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED){
                return false;
            }
        }
        return true;
    }



    private class LabelAnalyzer implements ImageAnalysis.Analyzer {

        TextView textView;
        private float lastAnalyzedTimestamp = 0L;


        LabelAnalyzer(TextView textView) {
            this.textView = textView;
        }

        @Override
        public void analyze(ImageProxy imageProxy, int rotationDegrees) {
            Image mediaImage = imageProxy.getImage();
            if (mediaImage != null) {

                // Pass image to an ML Kit Vision API
                // ...
            }
        }
    }

    private void runTextRecognition() {
        InputImage image = InputImage.fromBitmap(imageBmp, 0);
       recognizer = TextRecognition.getClient();
        recognizer.process(image)
                .addOnSuccessListener(
                        new OnSuccessListener<Text>() {
                            @Override
                            public void onSuccess(Text texts) {
                                processTextRecognitionResult(texts);
                            }
                        })
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Task failed with an exception

                                e.printStackTrace();
                            }
                        });
    }

    private void processTextRecognitionResult(Text texts) {
        List<Text.TextBlock> blocks = texts.getTextBlocks();
        if (blocks.size() == 0) {
            Toast.makeText(this, "No text found", Toast.LENGTH_LONG).show();
            return;
        }

        for (int i = 0; i < blocks.size(); i++) {
            List<Text.Line> lines = blocks.get(i).getLines();
            for (int j = 0; j < lines.size(); j++) {
                List<Text.Element> elements = lines.get(j).getElements();
                for (int k = 0; k < elements.size(); k++) {
                    Log.e("TAG", "processTextRecognitionResult: "+ elements.get(k).getText());


                    String emailRegex ="^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";


                    String extractedWord = elements.get(k).getText();
                    if (extractedWord.matches(emailRegex)) {
                        Email email = new Email(extractedWord , extractedWord);

                       if (!emailExistCheck(extractedWord)) {
                            emailList.add(0,email);
                            emailAdapter.notifyDataSetChanged();
                        }
                    }

                }
            }
        }
    }

    private  boolean emailExistCheck(String extractedWord) {

        for (int i=0; i<emailList.size(); i++) {
            if (emailList.get(i).getEmailAddress().equalsIgnoreCase(extractedWord)) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (isTextRecognizingRunnableRuunning) {
            handler.removeCallbacks(r);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isTextRecognizingRunnableRuunning != null) {
            if (isTextRecognizingRunnableRuunning == false) {
                handler.postDelayed(r, 2000);
            }
        }
    }
}