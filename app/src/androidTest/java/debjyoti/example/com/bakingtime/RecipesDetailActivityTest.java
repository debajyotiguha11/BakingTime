package debjyoti.example.com.bakingtime;

import android.content.Intent;
import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;

import debjyoti.example.com.bakingtime.RecipeDetailActivity;
import debjyoti.example.com.bakingtime.extras.Extras;
import debjyoti.example.com.bakingtime.extras.Ingredient;
import debjyoti.example.com.bakingtime.extras.Recipe;
import debjyoti.example.com.bakingtime.extras.Step;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;


@RunWith(JUnit4.class)
public class RecipesDetailActivityTest {

    @Rule
    public ActivityTestRule<RecipeDetailActivity> mActivityTestRule = new ActivityTestRule<>(RecipeDetailActivity.class, true, false);

    @Before
    public void setmActivityTestRule() {
        ArrayList<Ingredient> ingredientArrayList = new ArrayList<>();
        ingredientArrayList.add(new Ingredient(1, "TBLSB", "Graham Cracker crumbs"));
        ArrayList<Step> stepsArrayList = new ArrayList<>();
        stepsArrayList.add(new Step(1, "Description", "Whisk graham Cracker", "", ""));
        Intent intent = new Intent();
        intent.putExtra(Extras.RECIPE, new Recipe(2, "Cup Cake", ingredientArrayList, stepsArrayList, "", 23));
        mActivityTestRule.launchActivity(intent);
    }

    @Test
    public void checkIngredientsTitle() {
        ViewInteraction mViewInteraction = onView(
                allOf(withId(R.id.tv_ingredients_title), withText("Recipe Ingredients"), isDisplayed()));
        mViewInteraction.check(matches(withText("Recipe Ingredients")));
    }

    @Test
    public void checkForRecyclerViews() {
        ViewInteraction mViewInteraction = onView(
                allOf(withId(R.id.tv_steps_title), withText("Recipe Steps"),
                        isDisplayed()));
        mViewInteraction.check(matches(isDisplayed()));

        ViewInteraction ingredientRecyclerView = onView(
                allOf(withId(R.id.rv_ingredients_list),
                        isDisplayed()));
        ingredientRecyclerView.check(matches(isDisplayed()));

        ViewInteraction stepRecyclerView = onView(
                allOf(withId(R.id.rv_steps_list),
                        isDisplayed()));
        stepRecyclerView.check(matches(isDisplayed()));

    }


}