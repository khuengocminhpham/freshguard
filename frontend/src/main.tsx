import { StrictMode } from "react";
import { createRoot } from "react-dom/client";
import "./index.css";
import Home from "./pages/Home.tsx";
import { BrowserRouter, Route, Routes } from "react-router";
import { Items } from "./pages/Items.tsx";
import { Recipes } from "./pages/Recipes.tsx";
import { ItemsProvider } from "./context/ItemsContext.tsx";
import { RecipesProvider } from "./context/RecipesContext.tsx";
import { Recipe } from "./pages/Recipe.tsx";
import { Nav } from "./components/Nav.tsx";

createRoot(document.getElementById("root")!).render(
  <StrictMode>
    <RecipesProvider>
      <ItemsProvider>
        <BrowserRouter>
          <Nav />
          <Routes>
            <Route path="/" element={<Home />} />
            <Route path="/items" element={<Items />} />
            <Route path="/recipes" element={<Recipes />} />
            <Route path="/recipes/:recipeId" element={<Recipe />} />
          </Routes>
        </BrowserRouter>
      </ItemsProvider>
    </RecipesProvider>
  </StrictMode>
);
