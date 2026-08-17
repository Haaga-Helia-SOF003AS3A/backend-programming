# CSC services in Haaga-Helia – CLI version

> Command syntax in this document has been verified against `oc` client 4.22 (OKD, matching Rahti 2's OpenShift 4.x) and `python-openstackclient` 10.2 with `python-troveclient` (August 2026). The Rahti API URL and the shared egress IP have been checked against docs.csc.fi. Cluster-side behavior should still be smoke-tested once against a real Rahti project.

## 1. First use

Log in to MyCSC with your Haka account. The first login creates a CSC username.

MyCSC is still needed for the following administrative matters:

- Creating a CSC project
- Associating the Rahti service with a project
- Connecting the Pukki service to a project
- Adding users to a project
- Acceptance of the Terms of Use

A student can set up a Student-type project. In a course project, the teacher can set up a joint Course project and invite students to it.

Once Rahti has been activated for a CSC project, the web console is no longer needed for its daily use.

## 2. The `oc` command line tool

First, check if the OpenShift CLI is already installed:

```bash
oc version --client
```

If the command is not found, install the appropriate `oc` client for CSC's Rahti/OpenShift version and add it to the `PATH`.

HH's current instructions advise you to retrieve the login command from the Rahti web console. However, the OpenShift CLI also supports browser-assisted command-line login, so try it first:

```bash
oc login https://api.2.rahti.csc.fi:6443 --web
```

After that:

```bash
oc whoami
oc whoami --show-server
oc projects
```

OpenShift supports `oc login --web`. If the authentication chain used by CSC does not accept it, use the fallback method documented by CSC: retrieve the *Copy Login Command* once from the Rahti console and execute:

```bash
oc login https://api.2.rahti.csc.fi:6443 --token='<TOKEN>'
```

The token must not be stored in a Git repository.

## 3. Creating a Rahti project

The MyCSC project number is required in the Rahti project description.

For example:

```bash
export CSC_PROJECT=2001234
export RAHTI_PROJECT=softwareproject-group1
```

Create the project:

```bash
oc new-project "$RAHTI_PROJECT" \
  --description="csc_project:$CSC_PROJECT"
```

Check:

```bash
oc project
oc status
oc get quota
```

List all your projects:

```bash
oc projects
```

Switch to an existing project:

```bash
oc project softwareproject-group1
```

HH's current Rahti instruction specifically uses the `csc_project:<project number>` description when creating a project.

## 4. The most useful `oc` commands

These replace a large part of the Overview, Topology, Pods, and Logs views in the Rahti web console:

```bash
oc status
oc get all
oc get pods
oc get services
oc get routes
oc get builds
oc get bc
oc get deployments
```

Continuous monitoring:

```bash
oc get pods -w
```

Pod details:

```bash
oc describe pod <POD>
```

Logs:

```bash
oc logs <POD>
```

Continuous monitoring of logs:

```bash
oc logs -f <POD>
```

Deployment logs:

```bash
oc logs -f deployment/<APPLICATION>
```

Build logs:

```bash
oc logs -f bc/<APPLICATION>
```

Inside the container:

```bash
oc rsh <POD>
```

or:

```bash
oc exec -it <POD> -- /bin/sh
```

Events:

```bash
oc get events --sort-by=.lastTimestamp
```

Environment variables:

```bash
oc set env deployment/<APPLICATION> --list
```

The Pods/Logs/Terminal functions of HH's web-based debugging help can thus be performed directly via the CLI.

## 5. Spring Boot app to Rahti

First, define for example:

```bash
export APP=ticketguru
export REPO=https://github.com/kayttaja/ticketguru.git
export BRANCH=main
```

### Option A: The repository has a Dockerfile

If the repository has a Dockerfile at the root:

```bash
oc new-app "$REPO#$BRANCH" \
  --name="$APP"
```

Follow the build:

```bash
oc get builds
oc logs -f bc/"$APP"
```

Watch the pods start:

```bash
oc get pods -w
```

Check resources:

```bash
oc get all
```

HH's current instruction uses the same `oc new-app <repository>#<branch>` method in a Dockerfile-based build.

### Example Dockerfile for Spring Boot

For example, in a Maven project:

```dockerfile
FROM eclipse-temurin:17-jdk AS build
WORKDIR /app
COPY . .
RUN ./mvnw clean package -DskipTests

FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```

Before deploying to Rahti, try the Dockerfile locally:

```bash
docker build -t "$APP" .
docker run --rm -p 8080:8080 "$APP"
```

or with Podman:

```bash
podman build -t "$APP" .
podman run --rm -p 8080:8080 "$APP"
```

## 6. Spring Boot with the Source-to-Image method

A Dockerfile is optional. HH's help also uses the OpenShift Source-to-Image builder.

For example, Java 17:

```bash
oc new-app \
  "registry.access.redhat.com/ubi8/openjdk-17:1.18-2~$REPO#$BRANCH" \
  --name="$APP"
```

Follow:

```bash
oc logs -f bc/"$APP"
oc get pods -w
```

## 7. Publishing an app via HTTPS

The *Create Route / Route TLS* step in the web console is not required.

First, check the Service:

```bash
oc get svc
```

Assuming the service name is the same as the app's:

```bash
oc create route edge "$APP" \
  --service="$APP"
```

Check the Route:

```bash
oc get route
```

Address only:

```bash
oc get route "$APP" \
  -o jsonpath='{.spec.host}{"\n"}'
```

Test:

```bash
HOST=$(oc get route "$APP" -o jsonpath='{.spec.host}')
curl -I "https://$HOST"
```

If your project first created an HTTP route and you want to replace it:

```bash
oc delete route "$APP"
```

and then:

```bash
oc create route edge "$APP" \
  --service="$APP"
```

In HH's separate HTTPS help, the TLS route is created using the same `oc create route edge` command.

## 8. New build by hand

When there are new changes in GitHub:

```bash
oc start-build "$APP"
```

With follow-up at the same time:

```bash
oc start-build "$APP" --follow
```

Or:

```bash
oc start-build "$APP"
oc logs -f bc/"$APP"
```

## 9. Private GitHub repository with the CLI

In HH's instructions, a separate SSH deploy key is created for a private repository. This can be done almost entirely from the command line.

Generate a key without a passphrase:

```bash
ssh-keygen \
  -t ed25519 \
  -C "rahti-build" \
  -f id_rahti_build \
  -N ""
```

You will get:

```text
id_rahti_build
id_rahti_build.pub
```

View the public key:

```bash
cat id_rahti_build.pub
```

### Deploy key to GitHub with `gh`

If the GitHub CLI is available:

```bash
gh auth login
```

Configure:

```bash
export OWNER=mygithubuser
export GITHUB_REPO=ticketguru
```

Add the deploy key:

```bash
gh api \
  --method POST \
  "repos/$OWNER/$GITHUB_REPO/keys" \
  -f title="Rahti build" \
  -f key="$(cat id_rahti_build.pub)" \
  -F read_only=true
```

GitHub supports deploy key management via the REST API. For builds, the key does not normally need write access.

### Create a Rahti Secret

```bash
oc create secret generic github-secret \
  --from-file=ssh-privatekey=id_rahti_build \
  --type=kubernetes.io/ssh-auth
```

Associate it with the builder ServiceAccount:

```bash
oc secrets link builder github-secret
```

Check:

```bash
oc get secret github-secret
```

After that, the SSH address is used:

```bash
export REPO="git@github.com:$OWNER/$GITHUB_REPO.git"
```

Dockerfile project:

```bash
oc new-app "$REPO#$BRANCH" \
  --name="$APP" \
  --source-secret=github-secret
```

Source-to-Image:

```bash
oc new-app \
  "registry.access.redhat.com/ubi8/openjdk-17:1.18-2~$REPO#$BRANCH" \
  --name="$APP" \
  --source-secret=github-secret
```

## 10. Automating the build from a GitHub push

In the web help, the webhook is added in the GitHub *Settings → Webhooks* view. The same can be done from the command line.

First, look at the BuildConfig:

```bash
oc get bc
oc describe bc "$APP"
```

`oc describe` displays the webhook details of the BuildConfig if a GitHub webhook trigger is defined. OpenShift's BuildConfig supports GitHub webhook triggers.

If the GitHub webhook URL appears in the output, copy it into a shell variable:

```bash
export WEBHOOK_URL='https://api....'
```

Then GitHub with the CLI:

```bash
gh api \
  --method POST \
  "repos/$OWNER/$GITHUB_REPO/hooks" \
  -f name=web \
  -F active=true \
  -f 'events[]=push' \
  -f "config[url]=$WEBHOOK_URL" \
  -f 'config[content_type]=json'
```

GitHub's webhook API defines the URL, content type, activity, and events; push can be set as the trigger.

Test:

```bash
git add .
git commit -m "Test Rahti webhook"
git push
```

Track the build:

```bash
oc get builds -w
```

In a second terminal:

```bash
oc logs -f bc/"$APP"
```

This way, the entire push → build → deployment chain can be verified without the Rahti web console.

## 11. React app to Rahti

HH's current React help uses a Docker build and port 8080.

For example:

```bash
export APP=myreact
export REPO=https://github.com/kayttaja/myreact.git
export BRANCH=main
```

First, test locally:

```bash
docker build -t "$APP" .
docker run --rm -p 8080:8080 "$APP"
```

Create the app in Rahti:

```bash
oc new-app "$REPO#$BRANCH" \
  --name="$APP"
```

From a private repository:

```bash
oc new-app "$REPO#$BRANCH" \
  --name="$APP" \
  --source-secret=github-secret
```

Follow:

```bash
oc logs -f bc/"$APP"
oc get pods -w
```

Create an HTTPS route:

```bash
oc create route edge "$APP" \
  --service="$APP"
```

Print the address:

```bash
echo "https://$(oc get route "$APP" -o jsonpath='{.spec.host}')"
```

New build:

```bash
oc start-build "$APP" --follow
```

These correspond to the build, service, and route steps of the current React help in the web console.

## 12. Pukki database with the CLI

Here the CLI changes slightly: Pukki uses OpenStack/Trove command line tools, not `oc`.

Install:

```bash
python3 -m pip install --user \
  python-openstackclient \
  python-troveclient
```

CSC's current Pukki instructions use the same tools.

### OpenRC

In CSC's current implementation, the OpenRC file is retrieved once from Pukki's **API Access** view.

After that:

```bash
source ~/Downloads/<OPENRC_FILE>
```

Test:

```bash
openstack datastore list
```

This is Pukki's second remaining web-console exception. After that, the database can be managed with the CLI.

## 13. Exploring Pukki's options

Flavors:

```bash
openstack database flavor list
```

Database types:

```bash
openstack datastore list
```

PostgreSQL versions:

```bash
openstack datastore version list postgresql
```

## 14. Creating a PostgreSQL instance in Pukki

For example, specify:

```bash
export DB_INSTANCE=ticketguru-db
export DB_NAME=ticketguru
export DB_USER=ticketguru
```

Request the password without printing it on the screen:

```bash
read -s -p "Database password: " DB_PASSWORD
echo
```

Rahti's shared outgoing (egress) IP address, which must be allowed per the current HH and CSC guidelines:

```text
86.50.229.150/32
```

**Note:** this egress IP is shared by *all* Rahti customers, so allowing it means any application running on Rahti can reach your database port. CSC therefore stresses using a strong, unique database username and password. CSC also notes the egress IP may change in the future, so do not treat it as permanent in course material.

Create a PostgreSQL instance, for example:

```bash
openstack database instance create "$DB_INSTANCE" \
  --flavor standard.small \
  --databases "$DB_NAME" \
  --users "$DB_USER:$DB_PASSWORD" \
  --datastore postgresql \
  --datastore-version 17.0 \
  --is-public \
  --size 1 \
  --allowed-cidr 86.50.229.150/32
```

First check the available datastore version:

```bash
openstack datastore version list postgresql
```

So do not hardcode `17.0` in teaching material if the listing shows some other current version.

CSC's current Pukki CLI instruction uses the same `openstack database instance create` structure and allows multiple `--allowed-cidr` parameters.

## 15. Allowing your own computer into Pukki

Find out your public IPv4:

```bash
curl -4 ifconfig.me
```

For example:

```bash
export MY_IP=$(curl -4 -s ifconfig.me)
echo "$MY_IP"
```

If you create the instance allowing both Rahti and your own machine at the same time:

```bash
openstack database instance create "$DB_INSTANCE" \
  --flavor standard.small \
  --databases "$DB_NAME" \
  --users "$DB_USER:$DB_PASSWORD" \
  --datastore postgresql \
  --datastore-version 17.0 \
  --is-public \
  --size 1 \
  --allowed-cidr 86.50.229.150/32 \
  --allowed-cidr "$MY_IP/32"
```

CSC warns against opening the database to the entire Internet. In Pukki, firewall rules should be limited to individual addresses or the networks actually needed.

## 16. Examining the Pukki instance

List:

```bash
openstack database instance list
```

Details:

```bash
openstack database instance show <INSTANCE-ID>
```

JSON:

```bash
openstack database instance show <INSTANCE-ID> -f json
```

YAML:

```bash
openstack database instance show <INSTANCE-ID> -f yaml
```

Databases:

```bash
openstack database db list <INSTANCE-ID>
```

Users:

```bash
openstack database user list <INSTANCE-ID>
```

New database:

```bash
openstack database db create \
  <INSTANCE-ID> toinen_tietokanta
```

CSC documents these Trove/OpenStack commands in its current Pukki CLI documentation.

## 17. Changing Pukki's firewall

For example:

```bash
openstack database instance update <INSTANCE-ID> \
  --allowed-cidr 86.50.229.150/32 \
  --allowed-cidr "$MY_IP/32"
```

**Note an important detail:** according to CSC's Pukki guideline, updating the firewall rules **replaces** the existing rules. Therefore, all CIDRs to be kept must be entered in the same command.

## 18. Spring Boot's Pukki configuration

For example:

```properties
spring.datasource.url=jdbc:postgresql://${DB_SERVICE_HOST}:${DB_SERVICE_PORT}/${DB_NAME}
spring.datasource.username=${DB_USER}
spring.datasource.password=${DB_PASSWORD}
spring.jpa.generate-ddl=true
spring.jpa.hibernate.ddl-auto=update
```

The Rahti profile can be activated with:

```bash
SPRING_PROFILES_ACTIVE=rahti
```

HH's current Spring + Pukki guideline uses environment variables for exactly this purpose.

## 19. Database passwords into an OpenShift Secret

Do not add the password to the Dockerfile, `application.properties`, or GitHub.

Create a Secret:

```bash
oc create secret generic pukki-db \
  --from-literal=DB_USER="$DB_USER" \
  --from-literal=DB_PASSWORD="$DB_PASSWORD"
```

Check:

```bash
oc get secret pukki-db
```

Give the Deployment the secret's variables:

```bash
oc set env deployment/"$APP" \
  --from=secret/pukki-db
```

Other settings:

```bash
oc set env deployment/"$APP" \
  DB_SERVICE_HOST='<PUKKI-HOST>' \
  DB_SERVICE_PORT='5432' \
  DB_NAME="$DB_NAME" \
  SPRING_PROFILES_ACTIVE='rahti'
```

Check:

```bash
oc set env deployment/"$APP" --list
```

Follow the restart:

```bash
oc rollout status deployment/"$APP"
```

Logs:

```bash
oc logs -f deployment/"$APP"
```

This replaces entering environment variables in the *Deployment → Environment* view of the Rahti web console.

## 20. Fully debugging with the CLI

If the app doesn't start:

```bash
oc get pods
```

For example, if you see:

```text
CrashLoopBackOff
```

Look at:

```bash
oc describe pod <POD>
oc logs <POD>
```

Log of the previous crashed container:

```bash
oc logs <POD> --previous
```

If the build failed:

```bash
oc get builds
oc describe build <BUILD>
oc logs <BUILD>
```

or:

```bash
oc logs -f bc/"$APP"
```

If the Route doesn't work:

```bash
oc get route
oc describe route "$APP"
oc get svc
oc get endpoints
```

If the Deployment doesn't progress:

```bash
oc rollout status deployment/"$APP"
oc describe deployment "$APP"
```

Environment variables in a container:

```bash
oc exec <POD> -- env | sort
```

Shell:

```bash
oc rsh <POD>
```

HH's original troubleshooting instruction uses the same pods and their logs, but part of the review is done in the web console; these commands eliminate the need for the web console.

## 21. Deleting the project and starting from scratch

Delete:

```bash
oc delete project "$RAHTI_PROJECT"
```

Recreate:

```bash
oc new-project "$RAHTI_PROJECT" \
  --description="csc_project:$CSC_PROJECT"
```

This procedure is also included in HH's current error-tracing instructions.

## 22. JKube alternative

The repository also has JKube help, although it does not appear in the main menu of the current site. JKube allows you to build OpenShift resources directly from the Maven project.

Add to `pom.xml`:

```xml
<plugin>
  <groupId>org.eclipse.jkube</groupId>
  <artifactId>openshift-maven-plugin</artifactId>
</plugin>
```

Log in first:

```bash
oc login https://api.2.rahti.csc.fi:6443 --web
oc project "$RAHTI_PROJECT"
```

Then:

```bash
./mvnw package oc:build oc:resource oc:apply
```

Instead of the Overview view in the web console:

```bash
oc get all
oc get pods -w
```

Logs:

```bash
oc logs -f deployment/"$APP"
```

Route:

```bash
oc get route
```

If JKube doesn't create the Route the way you want, you can do it yourself:

```bash
oc create route edge "$APP" \
  --service="$APP"
```

## 23. Older Rahti internal database help

The repository also has an older `spring_tietokannan_konfigurointi` guideline. In it, the database operates under a Service name within the same OpenShift project, not as the current primary Pukki solution.

OpenShift gives the Service environment variables in the form of, for example:

```text
MYSQL_SERVICE_HOST
MYSQL_SERVICE_PORT
```

Again, the Environment view in the web console can be replaced:

```bash
oc set env deployment/"$APP" \
  DB_SERVICE_HOST='<HOST>' \
  DB_SERVICE_PORT='<PORT>'
```

Secret data from a Secret:

```bash
oc set env deployment/"$APP" \
  --from=secret/database-secret
```

In current teaching, I would rather use **the Pukki + Secret + `oc set env` model described above**, because Pukki is in the Spring instructions in the main menu of the current site.

## 24. Course project – teacher

The administrative part of the course project instruction cannot sensibly be converted to `oc` in its entirety, because the CSC project is created before the Rahti project.

Teacher:

1. Create a Course project in MyCSC
2. Activate the Rahti/Pukki services for it
3. Add students
4. Students accept the terms of the services

After that, all Rahti work can be done from the CLI:

```bash
oc login https://api.2.rahti.csc.fi:6443 --web
oc projects
```

and, for example, for a group:

```bash
oc new-project group1-backend \
  --description="csc_project:$CSC_PROJECT"
```

The current course project instructions define the teacher as the project manager and MyCSC as the administrative interface.

## 25. Course project – student

The student first joins the Course project according to the current instructions with an invitation link and a Haka/MyCSC login.

After that, the CLI:

```bash
oc login https://api.2.rahti.csc.fi:6443 --web
```

Projects:

```bash
oc projects
```

Choose the right one:

```bash
oc project <PROJECT>
```

Check:

```bash
oc whoami
oc status
```

From then on, the Rahti web console is no longer needed for normal work.

## 26. Spring Boot complete recipe

When MyCSC/Rahti is already activated:

```bash
export CSC_PROJECT=2001234
export RAHTI_PROJECT=ticketguru
export APP=ticketguru
export REPO=https://github.com/user/ticketguru.git
export BRANCH=main
```

Login:

```bash
oc login https://api.2.rahti.csc.fi:6443 --web
```

Project:

```bash
oc new-project "$RAHTI_PROJECT" \
  --description="csc_project:$CSC_PROJECT"
```

Application:

```bash
oc new-app "$REPO#$BRANCH" \
  --name="$APP"
```

Build:

```bash
oc logs -f bc/"$APP"
```

Pods:

```bash
oc get pods -w
```

HTTPS:

```bash
oc create route edge "$APP" \
  --service="$APP"
```

Address:

```bash
echo "https://$(oc get route "$APP" -o jsonpath='{.spec.host}')"
```

Test:

```bash
curl -I \
  "https://$(oc get route "$APP" -o jsonpath='{.spec.host}')"
```

Status:

```bash
oc get all
oc get route
```

This corresponds to the Rahti portion of the site's Spring Boot recipe, but without the deployment and route steps in the web console.

## 27. Spring + Pukki complete recipe

Pukki:

```bash
source ~/Downloads/project-openrc.sh
openstack database flavor list
openstack datastore version list postgresql
```

Create a database allowing Rahti:

```bash
openstack database instance create ticketguru-db \
  --flavor standard.small \
  --databases ticketguru \
  --users "$DB_USER:$DB_PASSWORD" \
  --datastore postgresql \
  --datastore-version 17.0 \
  --is-public \
  --size 1 \
  --allowed-cidr 86.50.229.150/32
```

Check:

```bash
openstack database instance list
```

Rahti:

```bash
oc create secret generic pukki-db \
  --from-literal=DB_USER="$DB_USER" \
  --from-literal=DB_PASSWORD="$DB_PASSWORD"

oc set env deployment/"$APP" \
  --from=secret/pukki-db

oc set env deployment/"$APP" \
  DB_SERVICE_HOST='<PUKKI-HOST>' \
  DB_SERVICE_PORT='5432' \
  DB_NAME='ticketguru' \
  SPRING_PROFILES_ACTIVE='rahti'

oc rollout status deployment/"$APP"
oc logs -f deployment/"$APP"
```

## 28. React complete recipe

```bash
export CSC_PROJECT=2001234
export RAHTI_PROJECT=frontend
export APP=frontend
export REPO=https://github.com/user/frontend.git
export BRANCH=main

oc login https://api.2.rahti.csc.fi:6443 --web

oc new-project "$RAHTI_PROJECT" \
  --description="csc_project:$CSC_PROJECT"

oc new-app "$REPO#$BRANCH" \
  --name="$APP"

oc logs -f bc/"$APP"
oc get pods -w

oc create route edge "$APP" \
  --service="$APP"

echo "https://$(oc get route "$APP" -o jsonpath='{.spec.host}')"
```

New build:

```bash
oc start-build "$APP" --follow
```

The automatic build can then be connected to the GitHub webhook as described in Chapter 10. This corresponds to the Rahti steps in the current React recipe, in CLI format.

## 29. In practice, the most important commands for students

If you want to shorten the course material a lot, these cover maybe 90% of daily Rahti work:

```bash
# Login
oc login https://api.2.rahti.csc.fi:6443 --web

# Projects
oc projects
oc project myproject

# Creating a project
oc new-project myproject \
  --description="csc_project:2001234"

# App from GitHub
oc new-app https://github.com/user/repo.git#main \
  --name=myapp

# Build
oc get builds
oc logs -f bc/myapp
oc start-build myapp --follow

# Pods
oc get pods
oc get pods -w
oc logs -f deployment/myapp

# All resources
oc get all

# HTTPS
oc create route edge myapp --service=myapp
oc get route

# Deployment settings
oc set env deployment/myapp KEY=value

# Secret
oc create secret generic mysecret \
  --from-literal=PASSWORD='secret'
oc set env deployment/myapp \
  --from=secret/mysecret

# Debug
oc describe pod <pod>
oc get events --sort-by=.lastTimestamp
oc rsh <pod>

# Restart
oc rollout restart deployment/myapp
oc rollout status deployment/myapp

# Deleting a project
oc delete project myproject
```
