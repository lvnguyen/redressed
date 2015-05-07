package com.taptag.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.taptag.R;
import com.taptag.custom.CustomFragment;

import java.util.ArrayList;
import java.util.List;

// TODO: Auto-generated Javadoc
/**
 * The Class Profile is the Fragment class that is launched when the user clicks
 * on Profile button in Left navigation drawer.
 */
public class Profile extends CustomFragment
{
    private List<ParseObject> allItems;

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		View v = inflater.inflate(R.layout.profile, null);
        ParseUser myUser = ParseUser.getCurrentUser();
        String myUsername = (String) myUser.get("name");
        TextView userNameText = (TextView) v.findViewById(R.id.user_name);
        userNameText.setText(myUsername);

        String myEmailAddress = myUser.getEmail();
        TextView emailText = (TextView) v.findViewById(R.id.email_addr);
        emailText.setText(myEmailAddress);

        TextView newPhotoButton = (TextView) v.findViewById(R.id.new_clothes);
        newPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(), NewClothesActivity.class);
                startActivityForResult(i, 0); // TODO: what error code we set here?
            }
        });

		setTouchNClick(v.findViewById(R.id.p1));
        allItems = new ArrayList<ParseObject>();
        final GridAdapter gridAdapter = new GridAdapter(allItems);

        GridView grid = (GridView) v.findViewById(R.id.grid);
        grid.setAdapter(gridAdapter);

        final TextView photoCountText = (TextView) v.findViewById(R.id.photo_count);

        /**
         * asynchronous
         */
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Clothes");
        query.whereEqualTo("username", ParseUser.getCurrentUser());

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                if (e == null) {
                    for (ParseObject object : parseObjects) {
                        allItems.add(object);
                    }
                    if (allItems.size() == 1) {
                        photoCountText.setText("1 Photo");
                    }
                    else {
                        photoCountText.setText(allItems.size() + " Photos");
                    }
                    gridAdapter.notifyDataSetChanged();
                }
                else {
                    // Log error here
                }
            }
        });

		return v;
	}

	/* (non-Javadoc)
	 * @see com.taptag.custom.CustomFragment#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v)
	{
		super.onClick(v);
		if (v.getId() == R.id.p3)
			getFragmentManager().beginTransaction()
					.replace(R.id.content_frame, new MainFragment())
					.addToBackStack("Elements").commit();
	}

	/**
	 * The Class GridAdapter is the adpater for displaying Products in GridView.
	 * The current implementation simply display dummy product images. You need
	 * to change it as per your needs.
	 */
	private class GridAdapter extends BaseAdapter
	{
        private List<ParseObject> allItems;

        public GridAdapter(List<ParseObject> allItems) {
            this.allItems = allItems;
        }

		/* (non-Javadoc)
		 * @see android.widget.Adapter#getCount()
		 */
		@Override
		public int getCount()
		{
			return allItems.size();
		}

		/* (non-Javadoc)
		 * @see android.widget.Adapter#getItem(int)
		 */
		@Override
		public Object getItem(int arg0)
		{
			return null;
		}

		/* (non-Javadoc)
		 * @see android.widget.Adapter#getItemId(int)
		 */
		@Override
		public long getItemId(int pos)
		{
			return pos;
		}

		/* (non-Javadoc)
		 * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
		 */
		@Override
		public View getView(int pos, View v, ViewGroup arg2)
		{
            // Do not load view when pos is out of range
			if (pos < 0 || pos >= allItems.size()) {
                return null;
            }

            if (v == null)
				v = LayoutInflater.from(getActivity()).inflate(
						R.layout.profile_item, null);

            final ImageView img = (ImageView) v.findViewById(R.id.img);
            final ParseObject imgObject = allItems.get(pos);

            // fetching photo, title, size and brand
            final ParseFile remoteFile = (ParseFile) imgObject.get("photo");

            // Bug occurred when the corresponding item received from the server is null
            // Temporary fix: if it is null, return
            if (remoteFile == null) {
                return null;
            }

            remoteFile.getDataInBackground(new GetDataCallback() {
                @Override
                public void done(byte[] bytes, ParseException e) {
                    if (e == null) {
                        Bitmap bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        img.setImageBitmap(bm);
                    }
                }
            });

            img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), LargeClothesActivity.class);
                    Bundle b = new Bundle();
                    b.putString("file", remoteFile.getUrl());

                    ParseUser user = (ParseUser) imgObject.get("username");
                    b.putString("user", (String) user.get("name"));

                    intent.putExtras(b);
                    startActivity(intent);
                }
            });

            String clothesTitle = (String) imgObject.get("title");
            TextView clothesTitleView = (TextView) v.findViewById(R.id.lbl);
            clothesTitleView.setText(clothesTitle);

            String clothesBrand = (String) imgObject.get("brand");
            TextView clothesBrandView = (TextView) v.findViewById(R.id.lbl1);
            clothesBrandView.setText(clothesBrand);

            String clothesSize = (String) imgObject.get("size");
            TextView clothesSizeView = (TextView) v.findViewById(R.id.lbl2);
            clothesSizeView.setText(clothesSize);

            return v;
		}
	}
}