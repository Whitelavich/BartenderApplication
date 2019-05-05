package com.example.bartender;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class DrawableFromUrl extends AsyncTask<String, Drawable, Drawable> {
    String url;
    private Drawable d;

    protected void onPreExecute() {

    }

    @Override
    protected Drawable doInBackground(String... url) {
        try {
            d = Drawable.createFromStream(
                    ((InputStream) new URL(url[0]).getContent()), "TheCocktailDB");
        } catch (IOException e) {
            e.printStackTrace();
        }

        return d;
    }

    protected void onPostExecute(Drawable response) {

    }
}

