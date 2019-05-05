package com.example.bartender;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
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

public class DrinkInformation extends AppCompatActivity {
    public String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drink_information);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        TextView nameView = findViewById(R.id.name);
        TextView instructions = findViewById(R.id.instructions);
        ImageView img = findViewById(R.id.imageView2);
        new DbHandler2(this).execute(name);


    }
}

class DbHandler2 extends AsyncTask<String, Void, String> {

    TextView txt;
    Activity mContext;

    public DbHandler2(Activity context) {
        mContext = context;
    }

    @Override
    protected String doInBackground(String... strings) {
        try {

            URL url = new URL("https://www.thecocktaildb.com/api/json/v1/1/search.php?s=" + strings[0]);
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

    protected void onPreExecute() {

    }


    protected void onPostExecute(String response) {
        if (response == null) {
            response = "Error";
            Log.d("EGG", "onPostExecute: oof");
        }


        JSONObject jsonObj;
        DrawableFromUrl2 drawableSrc = new DrawableFromUrl2();
        try {
            jsonObj = new JSONObject(response);
            JSONArray drinks = jsonObj.getJSONArray("drinks");
            JSONObject c = drinks.getJSONObject(0);
            TextView name = mContext.findViewById(R.id.name);
            name.setText(c.getString("strDrink"));
            String ingredients = c.getString("strMeasure1") + c.getString("strIngredient1") + " ";
            ingredients += c.getString("strMeasure2") + c.getString("strIngredient2") + " ";
            ingredients += c.getString("strMeasure3") + " " + c.getString("strIngredient3") + " ";
            ingredients += c.getString("strMeasure4") + " " + c.getString("strIngredient4") + " ";
            ingredients += c.getString("strMeasure5") + " " + c.getString("strIngredient5") + " ";
            ingredients += c.getString("strMeasure6") + " " + c.getString("strIngredient6") + " ";
            ingredients += c.getString("strMeasure7") + " " + c.getString("strIngredient7") + " ";
            ingredients += c.getString("strMeasure8") + " " + c.getString("strIngredient8") + " ";
            ingredients += c.getString("strMeasure9") + " " + c.getString("strIngredient9") + " ";
            ingredients += c.getString("strMeasure10") + " " + c.getString("strIngredient10") + " ";
            ingredients += c.getString("strMeasure11") + " " + c.getString("strIngredient11") + " ";
            ingredients += c.getString("strMeasure12") + " " + c.getString("strIngredient12") + " ";
            ingredients += c.getString("strMeasure13") + " " + c.getString("strIngredient13") + " ";
            ingredients += c.getString("strMeasure14") + " " + c.getString("strIngredient14") + " ";
            ingredients += c.getString("strMeasure15") + " " + c.getString("strIngredient15");
            ingredients = ingredients.replaceAll("nullnull", "");
            ingredients = ingredients.replaceAll("null", "");
            ingredients = ingredients.trim();

            TextView ingredientView = mContext.findViewById(R.id.ingredientsView);
            ingredientView.setText(ingredients);

            TextView instructions = mContext.findViewById(R.id.instructions);
            instructions.setText(c.getString("strInstructions"));


            drawableSrc.execute(c.getString("strDrinkThumb"));

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    class DrawableFromUrl2 extends AsyncTask<String, Void, Drawable> {

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

            Log.d("DOGGO", "onPostExecute: gotty img");
            ImageView img = mContext.findViewById(R.id.imageView2);
            img.setImageDrawable(response);
        }
    }

}


