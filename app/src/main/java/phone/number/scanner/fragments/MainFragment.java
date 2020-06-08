package phone.number.scanner.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import phone.number.scanner.R;
import phone.number.scanner.activities.ScannerActivity;
import phone.number.scanner.utils.SharedPrefManager;

import static phone.number.scanner.utils.AppUtil.getSharedPrefInstance;


public class MainFragment extends Fragment {




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        iitViews(view);
        initListeners(view);


        SharedPrefManager sharedPrefManager = getSharedPrefInstance();

        sharedPrefManager.setName("gh");

        SharedPrefManager sharedPrefManager2 = getSharedPrefInstance();
        Toast.makeText(getActivity(), ""+ sharedPrefManager2.getName(), Toast.LENGTH_LONG).show();
        return view;
    }

    private void initListeners(View view) {


        view.findViewById(R.id.ry_phone_number).setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ScannerActivity.class);
            intent.putExtra("task", "phone_number");
            startActivity(intent);
        });

        view.findViewById(R.id.ry_email_address).setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ScannerActivity.class);
            intent.putExtra("task", "email");
            startActivity(intent);
        });

        view.findViewById(R.id.ry_website).setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ScannerActivity.class);
            intent.putExtra("task", "website");
            startActivity(intent);
        });

        view.findViewById(R.id.ry_bar_code).setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ScannerActivity.class);
            intent.putExtra("task", "bar_code");
            startActivity(intent);
        });

    }

    private void iitViews(View view) {
    }
}