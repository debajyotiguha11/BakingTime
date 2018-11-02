package debjyoti.example.com.bakingtime;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ScrollView;

import java.util.ArrayList;

import debjyoti.example.com.bakingtime.extras.Extras;
import debjyoti.example.com.bakingtime.extras.Ingredient;
import debjyoti.example.com.bakingtime.extras.Recipe;
import debjyoti.example.com.bakingtime.extras.Step;


public class RecipeDetailActivity extends AppCompatActivity implements RecipeDetailFragment.StepFragmentListener {


    private static final String TAG = RecipeDetailActivity.class.getSimpleName();
    private Recipe recipe;
    private RecipeDetailFragment recipeDetailFragment;
    private StepDetailFragment stepDetailFragment;
    private ScrollView mScrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);
        recipe = getIntent().getParcelableExtra(Extras.RECIPE);
        ArrayList<Ingredient> ingredientArrayList = recipe.getIngredientsArrayList();
        ArrayList<Step> stepsArrayList = recipe.getStepsArrayList();
        mScrollView = findViewById(R.id.scroll_view_id);


        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(Extras.INGREDIENTS, ingredientArrayList);
        bundle.putParcelableArrayList(Extras.STEPS, stepsArrayList);
        recipeDetailFragment = new RecipeDetailFragment();
        recipeDetailFragment.setArguments(bundle);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.recipe_detail_container, recipeDetailFragment).commit();

        if (findViewById(R.id.step_detail_container) != null) {
            stepDetailFragment = new StepDetailFragment();
            Bundle bundleStep = new Bundle();
            bundleStep.putString(Extras.VIDEO_URL, stepsArrayList.get(0).getVideoUrl());
            bundleStep.putString(Extras.STEP_DESCRIPTION, stepsArrayList.get(0).getDescription());
            bundleStep.putInt(Extras.STEP_INDEX, stepsArrayList.get(0).getStep_id());
            bundleStep.putParcelableArrayList(Extras.STEPS, stepsArrayList);
            stepDetailFragment.setArguments(bundleStep);


            getSupportFragmentManager().beginTransaction()
                    .add(R.id.step_detail_container, stepDetailFragment).commit();
        }
    }

    @Override
    public void onStepFragmentListener(Bundle bundle) {

        if (findViewById(R.id.step_detail_container) == null) {
            Intent intent = new Intent(this, StepDetailActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);

        } else {

            stepDetailFragment = new StepDetailFragment();
            stepDetailFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().
                    replace(R.id.step_detail_container, stepDetailFragment).commit();
        }


    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putIntArray("ARTICLE_SCROLL_POSITION",
                new int[]{mScrollView.getScrollX(), mScrollView.getScrollY()});
    }

    @Override
    public void onRestoreInstanceState(@Nullable Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        final int[] position = savedInstanceState.getIntArray("ARTICLE_SCROLL_POSITION");
        if (position != null)
            mScrollView.post(new Runnable() {
                public void run() {
                    mScrollView.scrollTo(position[0], position[1]);
                }
            });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
