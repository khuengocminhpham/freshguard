import { ItemCard } from "../components/ItemCard";

export const Item = () => {
  return (
    <div className="p-20">
      <h1 className="mb-20 text-4xl font-extrabold tracking-tight leading-none text-primary-900 md:text-5xl lg:text-6xl">
        My inventory
      </h1>

      <div className="flex flex-wrap content-center-safe gap-8 justify-between">
        <ItemCard />
        <ItemCard />
        <ItemCard />
        <ItemCard />
        <ItemCard />
        <ItemCard />
        <ItemCard />
      </div>
    </div>
  );
};
