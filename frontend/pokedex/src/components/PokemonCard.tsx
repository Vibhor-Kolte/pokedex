/* eslint-disable @typescript-eslint/no-explicit-any */

"use client";

import { useTheme } from "@/context/themeContext";
import { typeColor } from "@/utils/colors";
import { FaAngleRight } from "react-icons/fa";
import Image from "next/image";
import { useRouter } from "next/navigation";
import React from "react";

interface PokemonCardProps {
  pokemon: any;
}

function PokemonCard({ pokemon }: PokemonCardProps) {
  const router = useRouter();
  const { theme } = useTheme();

  return (
    <div className="relative p-2 bg-white rounded-xl shadow-sm flex flex-col gap-2"
      style={{
        backgroundImage: `url('${theme === "dark" ? "/container_bg.png" : "none"}')`,
        width: "100%",
      }}>
      <div className="flex justify-between items-center">

        <button
          className="p-2 w-10 h-10 text-xl ml-1.5 mt-1.5 flex items-center justify-center rounded-full border-2 text-gray-300 border-gray-300
        hover:bg-[#00b894] hover:border-transparent hover:text-white transition-all duration-300 ease-in-out"
          onClick={() => router.push(`/pokemon/${pokemon?.name}`)}
        >
          <FaAngleRight />
        </button>
      </div>
      <div className="flex gap-4">
        <div className="flex-1">
          <Image
            src={
              pokemon?.sprites?.other?.home?.front_default ||
              pokemon?.sprites?.front_default
            }
            alt="pokemon image"
            width={200}
            height={200}
            className="object-contain"
          />
        </div>
        <div className="flex-1 flex flex-col items-center justify-center gap-4">
          <div className="mb-2 flex gap-2">
            <p className="text-xl uppercase font-semibold text-gray-500">
              #{pokemon?.id}
            </p>
            {/* <p className="text-xs uppercase font-semibold text-gray-500">
              {pokemon?.height} m,
            </p>
            <p className="text-xs uppercase font-semibold text-gray-500">
              {pokemon?.weight} kg,
            </p>
            <p className="text-xs uppercase font-semibold text-gray-500">
              {pokemon?.base_experience} xp
            </p> */}
          </div>
          <h2 className="text-2xl text-gray-800 capitalize font-bold text-center">
            {pokemon?.name}
          </h2>

          <div className="flex justify-center gap-2">
            {pokemon?.types?.map((type: any, index: number) => (
              <p
                key={index}
                className="text-xs uppercase font-semibold text-white px-5 py-1 rounded-full"
                style={{ backgroundColor: typeColor[type?.type?.name] }}
              >
                {type.type.name}
              </p>
            ))}
          </div>
        </div>
      </div>
    </div>
  );
}

export default PokemonCard;
