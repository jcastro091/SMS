package com.example.text;

import java.io.Serializable;
import java.util.ArrayList;

public class GroupObject implements Serializable {
	private static final long serialVersionUID = 1L;

	private final ArrayList<ContactObject> contacts;
	private final String group_name, message;

	public GroupObject(String name, String message, ArrayList<ContactObject> list){
		this.group_name = name;
		this.message = message;
		this.contacts = list;
	}

	public String getName() {
		return group_name;
	}

	public String getMessage() {
		return message;
	}

	public ArrayList<ContactObject> getContacts() {
		return contacts;
	}

}
