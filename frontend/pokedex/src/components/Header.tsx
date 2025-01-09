"use client";

import Image from "next/image";
import Link from "next/link";
import React from "react";
import { usePathname } from "next/navigation";
import { useTheme } from "@/context/themeContext";
import ThemeSwitcher from "@/components/ThemeSwitcher";

const menu = [
  {
    name: "Pokédex",
    link: "/",
    defaultIcon: "/pokeball_gray.png",
    hoverIcon: "/pokeball_white.png"
  }
];

function Header() {

  const { theme } = useTheme();
  const pathname = usePathname();

  return (
    <header className="min-h-[10vh] px-16 py-6 w-full bg-white flex justify-between items-center shadow-sm"
      style={{
        backgroundImage: `url('${theme === "light" ? "/container_bg.png" : "/body_gray_bg.png"}')`,
        width: "100%",
      }}>

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
                    : theme === "dark"
                      ? "text-white"
                      : "text-gray-400"
                  }
                    `}
              >
                <Image
                  src={
                    pathname === item.link
                      ? item.hoverIcon // Active state icon
                      : theme === "dark" ? item.hoverIcon : item.defaultIcon // Default state icon
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

      {/* Theme Toggle */}
      <div className="flex items-center">
        <ThemeSwitcher />
      </div>
    </header>
  );
}

export default Header;
