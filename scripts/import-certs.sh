#!/bin/sh

# To be run inside a JRE container

# The command line help
display_help() {
 	echo "Usage: $0 [-k KEYSTORE] [-c CERTDIRECTORY] [-p KSPASSWORD]"
    echo "   -c, --certs         Folder location of certs, defaults to certs"
    echo "   -k, --keystore      Keystore where certs will be imported, defaults to '$JAVA_HOME/lib/security/cacerts'"
    echo "   -p, --password      Password of the keystore, defaults to changeit"
    echo
    echo "Imports all *cer files from the specified folder into the specified keystore"
}

CERTS="certs"
KEYSTORE="$JAVA_HOME/lib/security/cacerts"
PWD="changeit"
while [ "$1" != "" ]; do
 case $1 in
   -k | --keystore )   shift
                       KEYSTORE=$1
                       ;;
   -c | --certs )      shift
                       CERTS=$1
                       ;;
   -p | --password )   shift
                       PWD=$1
                       ;;

   * )                 echo "$0: Invalid argument $1"
                       display_help
                       exit 1
   esac
   shift
done
set -e

if [ -d "$CERTS" ] && [ -f "$KEYSTORE" ]; then
    for file in "$CERTS"/*.cer; do
        echo "Importing $file"
        keytool -delete -alias $(basename "$file" .cer) -keystore "$KEYSTORE" -storepass $PWD > /dev/null || true
        keytool -importcert -noprompt -alias $(basename "$file" .cer) -keystore "$KEYSTORE" -storepass $PWD -file $file    
    done
else
    echo "$CERTS is not a valid directory or $KEYSTORE is not a valid file" >&2 && exit 1
fi
