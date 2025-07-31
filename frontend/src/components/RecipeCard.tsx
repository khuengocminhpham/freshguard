import { useState } from "react";
import type { Recipe } from "../services/api";
import { RecipeForm } from "./RecipeForm";
import { useRecipes } from "../context/RecipesContext";

type Props = {
  data: Recipe;
  itemView: boolean;
};

export const RecipeCard = ({ data, itemView }: Props) => {
  const [isOpen, setIsOpen] = useState(false);
  const [isSubmitting, setIsSubmitting] = useState(false);
  const { deleteRecipe, loading } = useRecipes();

  const handleDelete = async () => {
    if (!data?.id) return;

    const confirmDelete = window.confirm(
      "Are you sure you want to delete this recipe?"
    );
    if (!confirmDelete) return;

    setIsSubmitting(true);
    try {
      await deleteRecipe(data.id);
    } catch (error) {
      console.error("Error deleting recipe:", error);
    } finally {
      setIsSubmitting(false);
    }
  };
  return (
    <div>
      <a
        href="#"
        className="block max-w-sm p-6 bg-white border border-gray-200 rounded-lg shadow-sm hover:bg-gray-100 "
      >
        <div className="mb-2">
          <h5 className="text-2xl font-bold tracking-tight text-primary-700">
            {data.name}
          </h5>
          <p className="font-normal text-gray-400">{data.description}</p>
        </div>

        <div className="mb-4">
          <span className="mr-4 bg-blue-100 text-blue-800 text-xs font-medium inline-flex items-center px-2.5 py-0.5 rounded-sm border border-blue-400">
            <svg
              className="w-3 h-3 me-1.5"
              aria-hidden="true"
              xmlns="http://www.w3.org/2000/svg"
              width="24"
              height="24"
              fill="none"
              viewBox="0 0 20 20"
            >
              <path
                fill="currentColor"
                d="M8.9884 3.21891c3.0076.24714 5.945 1.51873 8.2417 3.8154 2.269 2.26907 3.5376 5.16369 3.806 8.13349.1181 1.3063-.817 2.3512-1.9748 2.5974-.3781.0805-.7426.0766-1.0826.0053-.0526-.9449-.1322-1.9863-.2259-2.7243-.2666-2.0994-1.0578-3.9824-2.805-5.72956-1.7428-1.74283-3.6864-2.60074-5.82006-2.9026-.44367-.06276-1.6162-.18893-2.69208-.26801-.06233-.34562-.05329-.7147.04516-1.0954.2799-1.0824 1.27852-1.93272 2.50758-1.83172Z"
              />
              <path
                fill="currentColor"
                fillRule="evenodd"
                d="M15.7687 15.2981c.0789.6213.1481 1.4924.1977 2.3181L4.6757 20.8741c-.34909.1007-.72534.0042-.98281-.2521-.25747-.2564-.35564-.6322-.25642-.9817L6.69176 8.17203c.89043.07337 1.79027.17058 2.15583.2223 1.73781.24585 3.27301.92348 4.68601 2.33657 1.4054 1.4054 2.0192 2.8665 2.2351 4.5672Zm-4.3548-4.5789c-.0238-.5518-.4904-.97977-1.0422-.95597-.55178.02381-.97978.49037-.95598 1.04217l.00047.0108c.0238.5518.4904.9798 1.04221.956.5517-.0238.9797-.4904.9559-1.0422l-.0004-.0108Zm3.0873 3.0873c-.0238-.5518-.4904-.9798-1.0422-.956-.5518.0238-.9798.4904-.956 1.0422l.0005.0108c.0238.5518.4903.9798 1.0421.956.5518-.0238.9798-.4904.956-1.0421l-.0004-.0109Zm-4.58671 1.4994c-.02377-.5517-.49034-.9798-1.04211-.956-.55177.0238-.9798.4903-.95603 1.0421l.00046.0108c.02377.5518.49034.9798 1.04211.9561.55178-.0238.97981-.4904.95604-1.0421l-.00047-.0109Z"
                clipRule="evenodd"
              />
            </svg>
            Servings: {data.servings}
          </span>
          <span className="bg-yellow-100 text-yellow-800 text-xs font-medium inline-flex items-center px-2.5 py-0.5 rounded-sm border border-yellow-400">
            <svg
              className="w-3 h-3 me-1.5"
              aria-hidden="true"
              xmlns="http://www.w3.org/2000/svg"
              fill="currentColor"
              viewBox="0 0 20 20"
            >
              <path d="M10 0a10 10 0 1 0 10 10A10.011 10.011 0 0 0 10 0Zm3.982 13.982a1 1 0 0 1-1.414 0l-3.274-3.274A1.012 1.012 0 0 1 9 10V6a1 1 0 0 1 2 0v3.586l2.982 2.982a1 1 0 0 1 0 1.414Z" />
            </svg>
            Prep time: {data.prepTimeMinutes} minutes
          </span>
        </div>
        {!itemView && (
          <p className="font-normal text-primary-700 mb-3 ml-0.5">
            {data.instructions.substring(0, 99)}...
          </p>
        )}
        {data.ingredients && !itemView && (
          <div className="mb-5">
            {data.ingredients.map((item) => (
              <span
                key={item.id}
                className="bg-gray-200 text-gray-800 border border-gray-400 text-xs font-medium me-2 px-2.5 py-0.5 rounded-full"
              >
                {item.name}
              </span>
            ))}
          </div>
        )}

        {!itemView && (
          <div>
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
              onClick={handleDelete}
              disabled={isSubmitting || loading}
              type="button"
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
          </div>
        )}
        {isOpen && (
          <RecipeForm
            recipe={data}
            isOpen={isOpen}
            onClose={() => setIsOpen(false)}
            create={false}
          />
        )}
      </a>
    </div>
  );
};
