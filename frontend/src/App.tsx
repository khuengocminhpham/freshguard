import { useEffect, useState } from "react";
import "./App.css";
import { ItemCard } from "./components/ItemCard";
import { RecipeCard } from "./components/RecipeCard";
import { itemAPI, type Item } from "./services/api";
import { useItems } from "./context/ItemsContext";

function App() {
  const { items, getItems } = useItems();
  useEffect(() => {
    getItems();
  }, []);

  console.log(items);

  return (
    <>
      <h1>Fresh Guard!</h1>
      <div className="flex flex-row justify-around w-full">
        <div>
          <ItemCard />
        </div>
        <div>
          <RecipeCard />
        </div>
      </div>
    </>
  );
}

export default App;
