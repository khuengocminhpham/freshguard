import { useEffect, useState } from "react";
import { useRecipes } from "../context/RecipesContext";
import type { Item, Recipe } from "../services/api";

type EditRecipeModalProps = {
  recipe?: Recipe;
  isOpen: boolean;
  onClose: () => void;
  create: boolean;
};

type FormData = {
  name: string;
  description: string;
  instructions: string;
  servings: number;
  prepTimeMinutes: number;
  createdAt: string;
  ingredients: Item[];
};
export const RecipeForm = ({
  recipe,
  isOpen,
  onClose,
  create,
}: EditRecipeModalProps) => {
  const { createRecipe, updateRecipe, deleteRecipe, loading } = useRecipes();

  const [formData, setFormData] = useState<FormData>({
    name: "",
    description: "",
    instructions: "",
    servings: 0,
    prepTimeMinutes: 0,
    createdAt: "",
    ingredients: [],
  });

  const [isSubmitting, setIsSubmitting] = useState(false);

  useEffect(() => {
    if (recipe && !create) {
      setFormData({
        name: recipe.name || "",
        description: recipe.description || "",
        instructions: recipe.instructions || "",
        servings: recipe.servings || 0,
        prepTimeMinutes: recipe.prepTimeMinutes || 0,
        createdAt: recipe.createdAt || "",
        ingredients: recipe.ingredients || [],
      });
    } else if (create) {
      setFormData({
        name: "",
        description: "",
        instructions: "",
        servings: 0,
        prepTimeMinutes: 0,
        createdAt: "",
        ingredients: [],
      });
    }
  }, [recipe, create, isOpen]);

  const handleInputChange = (
    e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>
  ) => {
    const { name, value } = e.target;
    setFormData((prev) => ({
      ...prev,
      [name]: value,
    }));
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setIsSubmitting(true);

    try {
      console.log(formData.createdAt);

      const recipeData: Omit<Recipe, "id"> = {
        name: formData.name,
        description: formData.description,
        instructions: formData.instructions,
        servings: formData.servings,
        prepTimeMinutes: formData.prepTimeMinutes,
        createdAt:
          formData.createdAt == ""
            ? new Date().toISOString().slice(0, -1)
            : formData.createdAt,
        ingredients: formData.ingredients,
      };

      console.log(recipeData);

      if (create) {
        await createRecipe(recipeData);
      } else if (recipe?.id) {
        await updateRecipe(recipe.id, recipeData);
      }

      onClose();
    } catch (error) {
      console.error("Error saving recipe:", error);
    } finally {
      setIsSubmitting(false);
    }
  };

  const handleDelete = async () => {
    if (!recipe?.id) return;

    const confirmDelete = window.confirm(
      "Are you sure you want to delete this item?"
    );
    if (!confirmDelete) return;

    setIsSubmitting(true);
    try {
      await deleteRecipe(recipe.id);
      onClose();
    } catch (error) {
      console.error("Error deleting recipe:", error);
    } finally {
      setIsSubmitting(false);
    }
  };

  const handleClose = () => {
    if (!isSubmitting && !loading) {
      onClose();
    }
  };
  if (!isOpen) return null;
  return (
    <div className="fixed top-0 right-0 left-0 z-50 flex justify-center items-center w-full overflow-y-auto overflow-x-hidden md:inset-0 h-modal md:h-full bg-black/50">
      <div className="relative p-4 w-full max-w-2xl h-full md:h-auto">
        {/* Modal content */}
        <div className="relative p-4 bg-white rounded-lg shadowsm:p-5">
          {/* Header */}
          <div className="flex justify-between items-center pb-4 mb-4 rounded-t border-b sm:mb-5">
            <h3 className="text-lg font-semibold text-gray-900">
              {create ? "Create" : "Update"} Recipe
            </h3>
            <button
              onClick={handleClose}
              disabled={isSubmitting || loading}
              type="button"
              className="text-gray-400 bg-transparent hover:bg-gray-200 hover:text-gray-900 rounded-lg text-sm p-1.5 ml-auto inline-flex items-center"
              data-modal-toggle="updateProductModal"
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

          {/* Form body */}
          <form onSubmit={handleSubmit}>
            <div className="grid gap-4 mb-8 sm:grid-cols-2">
              <div>
                <label
                  htmlFor="name"
                  className="block mb-2 text-sm font-medium text-gray-900"
                >
                  Name
                </label>
                <input
                  type="text"
                  name="name"
                  id="name"
                  value={formData.name}
                  onChange={handleInputChange}
                  required
                  disabled={isSubmitting || loading}
                  className="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-primary-600 focus:border-primary-600 block w-full p-2.5"
                />
              </div>
              <div className="sm:col-span-2">
                <label
                  htmlFor="description"
                  className="block mb-2 text-sm font-medium text-gray-900"
                >
                  Description
                </label>
                <textarea
                  id="description"
                  name="description"
                  rows={2}
                  className="block p-2.5 w-full text-sm text-gray-900 bg-gray-50 rounded-lg border border-gray-300 focus:ring-primary-500 focus:border-primary-500"
                  value={formData.description}
                  onChange={handleInputChange}
                  required
                  disabled={isSubmitting || loading}
                ></textarea>
              </div>
              <div>
                <label
                  htmlFor="servings"
                  className="block mb-2 text-sm font-medium text-gray-900"
                >
                  Servings
                </label>
                <input
                  type="number"
                  name="servings"
                  id="servings"
                  min="0"
                  value={formData.servings}
                  onChange={handleInputChange}
                  required
                  disabled={isSubmitting || loading}
                  className="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-primary-600 focus:border-primary-600 block w-full p-2.5"
                />
              </div>
              <div>
                <label
                  htmlFor="servings"
                  className="block mb-2 text-sm font-medium text-gray-900"
                >
                  Prep time (in minutes)
                </label>
                <input
                  type="number"
                  name="prepTimeMinutes"
                  id="prepTimeMinutes"
                  min="0"
                  value={formData.prepTimeMinutes}
                  onChange={handleInputChange}
                  required
                  disabled={isSubmitting || loading}
                  className="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-primary-600 focus:border-primary-600 block w-full p-2.5"
                />
              </div>
              <div className="sm:col-span-2">
                <label
                  htmlFor="instructions"
                  className="block mb-2 text-sm font-medium text-gray-900"
                >
                  Instruction
                </label>
                <textarea
                  id="instructions"
                  name="instructions"
                  rows={5}
                  className="block p-2.5 w-full text-sm text-gray-900 bg-gray-50 rounded-lg border border-gray-300 focus:ring-primary-500 focus:border-primary-500"
                  value={formData.instructions}
                  onChange={handleInputChange}
                  required
                  disabled={isSubmitting || loading}
                ></textarea>
              </div>
            </div>

            {/* Footer */}
            <div className="flex items-center space-x-4">
              <button
                type="submit"
                disabled={isSubmitting || loading}
                className="text-white bg-primary-700 hover:bg-primary-800 focus:ring-4 focus:outline-none focus:ring-primary-300 font-medium rounded-lg text-sm px-5 py-2.5 text-center"
              >
                {(isSubmitting || loading) && (
                  <svg
                    className="animate-spin -ml-1 mr-3 h-4 w-4 text-white inline-block"
                    xmlns="http://www.w3.org/2000/svg"
                    fill="none"
                    viewBox="0 0 24 24"
                  >
                    <circle
                      className="opacity-25"
                      cx="12"
                      cy="12"
                      r="10"
                      stroke="currentColor"
                      strokeWidth="4"
                    />
                    <path
                      className="opacity-75"
                      fill="currentColor"
                      d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"
                    />
                  </svg>
                )}
                {create ? "Add" : "Update"} item
              </button>
              {!create && (
                <button
                  type="button"
                  onClick={handleDelete}
                  disabled={isSubmitting || loading}
                  className="text-red-600 inline-flex items-center hover:text-white border border-red-600 hover:bg-red-600 focus:ring-4 focus:outline-none focus:ring-red-300 font-medium rounded-lg text-sm px-5 py-2.5 text-center"
                >
                  <svg
                    className="mr-1 -ml-1 w-5 h-5"
                    fill="currentColor"
                    viewBox="0 0 20 20"
                  >
                    <path
                      fillRule="evenodd"
                      d="M9 2a1 1 0 00-.894.553L7.382 4H4a1 1 0 000 2v10a2 2 0 002 2h8a2 2 0 002-2V6a1 1 0 100-2h-3.382l-.724-1.447A1 1 0 0011 2H9zM7 8a1 1 0 012 0v6a1 1 0 11-2 0V8zm5-1a1 1 0 00-1 1v6a1 1 0 102 0V8a1 1 0 00-1-1z"
                      clipRule="evenodd"
                    />
                  </svg>
                  Delete
                </button>
              )}
            </div>
          </form>
        </div>
      </div>
    </div>
  );
};
