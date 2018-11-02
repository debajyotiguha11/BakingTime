package debjyoti.example.com.bakingtime.extras;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;


public class Recipe implements Parcelable {
    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Recipe> CREATOR = new Parcelable.Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };
    private int recipeId;
    private String name;
    private ArrayList<Ingredient> ingredientsArrayList;
    private ArrayList<Step> stepsArrayList;
    private String image;
    private int servings;


    public Recipe(int recipeId, String name, ArrayList<Ingredient> ingredientsArrayList, ArrayList<Step> stepsList, String image, int servings) {
        this.recipeId = recipeId;
        this.name = name;
        this.ingredientsArrayList = ingredientsArrayList;
        this.stepsArrayList = stepsList;
        this.image = image;
        this.servings = servings;
    }

    private Recipe(Parcel in) {
        recipeId = in.readInt();
        name = in.readString();
        if (in.readByte() == 0x01) {
            ingredientsArrayList = new ArrayList<>();
            in.readList(ingredientsArrayList, Ingredient.class.getClassLoader());
        } else {
            ingredientsArrayList = null;
        }
        if (in.readByte() == 0x01) {
            stepsArrayList = new ArrayList<>();
            in.readList(stepsArrayList, Step.class.getClassLoader());
        } else {
            stepsArrayList = null;
        }
        servings = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(recipeId);
        dest.writeString(name);
        if (ingredientsArrayList == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(ingredientsArrayList);
        }
        if (stepsArrayList == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(stepsArrayList);
        }
        dest.writeInt(servings);
    }

    public String getName() {
        return name;
    }

    public ArrayList<Ingredient> getIngredientsArrayList() {
        return ingredientsArrayList;
    }

    public ArrayList<Step> getStepsArrayList() {
        return stepsArrayList;
    }

    public int getServings() {
        return servings;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}

