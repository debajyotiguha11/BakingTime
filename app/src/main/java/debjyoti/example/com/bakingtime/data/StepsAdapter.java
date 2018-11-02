package debjyoti.example.com.bakingtime.data;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import debjyoti.example.com.bakingtime.extras.Step;
import debjyoti.example.com.bakingtime.R;

public class StepsAdapter extends RecyclerView.Adapter<StepsAdapter.ViewHolder> {

    private ArrayList<Step> stepsArrayList;
    private StepClickHandler stepsOnClickListener;

    public StepsAdapter(ArrayList<Step> stepsArrayList, StepClickHandler stepsOnClickListener) {
        this.stepsArrayList = stepsArrayList;
        this.stepsOnClickListener = stepsOnClickListener;
    }

    @NonNull
    @Override
    public StepsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.step_card_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return stepsArrayList.size();
    }

    public interface StepClickHandler {
        void onStepClickHandler(int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView stepShortDescription;

        ViewHolder(View view) {
            super(view);
            stepShortDescription = view.findViewById(R.id.step_short_description);
            stepShortDescription.setOnClickListener(this);
        }

        void bind(int position) {
            stepShortDescription.setText(stepsArrayList.get(position).getShortDescription());
        }

        @Override
        public void onClick(View v) {
            stepsOnClickListener.onStepClickHandler(getAdapterPosition());
            Log.v("Steps Adapter", "Clicked");
        }
    }
}
