import { useEffect } from "react";
import { useItems } from "../context/ItemsContext";
import { RecipeCard } from "./RecipeCard";

type Props = {
  id?: number;
  name?: string;
  onClose: () => void;
};
export const RecipePopUp = ({ id, name, onClose }: Props) => {
  const { getItemRecipes, selectedItem, loading, error } = useItems();

  useEffect(() => {
    if (id) {
      getItemRecipes(id);
    }
  }, [id]);

  return (
    <div className="fixed top-0 right-0 left-0 z-50 flex justify-center items-center w-full overflow-y-auto overflow-x-hidden md:inset-0 h-modal md:h-full bg-black/50">
      <div className="relative p-4 w-full max-w-2xl h-full md:h-auto">
        <div className="relative p-4 bg-white rounded-lg shadow sm:p-5">
          <div className="flex justify-between items-center pb-4 mb-4 rounded-t border-b sm:mb-5">
            <h3 className="text-lg font-semibold text-gray-900">
              Recipes with {name}
            </h3>
            <button
              onClick={onClose}
              type="button"
              className="text-gray-400 bg-transparent hover:bg-gray-200 hover:text-gray-900 rounded-lg text-sm p-1.5 ml-auto inline-flex items-center"
            >
              <svg
                aria-hidden="true"
                className="w-5 h-5"
                fill="currentColor"
                viewBox="0 0 20 20"
              >
                <path
                  fillRule="evenodd"
                  d="M4.293 4.293a1 1 0 011.414 0L10 8.586l4.293-4.293a1 1 0 111.414 1.414L11.414 10l4.293 4.293a1 1 0 01-1.414 1.414L10 11.414l-4.293 4.293a1 1 0 01-1.414-1.414L8.586 10 4.293 5.707a1 1 0 010-1.414z"
                  clipRule="evenodd"
                />
              </svg>
              <span className="sr-only">Close modal</span>
            </button>
          </div>
          {loading && (
            <div className="text-center py-4">
              <div className="text-gray-600">
                Loading recipes...
              </div>
            </div>
          )}
          {error && (
            <div className="text-center py-4">
              <div className="text-red-600">{error}</div>
            </div>
          )}

          {!loading &&
            !error &&
            selectedItem?.recipes &&
            selectedItem?.recipes.length > 0 &&
            selectedItem?.recipes.map((recipe) => (
              <RecipeCard itemView={true} data={recipe} key={recipe.id} />
            ))}
          {!loading &&
            !error &&
            (!selectedItem?.recipes || selectedItem?.recipes.length === 0) && (
              <div className="text-center py-4">
                <div className="text-gray-600">
                  No recipes found for {name}
                </div>
              </div>
            )}
        </div>
      </div>
    </div>
  );
};
