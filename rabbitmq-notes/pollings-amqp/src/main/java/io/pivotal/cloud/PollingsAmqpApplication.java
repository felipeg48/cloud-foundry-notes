package io.pivotal.cloud;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Binding.DestinationType;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.utils.SerializationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.metrics.CounterService;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.handler.annotation.SendTo;

import io.pivotal.cloud.domain.Candidate;
import io.pivotal.cloud.domain.Poll;

@SpringBootApplication
public class PollingsAmqpApplication {

	public static void main(String[] args) {
		SpringApplication.run(PollingsAmqpApplication.class, args);
	}
	
	private static final Logger log = LoggerFactory.getLogger(PollingsAmqpApplication.class);
	private Candidate candidate = null;
	
	@Autowired
	private CounterService hCounterService; 
	@Autowired
	private CounterService tCounterService; 
	
	
	@RabbitListener(queues={Poll.TQUEUE})
	@SendTo
	Candidate tProcess(Message message){
		//Step. Getting the Message
		candidate = (Candidate) SerializationUtils.deserialize(message.getBody());
		log.info("Processing candidate: " + candidate);
		
		//Step. Set Message OK
		candidate.setCode("OK");
		
		//Step. Adding Metrics
		int times = candidate.getVote();
		for(int i = 1; i <= times; i++)
		 tCounterService.increment(Poll.TMETRIC);
	
		return candidate;
	}
	
	@RabbitListener(queues={Poll.HQUEUE})
	@SendTo
	Candidate hProcess(Message message){
		//Step. Getting the Message
		candidate = (Candidate) SerializationUtils.deserialize(message.getBody());
		log.info("Processing candidate: " + candidate);
		
		//Step. Set Message OK
		candidate.setCode("OK");
		
		//Step. Adding Metrics
		int times = candidate.getVote();
		for(int i = 1; i <= times; i++)
		 hCounterService.increment(Poll.HMETRIC);
		
		return candidate;
	}
	
	@Bean
	Queue tQueue(){
		return new Queue(Poll.TQUEUE, true);
	}
	
	@Bean
	Queue hQueue(){
		return new Queue(Poll.HQUEUE, true);
	}
	
	@Bean
	Binding hBinding(){
		return new Binding(Poll.HQUEUE, DestinationType.QUEUE, Poll.EXCHANGE, Poll.HQUEUE, null);
	}
	
	@Bean
	Binding tBnding(){
		return new Binding(Poll.TQUEUE, DestinationType.QUEUE, Poll.EXCHANGE, Poll.TQUEUE, null);
	}
}
