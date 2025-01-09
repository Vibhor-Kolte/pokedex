import type { Metadata } from "next";
import { Geist, Geist_Mono } from "next/font/google";
import "./globals.css";
import ContextProvider from "@/providers/ContextProvider";
import { ThemeProvider } from "@/context/themeContext";

const geistSans = Geist({
  variable: "--font-geist-sans",
  subsets: ["latin"],
});

const geistMono = Geist_Mono({
  variable: "--font-geist-mono",
  subsets: ["latin"],
});

export const metadata: Metadata = {
  title: "Pokedex - Encyclopaedia of Pokemon",
  description: "Learn about all the Pokemon in the world",
};

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <html lang="en">
      <head>
        {/* <link
          rel="stylesheet"
          href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.7.1/css/all.min.css"
          integrity="sha512-5Hs3dF2AEPkpNAR7UiOHba+lRSJNeM2ECkwxUIxC1Q/FLycGTbNapWXB4tP889k5T5Ju8fs4b1P5z/iB4nMfSQ=="
          crossOrigin="anonymous"
          referrerPolicy="no-referrer"
        /> */}
        <link rel="icon" href="/pokeball.svg" />
      </head>
      <ThemeProvider>
        <ContextProvider>
          <body
            className={`${geistSans.variable} ${geistMono.variable} antialiased`}
          >
            {children}
          </body>
        </ContextProvider>
      </ThemeProvider>
    </html>
  );
}
