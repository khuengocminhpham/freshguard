import { StrictMode } from "react";
import { createRoot } from "react-dom/client";
import "./index.css";
import Home from "./pages/Home.tsx";
import { BrowserRouter, Route, Routes } from "react-router";
import { Item } from "./pages/Item.tsx";
import { Recipe } from "./pages/Recipe.tsx";
import { ItemsProvider } from "./context/ItemsContext.tsx";
import { RecipesProvider } from "./context/RecipesContext.tsx";

createRoot(document.getElementById("root")!).render(
  <StrictMode>
    <RecipesProvider>
      <ItemsProvider>
        <BrowserRouter>
          <Routes>
            <Route path="/" element={<Home />} />
            <Route path="/item" element={<Item />} />
            <Route path="/recipe" element={<Recipe />} />
          </Routes>
        </BrowserRouter>
      </ItemsProvider>
    </RecipesProvider>
  </StrictMode>
);
