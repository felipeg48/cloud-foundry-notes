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

These projects are a Web projects, they depend only on:
- spring-boot-starter-web
- spring-boot-starter-amqp
- spring-boot-starter-actuator

The **voters-amqp** project will send 10 **_Candidate_** message every 1 second.
```bash
$ mvn clean package -DskipTests=true
```

The **polling-amqp** project will receive and process the votes and send a reply that everything went OK.
```bash
$ mvn clean package -DskipTests=true
```


## TODO

- [X] Simple RabbitMQ example deployable for Cloud Foundry
- [ ] Add metrics with the Gauge and Counter services from Actuator
- [ ] Add a Graph to represent the Metrics.
