#!/bin/bash

# The command line help
display_help() {
 	  echo "Usage: $0 [-f JSON_FILE_PATH] [-k JSON_KEY]"
    echo "   -k, --key           Full text of key for authentication"
    echo "   -f, --key-file      JSON file with GCP credentials"
    echo
    echo "Authenticates account with GCP and configures docker to communicate directly with GCP"
}

while [ "$1" != "" ]; do
 case $1 in
   -k | --key )    shift
                       JSON_KEY=$1
                       ;;
   -f | --key-file )    shift
                       JSON_KEY_FILE=$1
                       ;;
   * )                 echo "$0: Invalid argument $1"
                       display_help
                       exit 1
   esac
   shift
done


#Setup location of default authentication key
GOOGLE_APPLICATION_CREDENTIALS=${HOME}/google-key.json

if [ -n "$JSON_KEY" ] || [ -n "$JSON_KEY_FILE" ]; then
  if [ -n "$JSON_KEY" ]; then
    #Actual Key passed as parameter
    /bin/echo $JSON_KEY > $GOOGLE_APPLICATION_CREDENTIALS
  elif [ -f "$JSON_KEY_FILE" ]; then
    #Key file location passed as parameter
    $GOOGLE_APPLICATION_CREDENTIALS = $JSON_KEY_FILE
  else
    echo "$0: Authentication skipped. Missing key or json key file." >&2
    display_help
    exit 1
  fi

  #Attempt authentication to GCP
  export GOOGLE_APPLICATION_CREDENTIALS
  echo "Creating ${GOOGLE_APPLICATION_CREDENTIALS} and authenticating"
  gcloud auth activate-service-account --key-file=$GOOGLE_APPLICATION_CREDENTIALS
  gcloud auth configure-docker -q
else
  #nothing set, verifying if already authenticated
  gcloud auth print-access-token > /dev/null
  if [ $? -gt 0 ]; then   
    echo "$0: Authentication skipped. Missing key or json key file." >&2
    display_help
    exit 1
  else
    echo "$0: Already authenticated."
    return 0  
  fi
fi

AUTHENTICATED_USER=$(gcloud auth list --filter=status:ACTIVE --format="value(account)")
if [ -n "${AUTHENTICATED_USER}" ]; then
  echo "Authenticated as ${AUTHENTICATED_USER}" 
else
  echo "$0: Not Authenticated" >&2
  exit 1
fi
