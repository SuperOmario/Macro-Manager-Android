package models;

public class DiaryEntry {
    public int DiaryEntryID, UserID, RecipeID;
    public String Date, Meal;
    public float Calories, Carbohydrate, Fat, Protein, Servings;
    public String[] Misc;

    public DiaryEntry(int diaryEntryID, int userID, int recipeID, String date, String meal, float calories, float carbohydrate, float fat, float protein, float servings, String[] misc) {
        DiaryEntryID = diaryEntryID;
        UserID = userID;
        RecipeID = recipeID;
        Date = date;
        Meal = meal;
        Calories = calories;
        Carbohydrate = carbohydrate;
        Fat = fat;
        Protein = protein;
        Servings = servings;
        Misc = misc;
    }
}
