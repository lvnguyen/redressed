package com.redressed.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.redressed.R;
import com.redressed.custom.CustomFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * The Class Search is the Fragment class that is launched when the user
 * clicks on Search button in Left navigation drawer.
 */
public class Search extends CustomFragment
{
    private List<ParseObject> allItems;

    /* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		final View v = inflater.inflate(R.layout.search, null);
        final Button searchButton = (Button) v.findViewById(R.id.start_search_button);

        allItems = new ArrayList<ParseObject>();
        final GridAdapter gridAdapter = new GridAdapter(allItems);

        GridView grid = (GridView) v.findViewById(R.id.grid);
        grid.setAdapter(gridAdapter);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Clear all items before searching
                allItems.clear();

                TextView searchNameView = (EditText) v.findViewById(R.id.search_name);
                String searchName = (String) searchNameView.getText().toString();
                if (searchName.isEmpty()) {
                    Toast.makeText(v.getContext(), "You must enter your search",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                /**
                 * Construct the query with name, size and brand
                 */
                TextView searchSizeView = (EditText) v.findViewById(R.id.search_size);
                String searchSize = (String) searchSizeView.getText().toString();

                TextView searchBrandView = (EditText) v.findViewById(R.id.search_brand);
                String searchBrand = (String) searchBrandView.getText().toString();

                ParseQuery<ParseObject> query = ParseQuery.getQuery("Clothes");
                query.whereEqualTo("title", searchName);
                if (!searchSize.isEmpty()) {
                    query.whereEqualTo("size", searchSize);
                }
                if (!searchBrand.isEmpty()) {
                    query.whereEqualTo("brand", searchBrand);
                }

                /**
                 * Query returned, notify the grid adapter
                 */
                query.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> parseObjects, ParseException e) {
                        for (ParseObject obj : parseObjects) {
                            allItems.add(obj);
                        }
                        if (allItems.isEmpty()) {
                            Toast.makeText(getActivity(), "No result found.", Toast.LENGTH_SHORT).show();
                        }
                        gridAdapter.notifyDataSetChanged();
                    }
                });
            }
        });

		setTouchNClick(v.findViewById(R.id.p1));
		setTouchNClick(v.findViewById(R.id.p2));
		setTouchNClick(v.findViewById(R.id.p3));
		return v;
	}

    /**
     * TODO: avoid copy/paste grid adapter around like this
     */

    /**
     * The Class GridAdapter is the adapter for displaying Products in GridView.
     * The current implementation simply display dummy product images. You need
     * to change it as per your needs.
     */
    private class GridAdapter extends BaseAdapter
    {

        private List<ParseObject> allItems;

        public GridAdapter(List<ParseObject> allItems) {this.allItems = allItems; }

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
        public Object getItem(int pos)
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
            // Only load the picture when pos is inside the range
            if (pos < 0 || pos >= allItems.size()) {
                return v;
            }

            if (v == null)
                v = LayoutInflater.from(getActivity()).inflate(
                        R.layout.profile_item, null);

            final ImageView img = (ImageView) v.findViewById(R.id.img);
            final ParseObject imgObject = allItems.get(pos);
            final ParseFile remoteFile = (ParseFile) imgObject.get("photo");

            if (remoteFile == null) {
                return v;
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