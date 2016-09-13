# RabbitMQ Apps for Cloud Foundry

The following apps are just a simple example to run RabbitMQ as basking services. These are microservices comunicating through RabbitMQ using a RPC pattern, with a Topic Exchange.

## Requirements
1. Java 8 installed (JDK)
2. Maven installed
3. [Optional] STS IDE

## Deploying to Cloud Foundry

#### Prepare your Cloud Foundry Space
1. Login into Cloud Foundry
2. Create a RabbitMQ service instance:
+
```bash
$ cf create-service p-rabbitmq standard rabbitmq
```

#### Compile the two Projects:

These projects are a Spring Boot Web projects and they depend only on:
- spring-boot-starter-web
- spring-boot-starter-amqp
- spring-boot-starter-actuator

A simple Topology, a Topic Exchange:
![Topic Exchange](https://github.com/felipeg48/cloud-foundry-notes/blob/master/rabbitmq-notes/images/topology.png "Topic Exchange")


The **voters-amqp** project will send 10 **_Candidate_** messages every 1 second. This app will create a _polls_ exchange.
```bash
$ mvn clean package -DskipTests=true
```

The **polling-amqp** project will receive and process the votes and send a reply that everything went OK. This app will create the _queues_ and necessary bindings.
```bash
$ mvn clean package -DskipTests=true
```

#### Deploy to Cloud Foundry

1. Push the **voters-amqp** app:
```bash
$ cd voters-amqp
$ cf push voters-amqp -p target/voters-amqp-0.0.1-SNAPSHOT.war -b java_buildpack --no-start -m 512M
```
2. Push the **pollings-amqp** app:
```bash
$ cd pollingss-amqp
$ cf push pollings-amqp -p target/pollings-amqp-0.0.1-SNAPSHOT.war -b java_buildpack --no-start -m 512M
```
<aside class="notice">
Remember to use the --random-route for not colide with another apps.
</aside>
3. Bind the apps to the _rabbitmq_ service
```bash
$ cf bind-service voters-aqmp rabbitmq
$ cf bind-service pollings-amqp rabbitm
```
4. Starts the apps
```bash
$ cf start voters-aqmp
$ cf start pollings-amqp
```
5. Watch the Logs fgor each app.

## TODO

- [X] Simple RabbitMQ example deployable for Cloud Foundry
- [ ] Add metrics with the Gauge and Counter services from Actuator
- [ ] Add a Graph to represent the Metrics.
