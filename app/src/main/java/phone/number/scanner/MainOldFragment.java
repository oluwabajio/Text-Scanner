package phone.number.scanner;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.text.Line;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

import java.io.IOException;
import java.util.List;

public class MainOldFragment extends Fragment {

    private static final String TAG = "MainFragment";
    CameraSource mCameraSource;
    SurfaceView mCameraView;
TextView mTextView;
    int requestPermissionID = 555;
    SparseArray<TextBlock> items;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main_old, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.btn_click).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                NavHostFragment.findNavController(MainFragment.this)
//                        .navigate(R.id.action_FirstFragment_to_SecondFragment);

                if (items.size() != 0 ){

                    for(int i=0;i<items.size();i++){
                        TextBlock item = items.valueAt(i);
                        Log.e(TAG, "onClick: "+"text block value is " +item.getValue());
                        List<Line> lines = (List<Line>) item.getComponents();
                        for (Line line: lines) {
                            if (line.getValue().matches(".*\\d.*")) {
                                mTextView.setText(line.getValue());
                                Log.e(TAG, "onClick: "+"line value is " +line.getValue());
                            } else {
                       //         mTextView.setText("nnot found");
                            }
                        }
                    }

                }
            }
        });

        mTextView = view.findViewById(R.id.text_view);
        mCameraView = view.findViewById(R.id.surfaceView);

        startCameraSource();

    }



    private void startCameraSource() {

        //Create the TextRecognizer
        final TextRecognizer textRecognizer = new TextRecognizer.Builder(getActivity()).build();

        if (!textRecognizer.isOperational()) {
            Log.w(TAG, "Detector dependencies not loaded yet");
        } else {

            //Initialize camerasource to use high resolution and set Autofocus on.
            mCameraSource = new CameraSource.Builder(getActivity().getApplicationContext(), textRecognizer)
                    .setFacing(CameraSource.CAMERA_FACING_BACK)
                    .setRequestedPreviewSize(1280, 1024)
                    .setAutoFocusEnabled(true)
                    .setRequestedFps(2.0f)
                    .build();

            /**
             * Add call back to SurfaceView and check if camera permission is granted.
             * If permission is granted we can start our cameraSource and pass it to surfaceView
             */
            mCameraView.getHolder().addCallback(new SurfaceHolder.Callback() {
                @Override
                public void surfaceCreated(SurfaceHolder holder) {
                    try {

                        if (ActivityCompat.checkSelfPermission(getActivity(),
                                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {


                            ActivityCompat.requestPermissions(getActivity(),
                                    new String[]{Manifest.permission.CAMERA},
                                    requestPermissionID);
                            return;
                        }
                        mCameraSource.start(mCameraView.getHolder());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                }

                /**
                 * Release resources for cameraSource
                 */
                @Override
                public void surfaceDestroyed(SurfaceHolder holder) {
                    mCameraSource.stop();
                }
            });

            //Set the TextRecognizer's Processor.
            textRecognizer.setProcessor(new Detector.Processor<TextBlock>() {
                @Override
                public void release() {
                }

                /**
                 * Detect all the text from camera using TextBlock and the values into a stringBuilder
                 * which will then be set to the textView.
                 * */
                @Override
                public void receiveDetections(Detector.Detections<TextBlock> detections) {
                   items = detections.getDetectedItems();



//                    for (int i = 0; i < items.size(); ++i) {
//                        TextBlock item = items.valueAt(i);
//                        List<Line> lines = (List<Line>) item.getComponents();
//                        for (Line line : lines) {
//                            List<Element> elements = (List<Element>) line.getComponents();
//                            for (Element element : elements) {
//                                String word = element.getValue();
//                            }
//                        }
//                    }


                }
            });
        }
    }
}