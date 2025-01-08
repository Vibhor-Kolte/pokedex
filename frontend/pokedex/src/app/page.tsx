"use client";

import PokemonCard from "@/components/PokemonCard";
import Header from "../components/Header";
import { useGlobalContext } from "@/context/globalContext";


export default function Home() {
  const { pokemonListDetails, loading } = useGlobalContext();

  return (
    <main>
      <Header/>

      <section className="min-h-[91vh]">
        <div className="px-16 py-8 grid gap-6 grid-cols-1 md:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4">
          {!loading &&
            pokemonListDetails.map((pokemon: any, index: number) => {
              return <PokemonCard key={index} pokemon={pokemon} />;
            })}
        </div>
      </section>
    </main> 
  );
}
