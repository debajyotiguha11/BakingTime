package debjyoti.example.com.bakingtime;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.test.espresso.idling.CountingIdlingResource;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import debjyoti.example.com.bakingtime.data.RecipesAdapter;
import debjyoti.example.com.bakingtime.extras.ConnectionDetector;
import debjyoti.example.com.bakingtime.extras.Extras;
import debjyoti.example.com.bakingtime.extras.Ingredient;
import debjyoti.example.com.bakingtime.extras.Recipe;
import debjyoti.example.com.bakingtime.extras.Step;


public class RecipesActivity extends AppCompatActivity implements RecipesAdapter.RecipeListOnClickListener {

    private static final String TAG = RecipesActivity.class.getSimpleName();
    RecyclerView recipeListRecyclerView;
    CountingIdlingResource countingIdlingResource = new CountingIdlingResource("Check recipe list");
    private ConnectionDetector cd;
    private LinearLayout noConnection;
    private boolean isInternetPresent;
    private ArrayList<Recipe> recipeArrayList;
    private RecipesAdapter recipeListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes);
        cd = new ConnectionDetector(this);
        isInternetPresent = cd.isConnectingToInternet();
        noConnection = findViewById(R.id.linearLayout_no_connection);
        recipeListRecyclerView = findViewById(R.id.rv_recipe_list);

        recipeArrayList = new ArrayList<>();

        recipeListAdapter = new RecipesAdapter(recipeArrayList, this);

        boolean isTablet = (getBaseContext().getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
        if (isTablet) {
            GridLayoutManager mLayoutManager = new GridLayoutManager(getBaseContext(), 3);
            recipeListRecyclerView.setLayoutManager(mLayoutManager);
        } else {
            LinearLayoutManager mLayoutManager = new LinearLayoutManager(getBaseContext());
            recipeListRecyclerView.setLayoutManager(mLayoutManager);
        }

        recipeListRecyclerView.setAdapter(recipeListAdapter);


        //Accessing Network
        RequestQueue requestQueue = Volley.newRequestQueue(RecipesActivity.this);
        String url = getResources().getString(R.string.url);
        JsonArrayRequest jsonArrayRequest = getJsonArray(url);
        if (isInternetPresent) {
            countingIdlingResource.increment();
            Log.v(TAG, "started idling resource");
            requestQueue.add(jsonArrayRequest);
            recipeListRecyclerView.setVisibility(View.VISIBLE);
            noConnection.setVisibility(View.INVISIBLE);
        } else {
            recipeListRecyclerView.setVisibility(View.GONE);
            noConnection.setVisibility(View.VISIBLE);
        }
    }

    private JsonArrayRequest getJsonArray(String url) {
        return new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                recipeArrayList = parseRecipeJson(response);
                recipeListAdapter = new RecipesAdapter(recipeArrayList, RecipesActivity.this);
                recipeListRecyclerView.setAdapter(recipeListAdapter);
                recipeListRecyclerView.invalidate();
                countingIdlingResource.decrement();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast toast = Toast.makeText(RecipesActivity.this, "Check your Connection!", Toast.LENGTH_LONG);
                toast.show();
            }
        });
    }

    private ArrayList<Recipe> parseRecipeJson(JSONArray response) {
        ArrayList<Recipe> mRecipesList = new ArrayList<>();

        if (response != null) {
            try {
                int recipeId;
                String name;
                ArrayList<Ingredient> ingredients;
                ArrayList<Step> steps;
                String image;
                int servings;

                for (int i = 0; i < response.length(); i++) {
                    JSONObject obj = response.getJSONObject(i);
                    recipeId = obj.getInt("id");
                    name = obj.getString("name");
                    ingredients = new ArrayList<>();
                    steps = new ArrayList<>();
                    JSONArray ingredientsJsonArray = obj.getJSONArray("ingredients");
                    JSONArray stepsJsonArray = obj.getJSONArray("steps");
                    image = obj.getString("image");
                    servings = obj.getInt("servings");


                    for (int j = 0; j < ingredientsJsonArray.length(); j++) {
                        JSONObject ingredientsObject = ingredientsJsonArray.getJSONObject(j);
                        double quantity = ingredientsObject.getDouble("quantity");
                        String measure = ingredientsObject.getString("measure");
                        String ingredient = ingredientsObject.getString("ingredient");
                        ingredients.add(new Ingredient(quantity, measure, ingredient));
                    }

                    for (int j = 0; j < stepsJsonArray.length(); j++) {
                        JSONObject stepsJsonObject = stepsJsonArray.getJSONObject(j);
                        int stepId = stepsJsonObject.getInt("id");
                        String shortDescription = stepsJsonObject.getString("shortDescription");
                        String description = stepsJsonObject.getString("description");
                        String videoUrl = stepsJsonObject.getString("videoURL");
                        String thumbnailUrl = stepsJsonObject.getString("thumbnailURL");
                        steps.add(new Step(stepId, shortDescription, description, videoUrl, thumbnailUrl));
                    }

                    mRecipesList.add(new Recipe(recipeId, name, ingredients, steps, image, servings));


                    // sendBroadcast(new Intent().setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE));
                }
            } catch (JSONException e) {
                e.printStackTrace();

            }
        } else {
            Log.e(TAG, "Empty Json response !");
            Toast toast = Toast.makeText(RecipesActivity.this, "Check your Connection!", Toast.LENGTH_LONG);
            toast.show();
        }
        return mRecipesList;
    }

    @Override
    public void onRecipeItemClicked(Recipe recipe) {
        Intent intent = new Intent(RecipesActivity.this, RecipeDetailActivity.class);
        intent.putExtra(Extras.RECIPE, recipe);
        startActivity(intent);
        Log.e("Error ", "error in implementing the method of the interface that handles clicks");
    }
}
