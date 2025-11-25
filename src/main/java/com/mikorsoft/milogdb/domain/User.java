package com.mikorsoft.milogdb.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User {

	@Id
	private Long id;
	private String username;
	private String password;

}