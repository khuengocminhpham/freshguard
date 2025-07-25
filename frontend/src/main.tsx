import { StrictMode } from "react";
import { createRoot } from "react-dom/client";
import "./index.css";
import App from "./App.tsx";
import { BrowserRouter, Route, Routes } from "react-router";
import { Item } from "./pages/Item.tsx";
import { Recipe } from "./pages/Recipe.tsx";
import { ItemsProvider } from "./context/ItemsContext.tsx";

createRoot(document.getElementById("root")!).render(
  <StrictMode>
    <ItemsProvider>
      <BrowserRouter>
        <Routes>
          <Route path="/" element={<App />} />
          <Route path="/item" element={<Item />} />
          <Route path="/recipe" element={<Recipe />} />
        </Routes>
      </BrowserRouter>
    </ItemsProvider>
  </StrictMode>
);
