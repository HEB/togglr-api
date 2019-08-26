# Togglr API

<img src="assets/tglr-logo-color.svg" width="400" height="200" /> <img src="assets/hebLogo.png" width="400" height="200" />

### Development

#### Local
To run local/debug run with the `local` profile.

Default username/password as set in the local profile are `admin`/`test`.

#### Docker
To build the docker image locally:

`docker build -t togglr/togglr-api .`

To run the newly built image on localhost:8080:

`docker run -p 8080:8080 togglr/togglr-api`

Running without using the *docker-compose.yaml* file form the Togglr root project will throw errors without additional configs.

If you wish to run the Togglr API stand-alone.  Create a *docker-compose.yaml* file containing a database.


### Environment Variables for Container Deployments

To deploy as a Container, you need to set the following Environment Variables:

```
SPRING_DATASOURCE_URL
SPRING_DATASOURCE_USERNAME
SPRING_DATASOURCE_PASSWORD
SPRING_DATASOURCE_DRIVER-CLASS-NAME

SPRING_SECURITY_USER_PASSWORD
SPRING_SECURITY_USER_NAME

SERVER_SERVLET_CONTEXT-PATH
HEB_TOGGLR_APP-DOMAIN

HEB_TOGGLR_CLIENT_APP-ID
HEB_TOGGLR_CLIENT_SERVER-URL

SPRING_PROFILES_ACTIVE
```

#### Database Configuration


Togglr has been tested with the following databases:

    Microsoft Sql Server: com.microsoft.sqlserver.jdbc.SQLServerDriver
    H2: org.h2.Driver
    MySQL: com.mysql.jdbc.Driver

The database driver to use can be configured with the environment variables. (See Spring Profiles below).


#### Domain settings
The Environment Variable `HEB_TOGGLR_APP-DOMAIN` controls the cookie value of your JWT token.
This needs to be the root domain of your server.

#### User Configuration
```
SPRING_SECURITY_USER_PASSWORD
SPRING_SECURITY_USER_NAME
```

These will be the credentials users log in with.  It is best to pass these in with an Opaque secret.

#### Spring Profiles

When setting the `SPRING_PROFILES_ACTIVE` value, you will generally include the `clouddev` profile,
as well as the one for your specific database. If running with MySQL for example:

`SPRING_PROFILES_ACTIVE: clouddev,mysql`

#### Togglr Client
THIS IS BROKE!!!!!!1
Togglr can function as its own Feature Flag control software.

Configure the following values to point to the deployment:

```
HEB_TOGGLR_CLIENT_APP-ID
HEB_TOGGLR_CLIENT_SERVER-URL
```

In general, these will not be needed if you're not running a development build.

If you wish to set these values, also include the run profile `togglr` in your active profiles.

### Creating the secret for Kubernetes:   
```json
{
  "kind": "Secret",
  "apiVersion": "v1",
  "metadata": {
    "name": "togglr-secret",
    "namespace": "default"
  },
  "data": {
    "SPRING_DATASOURCE_DRIVER-CLASS-NAME": "",
    "SPRING_DATASOURCE_PASSWORD": "",
    "SPRING_DATASOURCE_URL": "",
    "SPRING_DATASOURCE_USERNAME": "",
    "SPRING_SECURITY_USER_NAME": "",
    "SPRING_SECURITY_USER_PASSWORD": "",
    "HEB_TOGGLR_APP-DOMAIN" : ""
  },
  "type": "Opaque"
}
```

And referencing them in your deployment:
```json
"env": [
          {
            "name": "SPRING_DATASOURCE_URL",
            "valueFrom": {
              "secretKeyRef": {
                "name": "togglr-secret",
                "key": "SPRING_DATASOURCE_URL"
              }
            }
          }
          ...
        ]
```

### License Information

Apache 2.0, see the `LICENSE` file for more information.  


