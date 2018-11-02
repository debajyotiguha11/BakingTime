package debjyoti.example.com.bakingtime.data;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import debjyoti.example.com.bakingtime.extras.Recipe;
import debjyoti.example.com.bakingtime.R;


public class RecipesAdapter extends RecyclerView.Adapter<RecipesAdapter.RecipeListViewHolder> {

    private final ArrayList<Recipe> recipeArrayList;
    private final RecipeListOnClickListener recipeListOnClickListener;
    Context mContext;

    public RecipesAdapter(ArrayList<Recipe> recipeArrayList, RecipeListOnClickListener recipeListOnClickListener) {
        this.recipeArrayList = recipeArrayList;
        this.recipeListOnClickListener = recipeListOnClickListener;
    }

    @NonNull
    @Override
    public RecipeListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.recipe_card_item, parent, false);
        return new RecipeListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeListViewHolder holder, int position) {
        holder.recipeName.setText(recipeArrayList.get(position).getName());
//        holder.servings.setText(recipeArrayList.get(position).getServings());
        String imageUrl = recipeArrayList.get(position).getImage();
        if (!imageUrl.isEmpty()) {
            Uri builtUri = Uri.parse(imageUrl);
            Picasso.with(mContext).load(builtUri).into(holder.mImage);
        } else {
            holder.mImage.setVisibility(View.GONE);
        }

        Log.d("Debug", "the problem in binding the data");

    }

    @Override
    public int getItemCount() {
        return recipeArrayList.size();
    }

    //Interface to handle clicks on the  RecyclerView
    public interface RecipeListOnClickListener {
        void onRecipeItemClicked(Recipe recipe);
    }


    public class RecipeListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView recipeName;
        CardView mCardView;
        ImageView mImage;

        RecipeListViewHolder(View view) {
            super(view);
            recipeName = view.findViewById(R.id.tv_recipe_name);
            mCardView = view.findViewById(R.id.recipe_card_view);
            mImage = view.findViewById(R.id.recipe_image);
            view.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            Recipe recipe = recipeArrayList.get(position);
            recipeListOnClickListener.onRecipeItemClicked(recipe);
            Log.e("Error", "error in on Click of recipes adapter");
        }

    }

}
