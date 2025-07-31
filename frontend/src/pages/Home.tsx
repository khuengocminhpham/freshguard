import { useEffect } from "react";
import { ItemCard } from "../components/ItemCard";
import { RecipeCard } from "../components/RecipeCard";
import { useItems } from "../context/ItemsContext";
import { useRecipes } from "../context/RecipesContext";
import { Link } from "react-router";

function Home() {
  const { items, getItems } = useItems();
  const { recipes, getRecipes } = useRecipes();
  useEffect(() => {
    getRecipes();
    getItems();
  }, []);


  return (
    <div className="p-20">
      <h1 className="mb-20 text-4xl font-extrabold tracking-tight leading-none text-primary-900 md:text-5xl lg:text-6xl">
        Fresh Guard!
      </h1>
      <div className="flex flex-row justify-between gap-32">
        <div className="flex flex-col gap-12">
          <Link
            to="/item"
            className="hover:text-green-900 text-2xl font-bold tracking-tight  leading-none text-green-700 md:text-3xl lg:text-4xl"
          >
            Ingredients
          </Link>
          {items && items.map((item) => (
            <ItemCard key={item.id} data={item} />
          ))}
          {items.length == 0 && <div>No item found</div>}
        </div>
        <div className="flex flex-col gap-12">
          <Link
            to="/recipe"
            className="hover:text-green-900 text-2xl font-bold tracking-tight  leading-none text-green-700 md:text-3xl lg:text-4xl"
          >
            Recipes
          </Link>
          {recipes.map((recipe) => (
            <RecipeCard key={recipe.id} data={recipe} itemView={false} />
          ))}
          {recipes.length == 0 && <div>No recipe found</div>}
        </div>
      </div>
    </div>
  );
}

export default Home;
