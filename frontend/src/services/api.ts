import axios from "axios";
import type { AxiosResponse } from "axios";

const API_BASE_URL = "http://localhost:8080/api";

// Define interfaces for your data models
export interface Item {
  id?: number;
  name?: string;
  description?: string;
  // Add other item properties as needed
}

export interface Recipe {
  id?: number;
  name?: string;
  description?: string;
  ingredients?: Item[];
  // Add other recipe properties as needed
}

const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    "Content-Type": "application/json",
  },
});

export const itemAPI = {
  createItem: (item: Omit<Item, "id">): Promise<AxiosResponse<Item>> =>
    api.post("/items", item),
  getItems: (): Promise<AxiosResponse<Item[]>> => api.get("/items"),
  getItemById: (id: number): Promise<AxiosResponse<Item>> =>
    api.get(`/items/${id}`),
  updateItem: (
    id: number,
    item: Omit<Item, "id">
  ): Promise<AxiosResponse<Item>> => api.put(`/items/${id}`, item),
  updateItemPartially: (
    id: number,
    item: Partial<Omit<Item, "id">>
  ): Promise<AxiosResponse<Item>> => api.patch(`/items/${id}`, item),
  deleteItem: (id: number): Promise<AxiosResponse<void>> =>
    api.delete(`/items/${id}`),
  getItemRecipes: (id: number): Promise<AxiosResponse<Recipe[]>> =>
    api.get(`/items/${id}/recipes`),
};

export const recipeAPI = {
  createRecipe: (recipe: Omit<Recipe, "id">): Promise<AxiosResponse<Recipe>> =>
    api.post("/recipes", recipe),
  getRecipes: (): Promise<AxiosResponse<Recipe[]>> => api.get("/recipes"),
  getRecipeById: (id: number): Promise<AxiosResponse<Recipe>> =>
    api.get(`/recipes/${id}`),
  updateRecipe: (
    id: number,
    recipe: Omit<Recipe, "id">
  ): Promise<AxiosResponse<Recipe>> => api.put(`/recipes/${id}`, recipe),
  updateRecipePartially: (
    id: number,
    recipe: Partial<Omit<Recipe, "id">>
  ): Promise<AxiosResponse<Recipe>> => api.patch(`/recipes/${id}`, recipe),
  deleteRecipe: (id: number): Promise<AxiosResponse<void>> =>
    api.delete(`/recipes/${id}`),
  addIngredient: (
    recipeId: number,
    itemId: number
  ): Promise<AxiosResponse<Recipe>> =>
    api.post(`/recipes/${recipeId}/ingredients/${itemId}`),
  removeIngredient: (
    recipeId: number,
    itemId: number
  ): Promise<AxiosResponse<Recipe>> =>
    api.delete(`/recipes/${recipeId}/ingredients/${itemId}`),
  setIngredients: (
    recipeId: number,
    itemIds: number[]
  ): Promise<AxiosResponse<Recipe>> =>
    api.put(`/recipes/${recipeId}/ingredients`, itemIds),
  findByIngredients: (itemIds: number[]): Promise<AxiosResponse<Recipe[]>> =>
    api.get("/recipes/find-by-ingredients", { params: { itemIds } }),
};

export default api;
