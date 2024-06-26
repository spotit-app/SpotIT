/** @type {import('tailwindcss').Config} */
export default {
  content: [
    './index.html',
    './src/**/*.{js,ts,jsx,tsx}',
    'node_modules/flowbite-react/**/*.{js,jsx,ts,tsx}'
  ],
  theme: {
    extend: {}
  },
  daisyui: {
    themes: ['corporate', 'night'],
    darkTheme: 'night'
  },
  // eslint-disable-next-line no-undef
  plugins: [require('daisyui'), require('flowbite/plugin')]
};
