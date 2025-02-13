import axios from "axios";
import { useEffect, useState } from "react";
import _ from "lodash";
import log from 'loglevel';

const pokedexBaseUrl = process.env.NEXT_PUBLIC_POKEDEX_BACKEND_BASE_URL;
log.setLevel('info');

export const usePokemonData = () => {
  const [loading, setLoading] = useState(false);
  const [currentPage, setCurrentPage] = useState(1);

  const [pokemonList, setPokemonList] = useState([]);
  const [allPokemon, setAllPokemon] = useState([]);
  const [pokemonListDetails, setPokemonListDetails] = useState([]);
  const [originalPokemonListDetails, setOriginalPokemonListDetails] = useState(
    []
  );

  const [activePokemon, setActivePokemon] = useState(null);

  const [searchQuery, setSearchQuery] = useState("");

  const [filters, setFilters] = useState({
    type: "",
    ability: "",
    weight: "",
    height: "",
    sortOrder: "",
  });  

  // fetch initial pokemons
  const fetchPokemon = async (page = 1) => {
    setLoading(true);
    try {
      const offset = (page - 1) * 50;
      const res = await axios.get(
        `${pokedexBaseUrl}/api/pokemon?offset=${offset}&limit=40`
      );

      log.debug('API response for fetchInitialPokemons:- ', res.data);

      setLoading(false);
      setPokemonList((prev) => [...prev, ...res.data]);
      setCurrentPage(page);
    } catch (error) {
      console.error(error);
    }
  };

  // fetch all pokemons
  const fetchAllPokemon = async () => {
    try {
      const res = await axios.get(`${pokedexBaseUrl}/api/pokemon?limit=1118`);
      log.debug('API response for fetchAllPokemons:- ', res.data);
      setAllPokemon(res.data);
    } catch (error) {
      console.error(error);
    }
  };

  // fetch pokemon details for initial pokemons list
  const fetchPokemonDetails = async () => {
    setLoading(true);
    try {
      const details = await Promise.all(
        pokemonList.map(async (pokemon) => {
          const res = await axios.get(`${pokedexBaseUrl}/api/pokemon/${pokemon.name}`);
          log.debug('API response for fetchPokemonDetails:- ', res.data);
          return res.data;
        })
      );

      setLoading(false);
      setPokemonListDetails(details);
      // preserve the original list
      setOriginalPokemonListDetails(details);
    } catch (error) {
      console.log("Error fetching pokemon details", error);
    }
  };

  // fecth pokemon by name
  const fetchPokemonByName = async (name) => {
    setLoading(true);
    try {
      const res = await axios.get(`${pokedexBaseUrl}/api/pokemon/${name}`);

      setLoading(false);
      log.debug('API response for fetchPokemonByName:- ', res.data);
      setActivePokemon(res.data);
      return res.data;
    } catch (error) {
      console.error("Error fetching pokemon by name", error);
    }
  };

  // search pokemon
  const searchPokemon = async (query) => {
    if (!query) {
      setSearchQuery("");

      const details = await Promise.all(
        pokemonList.map(async (pokemon) => {
          const res = await axios.get(pokemon.url);

          return res.data;
        })
      );

      setPokemonListDetails(details);
      return;
    }

    setLoading(true);

    const filteredPokemon = allPokemon.filter((pokemon) => {
      return pokemon.name.toLowerCase().includes(query.toLowerCase());
    });

    try {
      // fetch details for the filtered pokemon
      const filtered = await Promise.all(
        filteredPokemon.map(async (pokemon) => {
          const res = await axios.get(pokemon.url);
          return res.data;
        })
      );

      setLoading(false);

      setPokemonListDetails(filtered);
    } catch (error) {
      console.error("Error searching pokemon", error);
    }
  };

  // handle change for search
  const handleSearchChange = (e) => {
    log.debug('Search query:- ', e.target.value);
    const value = e.target.value;
    setSearchQuery(value);
    debouncedSearch(value);
  };

  // debounce search(using lodash)
  const debouncedSearch = _.debounce((value) => {
    setFilters((prev) => ({ ...prev, query: value }));
    filterPokemon();
    searchPokemon(value);
  }, 500);

  //filter pokemon
  const filterPokemon = () => {
    const { type, ability, weight, height, sortOrder } = filters;
    const query = searchQuery.toLowerCase();

    let filteredPokemon = originalPokemonListDetails;

    if (type) {
      filteredPokemon = filteredPokemon.filter((pokemon) => {
        return pokemon.types.some((t) => t.type.name === type);
      });
    }

    if (ability) {
      filteredPokemon = filteredPokemon.filter((pokemon) => {
        return pokemon.abilities.some((a) => a.ability.name === ability);
      });
    }

    if (weight) {
      filteredPokemon = filteredPokemon.filter((pokemon) => {
        return pokemon.weight >= weight;
      });
    }

    if (height) {
      filteredPokemon = filteredPokemon.filter((pokemon) => {
        return pokemon.height >= height;
      });
    }

    if (query) {
      filteredPokemon = filteredPokemon.filter((pokemon) => {
        return pokemon.name.toLowerCase().includes(query);
      });
    }

    if (sortOrder) {
      filteredPokemon =
        sortOrder === "asc"
          ? [...filteredPokemon].sort((a, b) => {
              return a.name.localeCompare(b.name, undefined, {
                sensitivity: "base",
              });
            })
          : [...filteredPokemon].sort((a, b) => {
              return b.name.localeCompare(a.name, undefined, {
                sensitivity: "base",
              });
            });
    }

    setPokemonListDetails(filteredPokemon);
  };

  // handle change for filters
  const handleFilterChange = (key, value) => {
    log.debug(`Filter key: ${key}, value: ${value}`);
    setFilters((prev) => ({ ...prev, [key]: value }));
  };

  // clear filters
  const clearFilters = () => {
    setFilters({
      type: "",
      ability: "",
      weight: "",
      height: "",
      sortOrder: "",
    });
    setSearchQuery("");
    setPokemonListDetails(originalPokemonListDetails);
  };

  // load more pokemons as user asks
  const loadMore = () => {
    fetchPokemon(currentPage + 1);
  };

  // -------------------Use Effects-------------------
  // Initial Renders
  useEffect(() => {
    log.debug("Fetching initial Pokémon data...");
    fetchPokemon(); // Fetch initial Pokémon data

    log.debug("Fetching all Pokémon data...");
    fetchAllPokemon(); // Fetch the full list of Pokémon
  }, []);

  useEffect(() => {
    if (pokemonList.length > 0) {
      fetchPokemonDetails();
    }
  }, [pokemonList]);

  // Filter Pokémon based on filters
  useEffect(() => {
    filterPokemon();
  }, [filters, searchQuery]);

  return {
    loading,
    fetchPokemon, pokemonList, pokemonListDetails,
    fetchPokemonByName, activePokemon,
    searchQuery, handleSearchChange,
    handleFilterChange, filters, clearFilters,
    loadMore,
  };
};