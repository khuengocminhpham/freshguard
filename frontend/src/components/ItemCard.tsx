import { useState } from "react";
import type { Item } from "../services/api";
import { arrayToDate } from "../util/helpers";
import { ItemForm } from "./ItemForm";
import { useItems } from "../context/ItemsContext";
type Props = {
  data: Item;
};
export const ItemCard = ({ data }: Props) => {
  const [isOpen, setIsOpen] = useState(false);
  const [isSubmitting, setIsSubmitting] = useState(false);
  const { deleteItem, loading } = useItems();

  const handleDelete = async () => {
    if (!data?.id) return;

    const confirmDelete = window.confirm(
      "Are you sure you want to delete this item?"
    );
    if (!confirmDelete) return;

    setIsSubmitting(true);
    try {
      await deleteItem(data.id);
    } catch (error) {
      console.error("Error deleting item:", error);
    } finally {
      setIsSubmitting(false);
    }
  };
  return (
    <div>
      <a
        href="#"
        className="max-w-md p-6 bg-white border border-gray-200 rounded-lg shadow-sm hover:bg-gray-100 block"
      >
        <div className="flex flex-row justify-between">
          <div className="mb-2">
            <h5 className="text-2xl font-bold tracking-tight text-primary-700">
              {data.name}
            </h5>
            <p className="font-normal text-gray-400">{data.category}</p>
          </div>
          <button className="block self-start mt-2 items-center px-3 py-2 text-sm font-medium text-center text-white bg-primary-700 hover:bg-primary-800 focus:ring-4 focus:outline-none focus:ring-primary-300 rounded-lg ">
            View Recipes
          </button>
        </div>
        <div className="mb-4">
          <span className="bg-red-100 text-red-800 text-xs font-medium inline-flex items-center px-2.5 py-0.5 rounded-sm me-2 border border-red-400">
            <svg
              className="w-2.5 h-2.5 me-1.5"
              aria-hidden="true"
              xmlns="http://www.w3.org/2000/svg"
              fill="currentColor"
              viewBox="0 0 20 20"
            >
              <path d="M10 0a10 10 0 1 0 10 10A10.011 10.011 0 0 0 10 0Zm3.982 13.982a1 1 0 0 1-1.414 0l-3.274-3.274A1.012 1.012 0 0 1 9 10V6a1 1 0 0 1 2 0v3.586l2.982 2.982a1 1 0 0 1 0 1.414Z" />
            </svg>
            Expired by: {arrayToDate(data.expirationDate)}
          </span>
          <span className="bg-gray-100 text-gray-800 text-xs font-medium inline-flex items-center px-2.5 py-0.5 rounded-sm me-2 border border-gray-500">
            <svg
              className="w-2.5 h-2.5 me-1.5"
              aria-hidden="true"
              xmlns="http://www.w3.org/2000/svg"
              fill="currentColor"
              viewBox="0 0 20 20"
            >
              <path d="M10 0a10 10 0 1 0 10 10A10.011 10.011 0 0 0 10 0Zm3.982 13.982a1 1 0 0 1-1.414 0l-3.274-3.274A1.012 1.012 0 0 1 9 10V6a1 1 0 0 1 2 0v3.586l2.982 2.982a1 1 0 0 1 0 1.414Z" />
            </svg>
            Purchased on: {arrayToDate(data.purchaseDate)}
          </span>
        </div>
        <div className="mb-4">
          <p className="font-normal text-gray-700 dark:text-gray-400">
            Quantity: {data.quantity}
          </p>
          <p className="font-normal text-gray-700 dark:text-gray-400">
            Location: {data.location}
          </p>
        </div>

        <button
          onClick={() => setIsOpen(true)}
          type="button"
          className="mr-4 inline-flex items-center px-4 py-2 text-sm font-medium text-center text-white bg-primary-700 hover:bg-primary-800 focus:ring-4 focus:outline-none focus:ring-primary-300 rounded-lg "
        >
          <svg
            aria-hidden="true"
            className="mr-1 -ml-1 w-4 h-4"
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
          type="button"
          onClick={handleDelete}
          disabled={isSubmitting || loading}
          className="inline-flex items-center px-4 py-2 text-sm font-medium text-white bg-red-600 hover:bg-red-700 focus:ring-4 focus:outline-none focus:ring-red-300 rounded-lg text-center"
        >
          <svg
            aria-hidden="true"
            className="w-4 h-4 mr-1.5 -ml-1"
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
          {isSubmitting ? "Deleting..." : "Delete"}
        </button>

        {isOpen && (
          <ItemForm
            item={data}
            isOpen={isOpen}
            onClose={() => setIsOpen(false)}
            create={false}
          />
        )}
      </a>
    </div>
  );
};
