package com.taptag.ui;

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
import com.taptag.R;
import com.taptag.custom.CustomFragment;

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
		View v = inflater.inflate(R.layout.search, null);
        final Button searchButton = (Button) v.findViewById(R.id.start_search_button);

        allItems = new ArrayList<ParseObject>();
        final GridAdapter gridAdapter = new GridAdapter(allItems);

        GridView grid = (GridView) v.findViewById(R.id.grid);
        grid.setAdapter(gridAdapter);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView searchNameView = (EditText) view.findViewById(R.id.search_name);
                String searchName = (String) searchNameView.getText().toString();
                if (searchName.isEmpty()) {
                    Toast.makeText(view.getContext(), "You must enter your search",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                /**
                 * Construct the query with name, size and brand
                 */
                TextView searchSizeView = (EditText) view.findViewById(R.id.search_size);
                String searchSize = (String) searchSizeView.getText().toString();

                TextView searchBrandView = (EditText) view.findViewById(R.id.search_brand);
                String searchBrand = (String) searchBrandView.getText().toString();

                ParseQuery<ParseObject> query = ParseQuery.getQuery("Clothes");
                query.whereEqualTo("name", searchName);
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
            if (v == null)
                v = LayoutInflater.from(getActivity()).inflate(
                        R.layout.profile_item, null);

            final ImageView img = (ImageView) v.findViewById(R.id.img);
            ParseObject imgObject = allItems.get(pos);
            ParseFile remoteFile = (ParseFile) imgObject.get("photo");
            remoteFile.getDataInBackground(new GetDataCallback() {
                @Override
                public void done(byte[] bytes, ParseException e) {
                    if (e == null) {
                        Bitmap bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        img.setImageBitmap(bm);
                    }
                }
            });

            return v;
        }

    }

}