package com.example.text;

import java.io.Serializable;

public class ContactObject implements Serializable {
	private static final long serialVersionUID = 1L;

	private String name;
	private String phoneNo;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPhoneNo() {
		return phoneNo;
	}
	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}
	
	
}
