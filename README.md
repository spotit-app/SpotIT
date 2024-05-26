# <h1 align="center">:rocket: _*SpotIT*_ :office:</h1>

## :notebook_with_decorative_cover: Description

_SpotIT_ is a team project made druring final year of CS studies at the [University of Gdańsk](https://ug.edu.pl/).
It is a web application made for IT employers and employees.

## :satellite: Preview

Preview of the working app is available at the following address:

[https://spotit.com.pl](https://spotit.com.pl)

:warning: Please be aware that the preview may be currently offline in order to not generate unnecessary expenses!

## :computer: Running the app locally

There are three main possibilities of running the app:

- for running both [backend](./backend) and [frontend](./frontend) parts separately, check out guides available in their directories
- for running app using `Kubernetes` see the guide in the [`k8s`](./k8s) directory
- for running the app using `Docker Compose`, see the guide below

### Running locally with `Docker` and `Docker Compose`

The easiest way to run the app locally is to use [`Docker`](https://www.docker.com/) and [`Docker Compose`](https://docs.docker.com/compose/).
After installing them, it can be started with one command:

```shell
$ docker compose -f <filename> up
```

where `filename` depends on desired environment (`docker-compose-dev.yml` or `docker-compose-prod.yml`).

:warning: Before running the app, it is mandatory to provide environment variables in seperate files (`.env.dev` or `.env.prod`) according to the provided examples. For running the app with `docker-compose-prod.yml`, it is also required to place a valid SSL certificate in the `ssl` directory.
