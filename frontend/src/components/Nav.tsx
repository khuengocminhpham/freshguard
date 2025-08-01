import React, { useState } from "react";
import { NavLink } from "react-router";

export const Nav: React.FC = () => {
  const [open, setOpen] = useState(false);

  const linkClasses = ({ isActive }: { isActive: boolean }) =>
    [
      "block px-4 py-2 rounded-md text-base font-medium text-primary-700",
      isActive ? "underline" : "hover:underline",
    ].join(" ");

  return (
    <header className="bg-white border-b border-gray-200">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="flex justify-between h-16 items-center">
          <div className="flex-shrink-0">
            <NavLink to="/" className="text-2xl font-bold text-primary-700">
              Fresh Guard
            </NavLink>
          </div>

          {/* Desktop links */}
          <nav className="hidden text-2xl sm:flex space-x-2">
            <NavLink to="/" className={linkClasses}>
              Home
            </NavLink>
            <NavLink to="/items" className={linkClasses}>
              Items
            </NavLink>
            <NavLink to="/recipes" className={linkClasses}>
              Recipes
            </NavLink>
          </nav>

          {/* Mobile toggle */}
          <div className="sm:hidden">
            <button
              aria-label="Toggle menu"
              onClick={() => setOpen((o) => !o)}
              className="inline-flex items-center justify-center p-2 rounded-md text-gray-700 hover:bg-gray-100 focus:outline-none focus:ring-2 focus:ring-gray-400"
            >
              <svg
                className="w-6 h-6"
                aria-hidden="true"
                fill="none"
                stroke="currentColor"
                viewBox="0 0 24 24"
              >
                {open ? (
                  <path
                    strokeLinecap="round"
                    strokeLinejoin="round"
                    strokeWidth={2}
                    d="M6 18L18 6M6 6l12 12"
                  />
                ) : (
                  <path
                    strokeLinecap="round"
                    strokeLinejoin="round"
                    strokeWidth={2}
                    d="M4 6h16M4 12h16M4 18h16"
                  />
                )}
              </svg>
            </button>
          </div>
        </div>

        {/* Mobile menu */}
        {open && (
          <div className="sm:hidden mt-2">
            <nav className="space-y-1">
              <NavLink
                to="/"
                className={linkClasses}
                onClick={() => setOpen(false)}
              >
                Home
              </NavLink>
              <NavLink
                to="/items"
                className={linkClasses}
                onClick={() => setOpen(false)}
              >
                Items
              </NavLink>
              <NavLink
                to="/recipes"
                className={linkClasses}
                onClick={() => setOpen(false)}
              >
                Recipes
              </NavLink>
            </nav>
          </div>
        )}
      </div>
    </header>
  );
};
