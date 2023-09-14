/** @type {import('tailwindcss').Config} */
module.exports = {
  content: [
    './src/pages/**/*.{js,ts,jsx,tsx,mdx}',
    './src/components/**/*.{js,ts,jsx,tsx,mdx}',
    './src/app/**/*.{js,ts,jsx,tsx,mdx}',
  ],
  theme: {
    extend: {
      colors: {
        "bg-neutral": '#27276b',
        "bg-primary": '#383896',
        "bg-secondary": '#4e4ebc',
        "fg-primary": '#ffffff',
        "fg-secondary": '#bfbfe8',
        accent: '#fca311'
      },
      fontFamily: {
        sans: ['Open Sans', 'sans-serif'],
        mono: ['JetBrains Mono', 'monospace'],
      }
    },
  },
  plugins: [
    require('@tailwindcss/forms')
  ]
}
