package io.pivotal.cloud.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.metrics.buffer.BufferMetricReader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.pivotal.cloud.domain.Poll;

@RestController
public class PollController {
	
	@Autowired
	private BufferMetricReader metrics; 
	
	@RequestMapping("/polls/data")
	public String data(){
		return String.format("[{\"candidate\":\"H\",\"total\":%d},{\"candidate\":\"T\",\"total\":%d}]", metrics.findOne(Poll.HMETRIC).getValue().intValue(),metrics.findOne(Poll.TMETRIC).getValue().intValue());
	}
}
