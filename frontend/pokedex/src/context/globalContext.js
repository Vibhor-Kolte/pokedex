import React from "react";
import { usePokemonData } from "./usePokemonData";
const GloablContext = React.createContext();

export const GlobalContextProvider = ({ children }) => {
  const {
    loading,
    fetchPokemon,
    pokemonList,
    pokemonListDetails,
    fetchPokemonByName,
    activePokemon,
    searchQuery, handleSearchChange,
  } = usePokemonData();

  return (
    <GloablContext.Provider
      value={{
        loading,
        fetchPokemon,
        pokemonList,
        pokemonListDetails,
        fetchPokemonByName,
        activePokemon,
        searchQuery, handleSearchChange,
      }}
    >
      {children}
    </GloablContext.Provider>
  );
};

export const useGlobalContext = () => {
  const context = React.useContext(GloablContext);

  // Add log to check the context value
  console.log('Context in useGlobalContext:', context);

  if (!context) {
    throw new Error("useGlobalContext must be used within a GlobalContextProvider");
  }
  return context;
};