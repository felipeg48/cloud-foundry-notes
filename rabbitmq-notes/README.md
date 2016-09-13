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
 Remember to use the <b>--random-route</b> for not colide with another apps.
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
6. You can take a look the pollings-amqp.cfapps.io/metrics  and take a look at the:
 - **counter.polls.for.h**
 - **counter.polls.for.t**
 ```json
 {

    "mem": 379870,
    "mem.free": 299583,
    "processors": 8,
    "instance.uptime": 14258,
    "uptime": 17797,
    "systemload.average": 2.8251953125,
    "heap.committed": 324608,
    "heap.init": 262144,
    "heap.used": 25024,
    "heap": 3728384,
    "nonheap.committed": 56336,
    "nonheap.init": 2496,
    "nonheap.used": 55264,
    "nonheap": 0,
    "threads.peak": 47,
    "threads.daemon": 23,
    "threads.totalStarted": 53,
    "threads": 45,
    "classes": 6731,
    "classes.loaded": 6731,
    "classes.unloaded": 0,
    "gc.ps_scavenge.count": 7,
    "gc.ps_scavenge.time": 68,
    "gc.ps_marksweep.count": 2,
    "gc.ps_marksweep.time": 115,
    "httpsessions.max": -1,
    "httpsessions.active": 0,
    "counter.polls.for.h": 355,
    "counter.polls.for.t": 395

 }
 ```

## TODO

- [X] Simple RabbitMQ example deployable for Cloud Foundry
- [X] Add metrics a Counter service from Actuator
- [ ] Add a Graph to represent the Metrics.
