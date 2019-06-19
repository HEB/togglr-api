az acr login --name sadcNonProdContainerRegistry &&
docker build -t togglr-api . &&
docker tag togglr-api sadcnonprodcontainerregistry.azurecr.io/togglr-api:stable &&
docker push sadcnonprodcontainerregistry.azurecr.io/togglr-api:stable