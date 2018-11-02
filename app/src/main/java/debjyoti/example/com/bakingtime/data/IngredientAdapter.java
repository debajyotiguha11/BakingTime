package debjyoti.example.com.bakingtime.data;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import debjyoti.example.com.bakingtime.extras.Ingredient;
import debjyoti.example.com.bakingtime.R;


public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.ViewHolder> {
    private final ArrayList<Ingredient> ingredientArrayList;

    public IngredientAdapter(ArrayList<Ingredient> ingredientArrayList) {
        this.ingredientArrayList = ingredientArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.ingredient_card_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.ingredientName.setText(ingredientArrayList.get(position).getIngredients());
        String measure = ingredientArrayList.get(position).getQuantity() + "    " + ingredientArrayList.get(position).getMeasure();
        holder.ingredient_quantity_measure.setText(measure);
    }

    @Override
    public int getItemCount() {
        return ingredientArrayList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final TextView ingredientName;
        final TextView ingredient_quantity_measure;

        ViewHolder(View view) {
            super(view);
            ingredientName = view.findViewById(R.id.ingredient_name);
            ingredient_quantity_measure = view.findViewById(R.id.ingredient_quantity);

        }
    }
}
