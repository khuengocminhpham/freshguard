import { useEffect, useState } from "react";
import { ItemCard } from "../components/ItemCard";
import { useItems } from "../context/ItemsContext";
import { ItemForm } from "../components/ItemForm";

export const Item = () => {
  const [isOpen, setIsOpen] = useState(false);
  const { items, getItems } = useItems();

  useEffect(() => {
    getItems();
  }, []);

  return (
    <div className="p-20">
      <div className="flex flex-row justify-between mb-20 items-center">
        <h1 className="text-4xl font-extrabold tracking-tight leading-none text-primary-600 md:text-5xl lg:text-6xl">
          My inventory
        </h1>

        <button
          onClick={() => setIsOpen(true)}
          type="button"
          className="px-6 py-3.5 text-base font-medium inline-flex items-center text-primary-700 hover:text-white border-2 border-primary-700 hover:bg-primary-800 focus:ring-4 focus:outline-none focus:ring-primary-300 rounded-lg text-center"
        >
          <svg
            className="w-5 h-5 me-1.5"
            aria-hidden="true"
            xmlns="http://www.w3.org/2000/svg"
            width="24"
            height="24"
            fill="currentColor"
            viewBox="0 0 24 24"
          >
            <path
              fillRule="evenodd"
              d="M2 12C2 6.477 6.477 2 12 2s10 4.477 10 10-4.477 10-10 10S2 17.523 2 12Zm11-4.243a1 1 0 1 0-2 0V11H7.757a1 1 0 1 0 0 2H11v3.243a1 1 0 1 0 2 0V13h3.243a1 1 0 1 0 0-2H13V7.757Z"
              clipRule="evenodd"
            />
          </svg>
          Add item
        </button>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-12 w-full">
        {items.map((item) => (
          <ItemCard key={item.id} data={item} />
        ))}
      </div>

      {isOpen && (
        <ItemForm
          isOpen={isOpen}
          onClose={() => setIsOpen(false)}
          create={true}
        />
      )}
    </div>
  );
};
