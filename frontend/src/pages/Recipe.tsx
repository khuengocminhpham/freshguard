import { redirect, useNavigate, useParams } from "react-router";
import { useRecipes } from "../context/RecipesContext";
import { useEffect, useState } from "react";
import { RecipeForm } from "../components/RecipeForm";
import { useItems } from "../context/ItemsContext";
import type { Item } from "../services/api";

export const Recipe = () => {
  const navigate = useNavigate();
  const { recipeId } = useParams<{ recipeId: string }>();
  const {
    selectedRecipe,
    getRecipeById,
    addIngredient,
    removeIngredient,
    deleteRecipe,
  } = useRecipes();
  const { items, getItems } = useItems();
  const [isOpen, setIsOpen] = useState<boolean>(false);
  const [showIngredientForm, setShowIngredientForm] = useState<boolean>(false);
  const [selectedItemId, setSelectedItemId] = useState<string>("");
  const [filteredItems, setFilteredItems] = useState<Item[]>([]);
  const [searchTerm, setSearchTerm] = useState<string>("");
  const [showDeleteConfirm, setShowDeleteConfirm] = useState<boolean>(false);

  useEffect(() => {
    if (recipeId) {
      getRecipeById(Number(recipeId));
    }
  }, [recipeId]);

  useEffect(() => {
    getItems();
  }, []);

  useEffect(() => {
    if (searchTerm) {
      setFilteredItems(
        items.filter((item: Item) =>
          item.name.toLowerCase().includes(searchTerm.toLowerCase())
        )
      );
    } else {
      setFilteredItems(items);
    }
  }, [items, searchTerm]);

  const handleAddIngredient = async (): Promise<void> => {
    if (!selectedItemId || !recipeId) {
      return;
    }

    try {
      await addIngredient(Number(recipeId), Number(selectedItemId));
      setSelectedItemId("");
      setSearchTerm("");
    } catch (error) {
      console.error("Failed to add ingredient:", error);
    }
  };

  const handleRemoveIngredient = async (itemId: number): Promise<void> => {
    if (!recipeId) return;

    try {
      await removeIngredient(Number(recipeId), itemId);
    } catch (error) {
      console.error("Failed to remove ingredient:", error);
    }
  };

  const handleCancelAddIngredient = (): void => {
    setSelectedItemId("");
    setSearchTerm("");
    setShowIngredientForm(false);
  };

  const handleDeleteRecipe = async (): Promise<void> => {
    if (!recipeId) return;

    try {
      await deleteRecipe(Number(recipeId));
      setShowDeleteConfirm(false);
      navigate("/recipes");
    } catch (error) {
      console.error("Failed to delete recipe:", error);
    }
  };

  return (
    <div className="p-20">
      <div className="flex flex-row justify-between mb-8 items-start">
        <div>
          <h1 className="text-4xl font-extrabold tracking-tight leading-none text-primary-600 md:text-5xl lg:text-6xl mb-4">
            {selectedRecipe?.name}
          </h1>
          <h4 className="text-2xl font-normal text-gray-400">
            {selectedRecipe?.description}
          </h4>
        </div>

        <div className="flex items-center space-x-4">
          <button
            onClick={() => setIsOpen(true)}
            type="button"
            className="px-6 py-3.5 text-base font-medium inline-flex items-center text-primary-700 hover:text-white border-2 border-primary-700 hover:bg-primary-700 focus:ring-4 focus:outline-none focus:ring-primary-300 rounded-lg text-center"
          >
            <svg
              aria-hidden="true"
              className="w-5 h-5 me-1.5"
              fill="currentColor"
              viewBox="0 0 20 20"
              xmlns="http://www.w3.org/2000/svg"
            >
              <path d="M17.414 2.586a2 2 0 00-2.828 0L7 10.172V13h2.828l7.586-7.586a2 2 0 000-2.828z"></path>
              <path
                fillRule="evenodd"
                d="M2 6a2 2 0 012-2h4a1 1 0 010 2H4v10h10v-4a1 1 0 112 0v4a2 2 0 01-2 2H4a2 2 0 01-2-2V6z"
                clipRule="evenodd"
              ></path>
            </svg>
            Edit
          </button>
          <button
            onClick={() => setShowDeleteConfirm(true)}
            type="button"
            className="px-6 py-3.5 text-base font-medium inline-flex items-center text-red-600 hover:text-white border-2 border-red-600 hover:bg-red-600 focus:ring-4 focus:outline-none focus:ring-red-300 rounded-lg text-center"
          >
            <svg
              aria-hidden="true"
              className="w-5 h-5 me-1.5"
              fill="currentColor"
              viewBox="0 0 20 20"
              xmlns="http://www.w3.org/2000/svg"
            >
              <path
                fillRule="evenodd"
                d="M9 2a1 1 0 00-.894.553L7.382 4H4a1 1 0 000 2v10a2 2 0 002 2h8a2 2 0 002-2V6a1 1 0 100-2h-3.382l-.724-1.447A1 1 0 0011 2H9zM7 8a1 1 0 012 0v6a1 1 0 11-2 0V8zm5-1a1 1 0 00-1 1v6a1 1 0 102 0V8a1 1 0 00-1-1z"
                clipRule="evenodd"
              ></path>
            </svg>
            Delete
          </button>
        </div>
      </div>
      <dl className="mb-8">
        <dt className="text-xl md:text-2xl lg:text-2xl mb-4 font-semibold leading-none text-gray-900">
          Instructions
        </dt>
        <dd className="mb-8 text-base md:text-xl lg:text-xl font-light text-gray-500 sm:mb-5">
          {selectedRecipe?.instructions}
        </dd>
      </dl>
      <dl className="flex items-center space-x-24 mb-8">
        <div>
          <dt className="text-xl md:text-2xl lg:text-2xl mb-4 font-semibold leading-none text-gray-900">
            Servings
          </dt>
          <dd className="text-base md:text-xl lg:text-xl font-light text-gray-500">
            {selectedRecipe?.servings}
          </dd>
        </div>
        <div>
          <dt className="text-xl md:text-2xl lg:text-2xl mb-4 font-semibold leading-none text-gray-900">
            Prep time (in minutes)
          </dt>
          <dd className="text-base md:text-xl lg:text-xl font-light text-gray-500">
            {selectedRecipe?.prepTimeMinutes}
          </dd>
        </div>
      </dl>
      <dl className="mb-12">
        <dt className="text-xl md:text-2xl lg:text-2xl mb-4 font-semibold leading-none text-gray-900">
          Ingredients
        </dt>
        <dd className="mb-8">
          <div className="mb-5 flex">
            {selectedRecipe?.ingredients?.length != 0 ? (
              selectedRecipe?.ingredients?.map((item) => (
                <span
                  key={item.id}
                  className="bg-gray-200 text-gray-800 border border-gray-400 text-md font-medium me-2 px-2.5 py-0.5 rounded-full flex items-end"
                >
                  {item.name}
                  <button
                    onClick={() => item.id && handleRemoveIngredient(item.id)}
                    className="text-red-600 hover:text-red-800 p-1"
                    aria-label="Remove ingredient"
                  >
                    <svg
                      className="w-4 h-4"
                      fill="currentColor"
                      viewBox="0 0 20 20"
                    >
                      <path
                        fillRule="evenodd"
                        d="M4.293 4.293a1 1 0 011.414 0L10 8.586l4.293-4.293a1 1 0 111.414 1.414L11.414 10l4.293 4.293a1 1 0 01-1.414 1.414L10 11.414l-4.293 4.293a1 1 0 01-1.414-1.414L8.586 10 4.293 5.707a1 1 0 010-1.414z"
                        clipRule="evenodd"
                      ></path>
                    </svg>
                  </button>
                </span>
              ))
            ) : (
              <div className="text-gray-500 italic text-base md:text-xl lg:text-xl font-light">
                No ingredients added yet!
              </div>
            )}
          </div>
        </dd>
      </dl>
      <div className="flex items-center space-x-4 mb-4">
        <button
          onClick={() => setShowIngredientForm((prev) => !prev)}
          type="button"
          className="text-white inline-flex items-center bg-primary-700 hover:bg-primary-800 focus:ring-4 focus:outline-none focus:ring-primary-300 font-medium rounded-lg text-base px-5 py-2.5 text-center"
        >
          <svg
            aria-hidden="true"
            className="mr-1 -ml-1 w-5 h-5"
            fill="currentColor"
            viewBox="0 0 24 24"
            xmlns="http://www.w3.org/2000/svg"
          >
            <path
              fillRule="evenodd"
              d="M2 12C2 6.477 6.477 2 12 2s10 4.477 10 10-4.477 10-10 10S2 17.523 2 12Zm11-4.243a1 1 0 1 0-2 0V11H7.757a1 1 0 1 0 0 2H11v3.243a1 1 0 1 0 2 0V13h3.243a1 1 0 1 0 0-2H13V7.757Z"
              clipRule="evenodd"
            />
          </svg>
          Add Ingredient
        </button>
      </div>
      {isOpen && (
        <RecipeForm
          recipe={selectedRecipe}
          isOpen={isOpen}
          onClose={() => setIsOpen(false)}
          create={false}
          deleteBtn={false}
        />
      )}

      <div className="mb-8">
        {showIngredientForm && (
          <div className="bg-gray-50 p-4 rounded-lg mb-4">
            <div className="mb-8">
              {showIngredientForm && (
                <div className="bg-gray-50 p-4 rounded-lg mb-4">
                  <h3 className="text-lg font-medium text-gray-900 mb-3">
                    Add New Ingredient
                  </h3>
                  <div className="mb-3">
                    <input
                      type="text"
                      placeholder="Search for ingredients..."
                      value={searchTerm}
                      onChange={(e: React.ChangeEvent<HTMLInputElement>) =>
                        setSearchTerm(e.target.value)
                      }
                      className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-primary-500 focus:border-transparent"
                    />
                  </div>

                  {searchTerm && (
                    <div className="mb-3 max-h-40 overflow-y-auto border border-gray-300 rounded-md">
                      {filteredItems.length > 0 ? (
                        filteredItems.map((item: Item) => (
                          <button
                            role="option"
                            className="w-full text-left px-3 py-2 hover:bg-gray-100 focus:outline-none focus:ring-2 focus:ring-primary-500"
                            key={item.id}
                            onClick={() => {
                              item.id && setSelectedItemId(item.id.toString());
                              setSearchTerm(item.name);
                            }}
                          >
                            {item.name}
                          </button>
                        ))
                      ) : (
                        <div className="px-3 py-2 text-gray-500">
                          No items found
                        </div>
                      )}
                    </div>
                  )}

                  <div className="flex justify-end space-x-2 mt-3">
                    <button
                      onClick={handleCancelAddIngredient}
                      className="px-4 py-2 text-gray-600 hover:text-gray-800 border border-gray-300 rounded-md hover:bg-gray-50"
                    >
                      Cancel
                    </button>
                    <button
                      onClick={handleAddIngredient}
                      disabled={!selectedItemId}
                      className="px-4 py-2 bg-primary-600 text-white rounded-md hover:bg-primary-700 focus:outline-none focus:ring-2 focus:ring-primary-500 disabled:bg-gray-400 disabled:cursor-not-allowed"
                    >
                      Add Ingredient
                    </button>
                  </div>
                </div>
              )}
            </div>
          </div>
        )}
      </div>

      {showDeleteConfirm && (
        <div className="fixed top-0 right-0 left-0 z-50 flex justify-center items-center w-full overflow-y-auto overflow-x-hidden md:inset-0 h-modal md:h-full bg-black/50">
          <div className="relative p-4 w-full max-w-2xl h-full md:h-auto">
            <div className="relative p-4 bg-white rounded-lg shadow sm:p-5 text-center">
              <div className="mt-2 px-7 py-3">
                <div className="mx-auto mb-2 flex items-center justify-center h-12 w-12 rounded-full bg-red-100">
                  <svg
                    className="h-6 w-6 text-red-600"
                    fill="none"
                    viewBox="0 0 24 24"
                    strokeWidth="1.5"
                    stroke="currentColor"
                  >
                    <path
                      strokeLinecap="round"
                      strokeLinejoin="round"
                      d="M12 9v3.75m-9.303 3.376c-.866 1.5.217 3.374 1.948 3.374h14.71c1.73 0 2.813-1.874 1.948-3.374L13.949 3.378c-.866-1.5-3.032-1.5-3.898 0L2.697 16.126zM12 15.75h.007v.008H12v-.008z"
                    />
                  </svg>
                </div>
                <h3 className="text-lg font-medium text-gray-900">
                  Delete Recipe
                </h3>
                <p className="mt-2 text-sm text-gray-500">
                  Are you sure you want to delete "{selectedRecipe?.name}"? This
                  action cannot be undone.
                </p>
              </div>
              <div className="items-center px-4 py-3">
                <div className="flex justify-end space-x-3">
                  <button
                    onClick={() => setShowDeleteConfirm(false)}
                    className="px-4 py-2 bg-gray-200 text-gray-800 text-base font-medium rounded-md hover:bg-gray-300 focus:outline-none focus:ring-2 focus:ring-gray-300"
                  >
                    Cancel
                  </button>
                  <button
                    onClick={handleDeleteRecipe}
                    className="px-4 py-2 bg-red-600 text-white text-base font-medium rounded-md hover:bg-red-700 focus:outline-none focus:ring-2 focus:ring-red-300"
                  >
                    Delete
                  </button>
                </div>
              </div>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};
