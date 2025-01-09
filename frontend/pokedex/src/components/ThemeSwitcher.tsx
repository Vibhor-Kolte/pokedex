import { useTheme } from "@/context/themeContext";
import { FaMoon, FaRegMoon } from "react-icons/fa";

const ThemeSwitcher = () => {
  const { theme, toggleTheme } = useTheme();

  return (
    <button
      onClick={toggleTheme}
      className="flex items-center justify-center p-4 rounded-full bg-gray-200 dark:bg-gray-700"
    >
      {theme === "light" ? (
        <FaRegMoon className="text-yellow-500 text-3xl" />
      ) : (
        <FaMoon className="text-white text-3xl" />
      )}
    </button>
  );
};

export default ThemeSwitcher;