package com.redressed.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.redressed.R;
import com.redressed.custom.CustomFragment;

/**
 * Created by linhnguyen on 4/18/15.
 */
public class OldCameraClass extends CustomFragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.camera, null);

        Button camera = (Button) v.findViewById(R.id.camera_button);
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), CameraActivity.class);
                startActivity(intent);
            }
        });
        Button fb = (Button) v.findViewById(R.id.fb_button);
        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "This feature is under development", Toast.LENGTH_SHORT).show();
            }
        });
        return v;
    }
}