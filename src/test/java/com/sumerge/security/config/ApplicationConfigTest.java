package com.sumerge.security.config;

import com.sumerge.security.model.Role;
import com.sumerge.security.model.User;
import com.sumerge.security.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;

import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ApplicationConfigTest {


    @Mock
    private UserRepository userRepository;

    @Mock
    private AuthenticationConfiguration authConfig;

    @InjectMocks
    private ApplicationConfig applicationConfig;
    User mockUser = new User(1, "za@gmail.com", "123456", Role.USER);

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testUserDetailsService() {
        when(userRepository.findByEmail("za@gmail.com")).thenReturn(Optional.of(mockUser));

        assertNotNull(applicationConfig.userDetailsService().loadUserByUsername("za@gmail.com"));

        verify(userRepository, times(1)).findByEmail("za@gmail.com");
    }


    @Test
    public void testAuthenticationProvider() {
        assertNotNull(applicationConfig.authenticationProvider());
    }

    @Test
    public void testPasswordEncoder() {
        assertNotNull(applicationConfig.passwordEncoder());
    }
}


