package com.sumerge.security.service;

import com.sumerge.security.dto.AuthenticationRequest;
import com.sumerge.security.dto.AuthenticationResponse;
import com.sumerge.security.dto.RegisterRequest;
import com.sumerge.security.component.JwtService;
import com.sumerge.security.model.Role;
import com.sumerge.security.model.User;
import com.sumerge.security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class AuthenticationService {
  private final UserRepository repository;

  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final AuthenticationManager authenticationManager;

  public AuthenticationResponse register(RegisterRequest request) {

    if(emailAlreadyExists(request.getEmail()))
    {
      throw new RuntimeException("email already exists");
    }



    var user = User.builder()
        .email(request.getEmail())
        .password(passwordEncoder.encode(request.getPassword()))
        .role(Role.USER)
        .build();


    var savedUser = repository.save(user);

    var jwtToken = jwtService.generateToken(user);

    return AuthenticationResponse.builder()
        .accessToken(jwtToken)
        .email(user.getEmail())
        .expiration(jwtService.getJwtExpiration())
        .build();
  }

  public AuthenticationResponse authenticate(AuthenticationRequest request) {
    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
            request.getEmail(),
            request.getPassword()
        )
    );
    var user = repository.findByEmail(request.getEmail())
        .orElseThrow();
    var jwtToken = jwtService.generateToken(user);
    return AuthenticationResponse.builder()
        .accessToken(jwtToken)
        .email(user.getEmail())
            .expiration(jwtService.getJwtExpiration())
        .build();
  }


  public boolean emailAlreadyExists(String email)
  {
    Optional<User> user=repository.findByEmail(email);

    if(user.isPresent())
    {
      return true;
    }
    return false;

  }
}

