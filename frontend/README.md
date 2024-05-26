# :globe_with_meridians: SpotIT Frontend

<p align="center">
    <img src="https://img.shields.io/badge/pnpm-F69220.svg?style=for-the-badge&logo=pnpm&logoColor=white" alt="pnpm">
    <img src="https://img.shields.io/badge/Vite-646CFF.svg?style=for-the-badge&logo=Vite&logoColor=white" alt="vite">
</p>
<p align="center">
    <img src="https://img.shields.io/badge/React-61DAFB.svg?style=for-the-badge&logo=React&logoColor=black" alt="react">
    <img src="https://img.shields.io/badge/React%20Router-CA4245.svg?style=for-the-badge&logo=React-Router&logoColor=white" alt="react-router">
    <img src="https://img.shields.io/badge/TanStack%20Query-orange?style=for-the-badge&logo=react&logoColor=white" alt="react-query">
    <img src="https://img.shields.io/badge/Auth0-EB5424.svg?style=for-the-badge&logo=Auth0&logoColor=white" alt="auth0">
    <img src="https://img.shields.io/badge/Jest-C21325.svg?style=for-the-badge&logo=Jest&logoColor=white" alt="jest">
</p>
<p align="center">
    <img src="https://img.shields.io/badge/Tailwind%20CSS-06B6D4.svg?style=for-the-badge&logo=Tailwind-CSS&logoColor=white" alt="tailwind">
    <img src="https://img.shields.io/badge/DaisyUI-5A0EF8.svg?style=for-the-badge&logo=DaisyUI&logoColor=white" alt="daisyui">
</p>

## :wrench: Running frontend locally

Frontend uses `pnpm` as the package manager.
Before running the app, you need to have all dependencies installed.
To do it, run the following command:

```shell
$ pnpm install
```

After that, you can run the app for development using this command:

```shell
$ pnpm dev
```

In order to run all the tests, execute the following command:

```shell
$ pnpm test
```

For the deployment, frontend is built and served statically from the backend, which is containerized with `Docker`.
