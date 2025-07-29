import { createContext, useContext, useState, type ReactNode } from "react";
import { recipeAPI, type Recipe } from "../services/api";
interface RecipesContextType {
  recipes: Recipe[];
  selectedRecipe: Recipe | undefined;
  error: string | null;
  loading: boolean;
  getRecipes: () => Promise<Recipe[]>;
  createRecipe: (recipe: Omit<Recipe, "id">) => Promise<Recipe | never[]>;
  getRecipeById: (id: number) => Promise<Recipe | null>;
  updateRecipe: (
    id: number,
    recipe: Omit<Recipe, "id">
  ) => Promise<Recipe | null>;
  updateRecipePartially: (
    id: number,
    recipe: Partial<Omit<Recipe, "id">>
  ) => Promise<Recipe | null>;
  deleteRecipe: (id: number) => Promise<void>;
  addIngredient: (recipeId: number, itemId: number) => Promise<Recipe | null>;
  removeIngredient: (
    recipeId: number,
    itemId: number
  ) => Promise<Recipe | null>;
  setIngredients: (
    recipeId: number,
    itemIds: number[]
  ) => Promise<Recipe | null>;
  findByIngredients: (itemIds: number[]) => Promise<Recipe[]>;
  setSelectedRecipe: (recipe: Recipe | undefined) => void;
}
const RecipesContext = createContext<RecipesContextType | null>(null);

interface RecipesProviderProps {
  children: ReactNode;
}
export function RecipesProvider({ children }: RecipesProviderProps) {
  const [recipes, setRecipes] = useState<Recipe[]>([]);
  const [selectedRecipe, setSelectedRecipe] = useState<Recipe>();
  const [error, setError] = useState(null);
  const [loading, setLoading] = useState(false);

  const getRecipes = async () => {
    setLoading(true);
    setError(null);
    try {
      const response = await recipeAPI.getRecipes();
      setRecipes(response.data.reverse() || []);
      return response.data || [];
    } catch (err: any) {
      const errorMessage =
        err.response?.data?.error?.message || "Error fetching recipes";
      setError(errorMessage);
      console.error(errorMessage);
      return [];
    } finally {
      setLoading(false);
    }
  };

  const createRecipe = async (recipe: Omit<Recipe, "id">) => {
    setLoading(true);
    setError(null);
    try {
      const response = await recipeAPI.createRecipe(recipe);
      const newRecipe = response.data;
      setRecipes((prev) => [newRecipe, ...prev]);
      return newRecipe;
    } catch (err: any) {
      const errorMessage =
        err.response?.data?.error?.message || "Error creating recipe";
      setError(errorMessage);
      console.error(errorMessage);
      return [];
    } finally {
      setLoading(false);
    }
  };

  const getRecipeById = async (id: number) => {
    setLoading(true);
    setError(null);
    try {
      const response = await recipeAPI.getRecipeById(id);
      setSelectedRecipe(response.data);
      return response.data;
    } catch (err: any) {
      const errorMessage =
        err.response?.data?.error?.message || "Error getting selected recipe";
      setError(errorMessage);
      console.error(errorMessage);
      return null;
    } finally {
      setLoading(false);
    }
  };

  const updateRecipe = async (id: number, recipe: Omit<Recipe, "id">) => {
    setLoading(true);
    setError(null);
    try {
      const response = await recipeAPI.updateRecipe(id, recipe);
      const updatedRecipe = response.data;
      setRecipes(
        recipes.map((recipe) => (recipe.id === id ? updatedRecipe : recipe))
      );
      if (selectedRecipe?.id === id) {
        setSelectedRecipe(updatedRecipe);
      }
      return response.data;
    } catch (err: any) {
      const errorMessage =
        err.response?.data?.error?.message || "Error updating recipe";
      setError(errorMessage);
      console.error(errorMessage);
      return null;
    } finally {
      setLoading(false);
    }
  };

  const updateRecipePartially = async (
    id: number,
    recipe: Partial<Omit<Recipe, "id">>
  ) => {
    setLoading(true);
    setError(null);
    try {
      const response = await recipeAPI.updateRecipePartially(id, recipe);
      const updatedRecipe = response.data;
      setRecipes(
        recipes.map((recipe) => (recipe.id === id ? updatedRecipe : recipe))
      );
      if (selectedRecipe?.id === id) {
        setSelectedRecipe(updatedRecipe);
      }
      return response.data;
    } catch (err: any) {
      const errorMessage =
        err.response?.data?.error?.message || "Error updating recipe";
      setError(errorMessage);
      console.error(errorMessage);
      return null;
    } finally {
      setLoading(false);
    }
  };
  const deleteRecipe = async (id: number) => {
    setLoading(true);
    setError(null);
    try {
      await recipeAPI.deleteRecipe(id);
      setRecipes(recipes.filter((recipe) => recipe.id !== id));
      if (selectedRecipe?.id === id) {
        setSelectedRecipe(undefined);
      }
    } catch (err: any) {
      const errorMessage = err.response?.data?.error || "Error deleting recipe";
      setError(errorMessage);
      console.error(errorMessage);
    } finally {
      setLoading(false);
    }
  };

  const addIngredient = async (
    recipeId: number,
    itemId: number
  ): Promise<Recipe | null> => {
    setLoading(true);
    setError(null);
    try {
      const response = await recipeAPI.addIngredient(recipeId, itemId);
      const updatedRecipe = response.data;

      setRecipes((prev) =>
        prev.map((recipe) => (recipe.id === recipeId ? updatedRecipe : recipe))
      );

      // Update selectedRecipe if it's the one being updated
      if (selectedRecipe?.id === recipeId) {
        setSelectedRecipe(updatedRecipe);
      }

      return updatedRecipe;
    } catch (err: any) {
      const errorMessage =
        err.response?.data?.error?.message ||
        "Error adding ingredient to recipe";
      setError(errorMessage);
      console.error(errorMessage);
      return null;
    } finally {
      setLoading(false);
    }
  };

  const removeIngredient = async (
    recipeId: number,
    itemId: number
  ): Promise<Recipe | null> => {
    setLoading(true);
    setError(null);
    try {
      const response = await recipeAPI.removeIngredient(recipeId, itemId);
      const updatedRecipe = response.data;

      setRecipes((prev) =>
        prev.map((recipe) => (recipe.id === recipeId ? updatedRecipe : recipe))
      );

      if (selectedRecipe?.id === recipeId) {
        setSelectedRecipe(updatedRecipe);
      }

      return updatedRecipe;
    } catch (err: any) {
      const errorMessage =
        err.response?.data?.error?.message ||
        "Error removing ingredient from recipe";
      setError(errorMessage);
      console.error(errorMessage);
      return null;
    } finally {
      setLoading(false);
    }
  };

  const setIngredients = async (
    recipeId: number,
    itemIds: number[]
  ): Promise<Recipe | null> => {
    setLoading(true);
    setError(null);
    try {
      const response = await recipeAPI.setIngredients(recipeId, itemIds);
      const updatedRecipe = response.data;

      setRecipes((prev) =>
        prev.map((recipe) => (recipe.id === recipeId ? updatedRecipe : recipe))
      );

      if (selectedRecipe?.id === recipeId) {
        setSelectedRecipe(updatedRecipe);
      }

      return updatedRecipe;
    } catch (err: any) {
      const errorMessage =
        err.response?.data?.error?.message ||
        "Error setting recipe ingredients";
      setError(errorMessage);
      console.error(errorMessage);
      return null;
    } finally {
      setLoading(false);
    }
  };

  const findByIngredients = async (itemIds: number[]): Promise<Recipe[]> => {
    setLoading(true);
    setError(null);
    try {
      const response = await recipeAPI.findByIngredients(itemIds);
      return response.data || [];
    } catch (err: any) {
      const errorMessage =
        err.response?.data?.error?.message ||
        "Error finding recipes by ingredients";
      setError(errorMessage);
      console.error(errorMessage);
      return [];
    } finally {
      setLoading(false);
    }
  };

  const contextValue = {
    recipes,
    selectedRecipe,
    error,
    loading,
    getRecipes,
    createRecipe,
    getRecipeById,
    updateRecipe,
    updateRecipePartially,
    deleteRecipe,
    addIngredient,
    removeIngredient,
    setIngredients,
    findByIngredients,
    setSelectedRecipe,
  };

  return (
    <RecipesContext.Provider value={contextValue}>
      {children}
    </RecipesContext.Provider>
  );
}

export function useRecipes(): RecipesContextType {
  const context = useContext(RecipesContext);
  if (!context) {
    throw new Error("useRecipes must be used within a RecipesProvider");
  }
  return context;
}
