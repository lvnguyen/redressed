package com.redressed.ui;

/**
 * Created by linhnguyen on 4/20/15.
 */

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseImageView;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.redressed.R;

/*
 * This fragment manages the data entry for a
 * new Meal object. It lets the user input a
 * meal name, give it a rating, and take a
 * photo. If there is already a photo associated
 * with this meal, it will be displayed in the
 * preview at the bottom, which is a standalone
 * ParseImageView.
 */
public class NewClothesFragment extends Fragment {

    private ImageButton photoButton;
    private Button saveButton;
    private Button cancelButton;

    private TextView clothesName;
    private TextView clothesSize;
    private TextView clothesBrand;

    private ParseImageView clothesPreview;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent,
                             Bundle SavedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_new_clothes, parent, false);

        // TODO: validate input
        clothesName = (EditText) v.findViewById(R.id.clothes_name);
        // What is size: small, medium, large, XL, L, ..
        clothesSize = (EditText) v.findViewById(R.id.clothes_size);
        clothesBrand = (EditText) v.findViewById(R.id.clothes_brand);

        photoButton = ((ImageButton) v.findViewById(R.id.photo_button));
        photoButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getActivity()
                        .getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(clothesName.getWindowToken(), 0);
                imm.hideSoftInputFromWindow(clothesBrand.getWindowToken(), 0);
                imm.hideSoftInputFromWindow(clothesSize.getWindowToken(), 0);
                startCamera();
            }
        });

        saveButton = ((Button) v.findViewById(R.id.save_button));
        saveButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Clothes clothes = ((NewClothesActivity) getActivity()).getCurrentClothes();

                // When the user clicks "Save," upload the clothes to Parse
                // Add data to the clothes object:
                clothes.setTitle(clothesName.getText().toString());
                clothes.setSize( clothesSize.getText().toString() );
                clothes.setBrand( clothesBrand.getText().toString() );

                // Associate the clothes with the current user
                clothes.setAuthor(ParseUser.getCurrentUser());

                // If the user added a photo, that data will be
                // added in the CameraFragment

                // Save the clothes and return
                clothes.saveInBackground(new SaveCallback() {

                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            getActivity().setResult(Activity.RESULT_OK);
                            getActivity().finish();
                        } else {
                            Toast.makeText(
                                    getActivity().getApplicationContext(),
                                    "Error saving: " + e.getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }

                });

            }
        });

        cancelButton = ((Button) v.findViewById(R.id.cancel_button));
        cancelButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                getActivity().setResult(Activity.RESULT_CANCELED);
                getActivity().finish();
            }
        });

        // Until the user has taken a photo, hide the preview
        clothesPreview = (ParseImageView) v.findViewById(R.id.clothes_preview_image);
        clothesPreview.setVisibility(View.INVISIBLE);

        return v;
    }

    /*
     * All data entry about a Meal object is managed from the NewMealActivity.
     * When the user wants to add a photo, we'll start up a custom
     * CameraFragment that will let them take the photo and save it to the Meal
     * object owned by the NewMealActivity. Create a new CameraFragment, swap
     * the contents of the fragmentContainer (see activity_new_meal.xml), then
     * add the NewMealFragment to the back stack so we can return to it when the
     * camera is finished.
     */
    public void startCamera() {
        Fragment cameraFragment = new CameraFragment();
        FragmentTransaction transaction = getActivity().getFragmentManager()
                .beginTransaction();
        transaction.replace(R.id.fragmentContainer, cameraFragment);
        transaction.addToBackStack("NewClothesFragment");
        transaction.commit();
    }

    /*
     * On resume, check and see if a meal photo has been set from the
     * CameraFragment. If it has, load the image in this fragment and make the
     * preview image visible.
     */
    @Override
    public void onResume() {
        super.onResume();
        ParseFile photoFile = ((NewClothesActivity) getActivity())
                .getCurrentClothes().getPhotoFile();
        if (photoFile != null) {
            clothesPreview.setParseFile(photoFile);
            clothesPreview.loadInBackground(new GetDataCallback() {
                @Override
                public void done(byte[] data, ParseException e) {
                    clothesPreview.setVisibility(View.VISIBLE);
                }
            });
        }
    }
}