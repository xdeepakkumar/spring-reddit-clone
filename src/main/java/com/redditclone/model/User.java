package com.redditclone.model;

import java.time.Instant;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long userId;
	@NotBlank(message = "username cannot be null or empty")
	private String username;
	@NotBlank(message = "password cannot be null or empty")
	private String password;
	@Email
	@NotBlank(message = "Post name cannot be null or empty")
	private String email;
	private Instant created;
	private boolean enabled;
}
