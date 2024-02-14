package com.sumerge.security.controller;

import com.sumerge.security.dto.AuthenticationRequest;
import com.sumerge.security.dto.AuthenticationResponse;
import com.sumerge.security.service.AuthenticationService;
import com.sumerge.security.dto.RegisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@CrossOrigin
@RestController
@RequestMapping("${controller-prefix}")
@RequiredArgsConstructor
public class AuthenticationController {

  private final AuthenticationService service;

  @PostMapping("/register")
  public ResponseEntity<AuthenticationResponse> register(
      @RequestBody RegisterRequest request
  ) {

    return ResponseEntity.ok(service.register(request));
  }
  @PostMapping("/authenticate")
  public ResponseEntity<AuthenticationResponse> authenticate(
      @RequestBody AuthenticationRequest request
  ) {

    return ResponseEntity.ok(service.authenticate(request));
  }

  @GetMapping("/validate")
  public void validate() {

  }
}
