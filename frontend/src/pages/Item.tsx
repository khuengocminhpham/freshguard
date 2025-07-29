import { useEffect } from "react";
import { ItemCard } from "../components/ItemCard";
import { useItems } from "../context/ItemsContext";

export const Item = () => {
  const { items, getItems } = useItems();

  useEffect(() => {
    getItems();
  }, []);
  return (
    <div className="p-20">
      <h1 className="mb-20 text-4xl font-extrabold tracking-tight leading-none text-primary-600 md:text-5xl lg:text-6xl">
        My inventory
      </h1>

      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-12 w-full">
        {items.map((item) => (
          <ItemCard key={item.id} data={item} />
        ))}
      </div>
    </div>
  );
};
