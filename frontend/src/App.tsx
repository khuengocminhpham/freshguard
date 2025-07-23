import { useEffect, useState } from "react";
import "./App.css";
import { ItemCard } from "./components/ItemCard";
import { RecipeCard } from "./components/RecipeCard";
import { itemAPI, type Item } from "./services/api";

function App() {
  const [items, setItems] = useState<Item[]>();
  const [loading, setLoading] = useState<boolean>(true);

  useEffect(() => {
    fetchItems();
  }, []);

  async function fetchItems() {
    try {
      const response = await itemAPI.getItems();
      setItems(response.data);
    } catch (error) {
      console.error("Error fetching users:", error);
    } finally {
      setLoading(false);
    }
  }

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
      {items?.map((item) => (
        <div key={item.id}>{item.name}</div>
      ))}
    </>
  );
}

export default App;
