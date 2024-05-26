# :ship: Kubernetes config

This directory consists of simple config files used for deployment of application on the [`Kubernetes`](https://kubernetes.io/) cluster.
Before using them, it is necessary to update `spotit-secret.yml` with real values encoded with `base64`. 

After that, you have to run each file with the command:

```shell
$ kubectl apply -f <filename>
```

The order of files does not matter, although it is recommended to run `spotit-config.yml` and `spotit-secret.yml` files first, as they contain variables used by all other services.
