package io.pivotal.cloud.domain;

import java.io.Serializable;

public class Candidate implements Serializable{
	
	private static final long serialVersionUID = 13533491683647033L;
	private String name;
	private int vote = 0;
	private int total = 0;
	private String code;
	
	public Candidate(String name, int vote) {
		super();
		this.name = name;
		this.vote = vote;
	}

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public int getVote() {
		return vote;
	}
	
	public void setVote(int vote) {
		this.vote = vote;
	}
	
	public int getTotal() {
		return total;
	}
	
	public void setTotal(int total) {
		this.total = total;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
	@Override
	public String toString() {
		return "Candidate [name=" + name + ", vote=" + vote + ", total=" + total + ", code=" + code + "]";
	}
	
}
