package com.example.bartender;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ActionProvider;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
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
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    static LinkedList<Drawable> images = new LinkedList<>();
    static LinkedList<String> names = new LinkedList<>();

    static DrinkListAdapter mAdapter = null;
    MenuItem current = null;

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
        this.current = item;
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

    public void setCurrent(MenuItem item) {
        if (item != null) {
            this.current = item;
        } else {
            MenuItem random = new MenuItem() {
                @Override
                public int getItemId() {
                    return 0;
                }

                @Override
                public int getGroupId() {
                    return 0;
                }

                @Override
                public int getOrder() {
                    return 0;
                }

                @Override
                public MenuItem setTitle(CharSequence charSequence) {
                    return null;
                }

                @Override
                public MenuItem setTitle(int i) {
                    return null;
                }

                @Override
                public CharSequence getTitle() {
                    return null;
                }

                @Override
                public MenuItem setTitleCondensed(CharSequence charSequence) {
                    return null;
                }

                @Override
                public CharSequence getTitleCondensed() {
                    return null;
                }

                @Override
                public MenuItem setIcon(Drawable drawable) {
                    return null;
                }

                @Override
                public MenuItem setIcon(int i) {
                    return null;
                }

                @Override
                public Drawable getIcon() {
                    return null;
                }

                @Override
                public MenuItem setIntent(Intent intent) {
                    return null;
                }

                @Override
                public Intent getIntent() {
                    return null;
                }

                @Override
                public MenuItem setShortcut(char c, char c1) {
                    return null;
                }

                @Override
                public MenuItem setNumericShortcut(char c) {
                    return null;
                }

                @Override
                public char getNumericShortcut() {
                    return 0;
                }

                @Override
                public MenuItem setAlphabeticShortcut(char c) {
                    return null;
                }

                @Override
                public char getAlphabeticShortcut() {
                    return 0;
                }

                @Override
                public MenuItem setCheckable(boolean b) {
                    return null;
                }

                @Override
                public boolean isCheckable() {
                    return false;
                }

                @Override
                public MenuItem setChecked(boolean b) {
                    return null;
                }

                @Override
                public boolean isChecked() {
                    return false;
                }

                @Override
                public MenuItem setVisible(boolean b) {
                    return null;
                }

                @Override
                public boolean isVisible() {
                    return false;
                }

                @Override
                public MenuItem setEnabled(boolean b) {
                    return null;
                }

                @Override
                public boolean isEnabled() {
                    return false;
                }

                @Override
                public boolean hasSubMenu() {
                    return false;
                }

                @Override
                public SubMenu getSubMenu() {
                    return null;
                }

                @Override
                public MenuItem setOnMenuItemClickListener(OnMenuItemClickListener onMenuItemClickListener) {
                    return null;
                }

                @Override
                public ContextMenu.ContextMenuInfo getMenuInfo() {
                    return null;
                }

                @Override
                public void setShowAsAction(int i) {

                }

                @Override
                public MenuItem setShowAsActionFlags(int i) {
                    return null;
                }

                @Override
                public MenuItem setActionView(View view) {
                    return null;
                }

                @Override
                public MenuItem setActionView(int i) {
                    return null;
                }

                @Override
                public View getActionView() {
                    return null;
                }

                @Override
                public MenuItem setActionProvider(ActionProvider actionProvider) {
                    return null;
                }

                @Override
                public ActionProvider getActionProvider() {
                    return null;
                }

                @Override
                public boolean expandActionView() {
                    return false;
                }

                @Override
                public boolean collapseActionView() {
                    return false;
                }

                @Override
                public boolean isActionViewExpanded() {
                    return false;
                }

                @Override
                public MenuItem setOnActionExpandListener(OnActionExpandListener onActionExpandListener) {
                    return null;
                }
            };
            random.setTitle("Random");
            this.current = random;
        }
    }

    public void refresh(View view) {
        if (this.current != null) {
            if (this.current.getTitle() == "Random") {
                this.listRandom(this.current);
            } else {
                this.list(this.current);
            }
        } else {
            this.listRandom(null);
        }


    }

    static class DrawableFromUrl extends AsyncTask<String, Void, Drawable> {

        Drawable d;
        String name;

        protected void onPreExecute() {

        }

        @Override
        protected Drawable doInBackground(String... url) {
            try {
                name = url[1];
                d = Drawable.createFromStream(
                        ((InputStream) new URL(url[0]).getContent()), "TheCocktailDB");

                Bitmap x;
                HttpURLConnection connection = (HttpURLConnection) new URL(url[0]).openConnection();
                connection.connect();
                InputStream input = connection.getInputStream();

                x = BitmapFactory.decodeStream(input);
                return new BitmapDrawable(Resources.getSystem(), x);


            } catch (IOException e) {
                e.printStackTrace();
            }

            return d;
        }

        protected void onPostExecute(Drawable response) {

            images.add(response);
            names.add(name);
            mAdapter.notifyDataSetChanged();
            Log.d("#####", "onPostExecute: gotty img");
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


                    new DrawableFromUrl().execute(c.getString("strDrinkThumb"), c.getString("strDrink"));
                    Log.d("#####", c.toString());


                    c = null;

                }


            } catch (JSONException e) {
                e.printStackTrace();
            }

        }


    }

}
