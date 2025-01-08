export type Pokemon = {
    name: string;
    url: string;
  };
  
  export type PokemonDetail = {
    id: number;
    name: string;
    height: number;
    weight: number;
    types: string[];
    // Add other fields as per your data structure
  };