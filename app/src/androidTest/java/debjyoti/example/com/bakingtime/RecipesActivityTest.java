package debjyoti.example.com.bakingtime;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import debjyoti.example.com.bakingtime.RecipesActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class RecipesActivityTest {

    @Rule
    public final ActivityTestRule<RecipesActivity> mActivityTestRule = new ActivityTestRule<RecipesActivity>(RecipesActivity.class);

    @Test
    public void clickingRecipeResult() {
        Espresso.registerIdlingResources(mActivityTestRule.getActivity().countingIdlingResource);
        onView(withId(R.id.rv_recipe_list)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
    }

}