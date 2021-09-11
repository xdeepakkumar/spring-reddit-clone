package com.redditclone.service;

import java.time.Instant;
import com.redditclone.dto.AuthenticationResponse;
import java.util.Optional;
import java.util.UUID;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.redditclone.dto.LoginRequest;
import com.redditclone.dto.RegisterRequest;
import com.redditclone.exception.SpringRedditException;
import com.redditclone.model.NotificationEmail;
import com.redditclone.model.User;
import com.redditclone.model.VerificationToken;
import com.redditclone.repository.UserRepository;
import com.redditclone.repository.VerificationTokenRepository;
import com.redditclone.security.JwtProvider;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AuthService {
	
	
	private final PasswordEncoder passwordEncoder;
	private final UserRepository userRepository;
	private final VerificationTokenRepository verificationTokenRepository;
	private final MailService mailService;
	private final AuthenticationManager authenticationManager;
	private final JwtProvider jwtProvider;
	
	@Transactional
	public void signup(RegisterRequest registerRequest) {
		User user = new User();
		user.setUsername(registerRequest.getUsername());
		user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
		user.setEmail(registerRequest.getEmail());
		user.setCreated(Instant.now());
		user.setEnabled(false);
		userRepository.save(user);
		
		String token =  generateVerificationToken(user);
		mailService.sendMail(new NotificationEmail("Please activate your account", user.getEmail(), "Please activate your account which is sent to your email account : " + "http://localhost:8080/api/auth/accountVerification/"+ token));
	}

	private String generateVerificationToken(User user) {
		String token = UUID.randomUUID().toString();
		VerificationToken verificationToken = new VerificationToken();
		verificationToken.setToken(token);
		verificationToken.setUser(user);
		verificationTokenRepository.save(verificationToken);
		return token;
	}
	
	public void verifyAccount(String token) {
		Optional<VerificationToken> verificationToken = verificationTokenRepository.findByToken(token);	
		verificationToken.orElseThrow(() -> new SpringRedditException("Invalid token"));
		fetchUserAndEnable(verificationToken.get());
	}

	@Transactional
	private void fetchUserAndEnable(VerificationToken verificationToken) {
		String username = verificationToken.getUser().getUsername();
		User user = userRepository.findByUsername(username).orElseThrow(()-> new SpringRedditException("User not found with username : " + username));
		user.setEnabled(true);
		userRepository.save(user);
	}

	public AuthenticationResponse login(LoginRequest loginRequest) {
		Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
		SecurityContextHolder.getContext().setAuthentication(authenticate);
		String token = jwtProvider.generateToken(authenticate);
		return new AuthenticationResponse(token, loginRequest.getUsername());
	}
}
