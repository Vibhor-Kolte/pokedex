"use client";

import Image from "next/image";
import Link from "next/link";
import React from "react";
import { usePathname } from "next/navigation";

const menu = [
  {
    name: "Pokédex",
    link: "/",
    defaultIcon: "/pokeball_gray.png",
    hoverIcon: "/pokeball_white.png"
  }
];

function Header() {

  const pathname = usePathname();

  return (
    <header className="min-h-[10vh] px-16 py-6 w-full bg-white flex justify-between items-center shadow-sm">
      {/* Logo */}
      <Link href="/">
        <Image src={"/pokemon--logo.png"} width={120} height={90} alt="logo" />
      </Link>

      {/* Navigation Menu */}
      <nav className="flex justify-center items-center w-full">
        <ul className="flex items-center gap-8 text-gray-400">
          {menu.map((item, index: number) => (
            <li key={index}>
              <Link
                href={item.link}
                className={`group py-2 px-6 text-sm flex flex-col items-center gap-2 font-bold rounded-lg transition-all duration-300
                        ${pathname === item.link
                    ? "bg-orange-500 text-white"
                    : "text-gray-400"
                  }
                    `}
              >
                {/* Dynamic Icon */}
                <Image
                  src={
                    pathname === item.link
                      ? item.hoverIcon // Active state icon
                      : item.defaultIcon // Default state icon
                  }
                  width={40}
                  height={40}
                  alt={`${item.name} icon`}
                  className="transition-transform duration-300 group-hover:scale-110"
                />
                {/* Menu Name */}
                <span className={`transition-colors duration-300`}>
                  {item.name}
                </span>
              </Link>
            </li>
          ))}
        </ul>
      </nav>
    </header>
  );
}

export default Header;
