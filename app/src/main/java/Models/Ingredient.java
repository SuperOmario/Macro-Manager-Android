package Models;

public class Ingredient {
    public int RecipeIngredientID;
    public int IngredientID;
    public double Servings;
    public double Amounts;

    public Ingredient(int RecipeIngredientID, int IngredientID, double Servings, double Amounts) {
        this.RecipeIngredientID = RecipeIngredientID;
        this.IngredientID = IngredientID;
        this.Servings = Servings;
        this.Amounts = Amounts;
    }
}
