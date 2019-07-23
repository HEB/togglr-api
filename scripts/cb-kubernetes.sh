#! /bin/bash
set -e

# Build manifest, expects an environment as first parameter
build_manifest() {
 	LOCAL_ENV=$1
    ENV_YAML=".config/$LOCAL_ENV.yaml"
    RELEASE="$(yq -r ._meta.name $ENV_YAML)"
    NAMESPACE="$(yq -r ._meta.namespace $ENV_YAML)"
    CHART="$(yq -r ._meta.chart $ENV_YAML)"
    VERSION="$(yq -r ._meta.version $ENV_YAML)"
    helm fetch --untar --untardir df --version $VERSION $CHART
    helm template --name $RELEASE --namespace $NAMESPACE -f $ENV_YAML $CHART > output/$LOCAL_ENV.yaml
    echo "Built $LOCAL_PROD manifest from chart $CHART:$VERSION and value $ENV_YAML"
}

# Unencrypt nonprod
cd .config
if [ "$TOGGLR_NONPROD" ]; then
    echo $TOGGLR_NONPROD > togglr-nonprod.json
    export GOOGLE_APPLICATION_CREDENTIALS="$(pwd)/togglr-nonprod.json"
    gcloud auth activate-service-account --key-file togglr-nonprod.json
fi
gcloud auth list
sops -d nonprod.enc.yaml > nonprod.yaml
sed -i s/_enc:/:/g nonprod.yaml 

# Unencrypt prod
if [ "$TOGGLR_PROD" ]; then
    echo $TOGGLR_PROD > togglr-prod.json
    gcloud auth activate-service-account --key-file togglr-prod.json
    export GOOGLE_APPLICATION_CREDENTIALS="$(pwd)/togglr-prod.json"
fi
gcloud auth list
sops -d prod.enc.yaml > prod.yaml
sed -i s/_enc:/:/g prod.yaml 
cd ..

# Build templates
echo "Initializing helm"
mkdir -p output
helm init --client-only
helm repo add df https://constellation-utils-helm-charts.storage.googleapis.com
helm repo update

# Build nonprod manifest
build_manifest "nonprod"
build_manifest "prod"

rm .config/nonprod.yaml
rm .config/prod.yaml
