# RDAP server application sample

This is a sample application in using the [DNS Belgium RDAP library](https://github.com/DNSBelgium/rdap) which retrieves the domain data from the WHOIS backends for newGTLDs.

It only performs exact domain queries, nothing else, and any TLD can be used, but not every TLD is supported and will yield output.

## How to run this application

This is a Java Web Application, the instructions below assume that you have a recent version of the JDK and Apache Maven on your system, and preferably use an IDE for local development/testing.

In the root folder of the project, type following command:

    mvn clean install

In your IDE, run the following class:

    StartJetty

You should then find following line at the end of the generated log output

    Started http://localhost:3030/

which means your server has started.

RDAP queries are now possible, for example:

    http://localhost:3030/rdap/domain/nic.brussels

## How to run deploy application

Using the web application container of your choice (Tomcat, Glassfish, Jetty, ...) deploy the generated war which can be found using the path below.

    targets/rdapgtld.war

For Tomcat users:

if you deploy this a regular webapp (contextpath = war name), your queries can be done as following

    http://<machine>:<tomcatport>/rdapgtld/rdap/domain/nic.brussels

otherwise, if you deploy it as your ROOT webapp, it will be

    http://<machine>:<tomcatport>/rdap/domain/nic.brussels

## Using the DNS Belgium RDAP client

Unless you are fond of curl/wget/Postman/... , you can also try the DNS Belgium RDAP client

Installing from source (requires Java and Gradle)

    git clone git@github.com:DNSBelgium/rdap.git
    cd rdap/client
    gradle distTar
    sudo tar -C /opt -xf build/distributions/rdap-<version>.tar
    export PATH="$PATH:/opt/rdap-<version>/bin"
    rdap --help

Try the same query

    rdap --pretty -i -u http://localhost:3030/rdap nic.brussels