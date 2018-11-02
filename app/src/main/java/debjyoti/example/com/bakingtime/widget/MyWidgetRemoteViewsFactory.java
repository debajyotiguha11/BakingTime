package debjyoti.example.com.bakingtime.widget;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import debjyoti.example.com.bakingtime.R;
import debjyoti.example.com.bakingtime.RecipeDetailFragment;
import debjyoti.example.com.bakingtime.extras.ConnectionDetector;
import debjyoti.example.com.bakingtime.extras.Ingredient;

/**
 * Created by 007 on 25/08/2017.
 */

public class MyWidgetRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private final String TAG = MyWidgetRemoteViewsFactory.class.getSimpleName();
    private Context mContext;
    private ConnectionDetector cd;

    private ArrayList<Ingredient> mIngredients = new ArrayList<>();

    public MyWidgetRemoteViewsFactory(Context mContext) {
        this.mContext = mContext;
        this.cd = new ConnectionDetector(mContext);
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {

        SharedPreferences mSharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(mContext);
        Gson gson = new Gson();
        String json = mSharedPreferences.getString(RecipeDetailFragment.INGREDINT_LIST_FOR_WIDGET, "");
        if (json.isEmpty()) {
            Log.d("No Available Ingredient", json);

        } else {
            Type type = new TypeToken<List<Ingredient>>() {
            }.getType();
            mIngredients = gson.fromJson(json, type);


        }

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public RemoteViews getViewAt(int position) {

        Ingredient ingredient = mIngredients.get(position);
        RemoteViews ingredient_view = new RemoteViews(mContext.getPackageName(), R.layout.widget_list_item);
        ingredient_view.setTextViewText(R.id.ingredient_widget_name, ingredient.getIngredients());
        ingredient_view.setTextViewText(R.id.ingredient_widget_quantity, ingredient.getQuantity() + "");

        Intent intent = new Intent();
        intent.putExtra("item", position);
        ingredient_view.setOnClickFillInIntent(R.id.widget_list, intent);

        return ingredient_view;
    }


    @Override
    public int getCount() {
        return (mIngredients == null) ? 0 : mIngredients.size();
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }


}
