import { createContext, useContext, useState } from "react";
import { itemAPI, type Item } from "../services/api";
export type ItemsContextType = {
  items: Item[];
  selectedItem: Item | undefined;
  loading: boolean;
  error: string | null;
  createItem: (item: Omit<Item, "id">) => Promise<Omit<Item, "id"> | never[]>;
  getItems: () => Promise<Item[]>;
  getItemById: (id: number) => Promise<Item | null>;
  updateItem: (id: number, item: Omit<Item, "id">) => Promise<Item | null>;
  updateItemPartially: (
    id: number,
    item: Partial<Omit<Item, "id">>
  ) => Promise<Item | null>;
  deleteItem: (id: number) => Promise<void>;
  getItemRecipes: (id: number) => Promise<void>;
};

const ItemsContext = createContext<ItemsContextType | null>(null);

export function ItemsProvider({ children }: any) {
  const [items, setItems] = useState<Item[]>([]);
  const [selectedItem, setSelectedItem] = useState<Item>();
  const [error, setError] = useState(null);
  const [loading, setLoading] = useState(false);

  const getItems = async () => {
    setLoading(true);
    setError(null);
    try {
      const response = await itemAPI.getItems();
      setItems(response.data.reverse() || []);
      return response.data || [];
    } catch (err: any) {
      const errorMessage =
        err.response?.data?.error?.message || "Error fetching items";
      setError(errorMessage);
      console.error(errorMessage);
      return [];
    } finally {
      setLoading(false);
    }
  };

  const createItem = async (item: Omit<Item, "id">) => {
    setLoading(true);
    setError(null);
    try {
      const response = await itemAPI.createItem(item);
      const newItem = response.data;
      setItems((prevItems) => [newItem, ...prevItems]);
      return newItem;
    } catch (err: any) {
      const errorMessage =
        err.response?.data?.error?.message || "Error creating item";
      setError(errorMessage);
      console.error(errorMessage);
      return [];
    } finally {
      setLoading(false);
    }
  };

  const getItemById = async (id: number) => {
    setLoading(true);
    setError(null);
    try {
      const response = await itemAPI.getItemById(id);
      setSelectedItem(response.data);
      return response.data;
    } catch (err: any) {
      const errorMessage =
        err.response?.data?.error?.message || "Error getting selected item";
      setError(errorMessage);
      console.error(errorMessage);
      return null;
    } finally {
      setLoading(false);
    }
  };

  const updateItem = async (id: number, item: Omit<Item, "id">) => {
    setLoading(true);
    setError(null);
    try {
      const response = await itemAPI.updateItem(id, item);
      const updatedItem = response.data;
      setItems(items.map((item) => (item.id === id ? updatedItem : item)));
      return response.data;
    } catch (err: any) {
      const errorMessage =
        err.response?.data?.error?.message || "Error updating item";
      setError(errorMessage);
      console.error(errorMessage);
      return null;
    } finally {
      setLoading(false);
    }
  };

  const updateItemPartially = async (
    id: number,
    item: Partial<Omit<Item, "id">>
  ) => {
    setLoading(true);
    setError(null);
    try {
      const response = await itemAPI.updateItemPartially(id, item);
      const updatedItem = response.data;
      setItems(items.map((item) => (item.id === id ? updatedItem : item)));
      return response.data;
    } catch (err: any) {
      const errorMessage =
        err.response?.data?.error?.message || "Error updating item";
      setError(errorMessage);
      console.error(errorMessage);
      return null;
    } finally {
      setLoading(false);
    }
  };
  const deleteItem = async (id: number) => {
    setLoading(true);
    setError(null);
    try {
      await itemAPI.deleteItem(id);
      setItems(items.filter((item) => item.id !== id));
    } catch (err: any) {
      const errorMessage = err.response?.data?.error || "Error deleting item";
      setError(errorMessage);
      console.error(errorMessage);
    } finally {
      setLoading(false);
    }
  };

  const getItemRecipes = async (id: number) => {
    setLoading(true);
    setError(null);
    try {
      const response = await itemAPI.getItemRecipes(id);
      const recipes = await response.data;
      setItems(
        items.map((item) => (item.id === id ? { ...item, recipes } : item))
      );
      setSelectedItem((prevItem) => {
        if (prevItem && prevItem.id === id) {
          return { ...prevItem, recipes };
        }

        const itemFromArray = items.find((item) => item.id === id);
        if (itemFromArray) {
          return { ...itemFromArray, recipes };
        }
        return prevItem;
      });
    } catch (err: any) {
      const errorMessage =
        err.response?.data?.error || "Error fetching item's recipes";
      setError(errorMessage);
      console.error(errorMessage);
    } finally {
      setLoading(false);
    }
  };

  const contextValue = {
    items,
    selectedItem,
    loading,
    error,
    createItem,
    getItems,
    getItemById,
    updateItem,
    updateItemPartially,
    deleteItem,
    getItemRecipes,
  };
  return (
    <ItemsContext.Provider value={contextValue}>
      {children}
    </ItemsContext.Provider>
  );
}

export const useItems = (): ItemsContextType => {
  const context = useContext(ItemsContext);
  if (!context) {
    throw new Error("useItems must be used within an ItemsProvider");
  }
  return context;
};
