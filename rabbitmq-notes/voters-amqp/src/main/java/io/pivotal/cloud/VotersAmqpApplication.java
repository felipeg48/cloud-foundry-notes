package io.pivotal.cloud;

import java.util.Arrays;
import java.util.Random;

import org.apache.log4j.Logger;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.utils.SerializationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import io.pivotal.cloud.domain.Candidate;

@SpringBootApplication
@EnableScheduling
@EnableRabbit
public class VotersAmqpApplication {

	public static void main(String[] args) {
		SpringApplication.run(VotersAmqpApplication.class, args);
	}
	
	private static final Logger log = Logger.getLogger(VotersAmqpApplication.class);
	private static final Random RANDOM = new Random();
	private static final int MAX_VOTES = 10;
	private static final String EXCHANGE = "polls";
	private static Candidate[] candidates = new Candidate[]{
		new Candidate("T",0),new Candidate("H",0)	
	};
	
	@Autowired
	RabbitTemplate template;
	
	@Scheduled(fixedDelay=1000L)
	private void sendVotes(){
		int selection = 0;
		Candidate candidate = null;
		for(int i = 1; i<=10; i++){
			selection = RANDOM.nextInt(2);
			candidate = candidates[selection];
			candidate.setVote(RANDOM.nextInt(MAX_VOTES)+1);
			
			log.info("Sending: " + candidate);
			Message message = MessageBuilder.withBody(SerializationUtils.serialize(candidate)).build();
			message = template.sendAndReceive(EXCHANGE, candidate.getName(), message);
			
			if(null!=message){
				candidate = (Candidate) SerializationUtils.deserialize(message.getBody());
				candidate.setTotal(candidate.getTotal() + candidate.getVote());
				candidates[selection] = candidate;
			}
		}
		
		log.info("So far...");
		Arrays.stream(candidates).forEach( x -> log.info(x));
	}
	
	@Bean
	TopicExchange electionPolls(){
		return new TopicExchange(EXCHANGE,true,false);
	}

}
