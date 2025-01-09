import React from "react";
import { usePokemonData } from "./usePokemonData";
const GloablContext = React.createContext();
import log from 'loglevel';

log.setLevel('info');

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
  log.debug('Context in useGlobalContext:', context);

  if (!context) {
    throw new Error("useGlobalContext must be used within a GlobalContextProvider");
  }
  return context;
};