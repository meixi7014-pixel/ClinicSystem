package com.example.app.domain;

import lombok.Data;

@Data
public class Admin {
	private String id;
	private String name;
	private String passwordHash;
}
