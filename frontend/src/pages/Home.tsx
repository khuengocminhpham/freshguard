import { ItemCard } from "../components/ItemCard";
import { RecipeCard } from "../components/RecipeCard";

export const Home = () => {
  return (
    <div>
      <h1>Fresh Guard!</h1>
      <div className="flex flex-row justify-around w-full">
        <div>
          <ItemCard />
        </div>
        <div>
          <RecipeCard />
        </div>
      </div>
    </div>
  );
};
