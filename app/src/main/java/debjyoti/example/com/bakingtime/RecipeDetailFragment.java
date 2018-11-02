package debjyoti.example.com.bakingtime;

import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;

import debjyoti.example.com.bakingtime.data.IngredientAdapter;
import debjyoti.example.com.bakingtime.data.StepsAdapter;
import debjyoti.example.com.bakingtime.extras.Extras;
import debjyoti.example.com.bakingtime.extras.Ingredient;
import debjyoti.example.com.bakingtime.extras.Step;


public class RecipeDetailFragment extends android.support.v4.app.Fragment implements StepsAdapter.StepClickHandler {

    public static final String INGREDINT_LIST_FOR_WIDGET = "INGREDINT_LIST_FOR_WIDGET";

    private RecyclerView ingredientsRecyclerView;
    private RecyclerView stepsRecyclerView;
    private ImageView thumbImage;
    private ArrayList<Ingredient> ingredientArrayList;
    private ArrayList<Step> stepsArrayList;
    private Button addToWidget;


    //Empty constructor
    public RecipeDetailFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_recipe_detail, container, false);
        ingredientsRecyclerView = rootView.findViewById(R.id.rv_ingredients_list);
        stepsRecyclerView = rootView.findViewById(R.id.rv_steps_list);
        addToWidget = rootView.findViewById(R.id.btn_add_to_widget);
        thumbImage = rootView.findViewById(R.id.iv_thumbnail_description_step);
        ingredientArrayList = getArguments().getParcelableArrayList("Ingredients");
        stepsArrayList = getArguments().getParcelableArrayList("Steps");

        IngredientAdapter ingredientsAdapter = new IngredientAdapter
                (ingredientArrayList);
        ingredientsRecyclerView.setAdapter(ingredientsAdapter);
        StepsAdapter stepsRecyclerViewAdapter = new StepsAdapter(stepsArrayList, this);
        stepsRecyclerView.setAdapter(stepsRecyclerViewAdapter);

        ingredientsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        stepsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        addToWidget.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                SharedPreferences mSharedPreferences = PreferenceManager
                        .getDefaultSharedPreferences(getContext());
                SharedPreferences.Editor mEditor = mSharedPreferences.edit();
                Gson gson = new Gson();
                String json = gson.toJson(ingredientArrayList);
                mEditor.putString(INGREDINT_LIST_FOR_WIDGET, json).apply();
                Toast.makeText(getActivity(), " This Recipe has been added to The Widget", Toast.LENGTH_LONG).show();
                Intent intent = new Intent();
                intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
                getActivity().sendBroadcast(intent);


            }
        });

        return rootView;
    }

    @Override
    public void onStepClickHandler(int position) {
        StepFragmentListener ingredientStepsOnClickListener = (StepFragmentListener) getActivity();
        Bundle bundle = new Bundle();
        bundle.putString(Extras.VIDEO_URL, stepsArrayList.get(position).getVideoUrl());
        bundle.putString(Extras.STEP_DESCRIPTION, stepsArrayList.get(position).getDescription());
        bundle.putString(Extras.THUMBNAIL, stepsArrayList.get(position).getThumbnailUrl());
        bundle.putInt(Extras.STEP_INDEX, stepsArrayList.get(position).getStep_id());
        bundle.putParcelableArrayList(Extras.STEPS, stepsArrayList);
        ingredientStepsOnClickListener.onStepFragmentListener(bundle);
    }


    public interface StepFragmentListener {
        void onStepFragmentListener(Bundle bundle);
    }

}