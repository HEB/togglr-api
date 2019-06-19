gcloud auth configure-docker &&
docker build -t togglr-api:stable . &&
docker tag togglr-api:stable gcr.io/heb-cx-labs/togglr-api:stable &&
docker push gcr.io/heb-cx-labs/togglr-api:stable