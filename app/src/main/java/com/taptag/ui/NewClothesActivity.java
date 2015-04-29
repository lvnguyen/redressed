package com.taptag.ui;

/**
 * Created by linhnguyen on 4/20/15.
 */

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.taptag.R;

/*
 * NewClothesActivity contains two fragments that handle
 * data entry and capturing a photo of a given clothes.
 * The Activity manages the overall clothes data.
 */
public class NewClothesActivity extends Activity {

    private Clothes clothes;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        clothes = new Clothes();
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);

        // Begin with main data entry view,
        // NewClothesFragment
        setContentView(R.layout.activity_new_clothes);
        FragmentManager manager = getFragmentManager();
        Fragment fragment = manager.findFragmentById(R.id.fragmentContainer);

        if (fragment == null) {
            fragment = new NewClothesFragment();
            manager.beginTransaction().add(R.id.fragmentContainer, fragment)
                    .commit();
        }
    }

    public Clothes getCurrentClothes() {
        return clothes;
    }

}