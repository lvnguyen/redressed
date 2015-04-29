package com.taptag.ui;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

/**
 * Created by linhnguyen on 4/20/15.
 */
@ParseClassName("Clothes")
public class Clothes extends ParseObject {

    public Clothes() {
        // A default constructor is required.
    }

    public String getTitle() {
        return getString("title");
    }

    public void setTitle(String title) {
        put("title", title);
    }

    public String getSize() { return getString("size"); }

    public void setSize(String size) { put("size", size); }

    public String getBrand() { return getString("brand"); }

    public void setBrand(String brand) { put("brand", brand); }

    public ParseUser getAuthor() {
        return getParseUser("username");
    }

    public void setAuthor(ParseUser user) {
        put("username", user);
    }

    public ParseFile getPhotoFile() {
        return getParseFile("photo");
    }

    public void setPhotoFile(ParseFile file) {
        put("photo", file);
    }

}