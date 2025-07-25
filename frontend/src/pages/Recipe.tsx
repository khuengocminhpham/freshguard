import { RecipeCard } from "../components/RecipeCard";

export const Recipe = () => {
  return (
    <div className="p-20">
      <h1 className="mb-20 text-4xl font-extrabold tracking-tight leading-none text-primary-900 md:text-5xl lg:text-6xl">
        My recipes
      </h1>

      <div className="flex flex-wrap content-center-safe gap-8 justify-between">
        <RecipeCard />
        <RecipeCard />
        <RecipeCard />
        <RecipeCard />
        <RecipeCard />
        <RecipeCard />
        <RecipeCard />
        <RecipeCard />
        <RecipeCard />
      </div>
    </div>
  );
};
