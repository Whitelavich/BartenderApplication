package com.example.bartender;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedList;

public class MainActivity extends AppCompatActivity {

    static LinkedList<Drawable> images = new LinkedList<>();
    LinkedList<String> names = new LinkedList<>();

    static DrinkListAdapter mAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Intent intent = new Intent(MainActivity.this, DrinkInformation.class);
        for (int a = 0; a < 50; a++) {
            new DbHandler().execute("https://www.thecocktaildb.com/api/json/v1/1/random.php");

        }


        RecyclerView recyclerView = findViewById(R.id.recycler);
        mAdapter = new DrinkListAdapter(this, names, images);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.list_menu, menu);

        return true;
    }

    public void list(MenuItem item) {
        names.clear();
        images.clear();
        new DbHandler().execute("https://www.thecocktaildb.com/api/json/v1/1/filter.php?i=" + item.getTitle());

    }

    public void listRandom(MenuItem item) {
        names.clear();
        images.clear();
        for (int a = 0; a < 50; a++) {
            new DbHandler().execute("https://www.thecocktaildb.com/api/json/v1/1/random.php");

        }

    }


    class DbHandler extends AsyncTask<String, Void, String> {

        TextView txt;


        protected void onPreExecute() {
            txt = findViewById(R.id.textView);
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                URL url = new URL(strings[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                try {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line).append("\n");
                    }
                    bufferedReader.close();

                    return stringBuilder.toString();
                } finally {
                    urlConnection.disconnect();
                }
            } catch (Exception e) {
                Log.d("ERROR123", e.getMessage(), e);
                return null;
            }

        }

        protected void onPostExecute(String response) {
            if (response == null) {
                response = "Error";
            }


            JSONObject jsonObj = null;
            JSONArray drinks = null;

            try {
                jsonObj = new JSONObject(response);
                drinks = jsonObj.getJSONArray("drinks");
                for (int i = 0; i < drinks.length(); i++) {
                    JSONObject c = drinks.getJSONObject(i);
                    Drawable d = null;

                    names.add(c.getString("strDrink"));
                    new DrawableFromUrl().execute(c.getString("strDrinkThumb"));
                    Log.d("#####", c.get("strDrink") + " " + c.getString("strDrinkThumb"));


                    c = null;

                }
                mAdapter.notifyDataSetChanged();


            } catch (JSONException e) {
                e.printStackTrace();
            }

        }


    }

    static class DrawableFromUrl extends AsyncTask<String, Void, Drawable> {

        Drawable d;

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
            images.add(response);
            mAdapter.notifyDataSetChanged();
            Log.d("DOGGO", "onPostExecute: gotty img");
        }
    }

}
