import { useEffect } from "react";
import { RecipeCard } from "../components/RecipeCard";
import { useRecipes } from "../context/RecipesContext";

export const Recipe = () => {
  const { recipes, getRecipes } = useRecipes();

  useEffect(() => {
    getRecipes();
  }, []);
  return (
    <div className="p-20">
      <h1 className="mb-20 text-4xl font-extrabold tracking-tight leading-none text-primary-900 md:text-5xl lg:text-6xl">
        My recipes
      </h1>

      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-12 w-full">
        {recipes.map((recipe) => (
          <RecipeCard key={recipe.id} data={recipe} />
        ))}
      </div>
    </div>
  );
};
