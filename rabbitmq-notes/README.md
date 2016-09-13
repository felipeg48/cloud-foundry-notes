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

1.
