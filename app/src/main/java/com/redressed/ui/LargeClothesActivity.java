package com.redressed.ui;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;

import com.redressed.R;

/**
 * Created by linhnguyen on 4/28/15.
 */
public class LargeClothesActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        Bundle b = getIntent().getExtras();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_clothes);
        FragmentManager manager = getFragmentManager();
        Fragment fragment = manager.findFragmentById(R.id.fragmentContainer);

        if (fragment == null) {
            fragment = new LargeClothesFragment();
            fragment.setArguments(b);
            manager.beginTransaction().add(R.id.fragmentContainer, fragment)
                    .commit();
        }
    }

}
