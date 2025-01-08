"use client";

import Header from "../../components/Header";
import { usePokemonData } from "../../context/usePokemonData";

export default function Home() {
  const { pokemonList } = usePokemonData();
  return (
    <div>
      <Header/>

      <div>
        <h1>Pokémon List</h1>
      </div>

    </div> 
  );
}
