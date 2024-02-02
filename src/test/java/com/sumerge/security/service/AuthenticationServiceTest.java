package com.sumerge.security.service;

import com.sumerge.security.component.JwtService;
import com.sumerge.security.dto.AuthenticationRequest;
import com.sumerge.security.dto.AuthenticationResponse;
import com.sumerge.security.dto.RegisterRequest;
import com.sumerge.security.model.*;
import com.sumerge.security.repository.UserRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;


@RunWith(SpringRunner.class)
public class AuthenticationServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private  PasswordEncoder passwordEncoder;

    @Mock
    private  JwtService jwtService;

    @Mock
    private  AuthenticationManager authenticationManager;

    private AutoCloseable autoCloseable;
    private AuthenticationService underTest;

    @Before
    public void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        underTest = new AuthenticationService(userRepository,passwordEncoder,jwtService,authenticationManager);
    }

    @After
    public void tearDown() throws Exception {

        autoCloseable.close();
    }

    @Test
    public void register() {
        User mockUser=new User(1,"za@gmail.com","123456",Role.USER);

        given(userRepository.findByEmail("za@gmail.com")).willReturn(Optional.empty());
        when(jwtService.generateToken(any(User.class))).thenReturn("jwtToken");
        RegisterRequest request = new RegisterRequest("za@gmail.com","123456",Role.USER);

        AuthenticationResponse response = underTest.register(request);

        assertNotNull(response);
        assertEquals(request.getEmail(), response.getEmail());
        assertEquals("jwtToken",response.getAccessToken());
        assertNotNull(response.getExpiration());


    }

    @Test
    public void authenticate() {
        User mockUser=new User(1,"za@gmail.com","123456",Role.USER);

        given(userRepository.findByEmail("za@gmail.com")).willReturn(Optional.of(mockUser));
        when(jwtService.generateToken(any(User.class))).thenReturn("jwtToken");

        AuthenticationRequest request = new AuthenticationRequest("za@gmail.com","123456");

        AuthenticationResponse response = underTest.authenticate(request);

        assertNotNull(response);
        assertEquals(request.getEmail(), response.getEmail());
        assertEquals("jwtToken",response.getAccessToken());
        assertNotNull(response.getExpiration());
    }

    @Test
    public void registerExistingEmail() {
        User mockUser=new User(1,"za@gmail.com","123456",Role.USER);

        given(userRepository.findByEmail("za@gmail.com")).willReturn(Optional.of(mockUser));
        Exception exception = assertThrows(RuntimeException.class, () -> {
            underTest.register(null);
        }


        );



    }

}