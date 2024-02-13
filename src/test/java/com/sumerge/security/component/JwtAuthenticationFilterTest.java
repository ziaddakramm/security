package com.sumerge.security.component;

import com.sumerge.security.component.JwtAuthenticationFilter;
import com.sumerge.security.component.JwtService;
import com.sumerge.security.model.Role;
import com.sumerge.security.model.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class JwtAuthenticationFilterTest {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }


    private static final String validJwtToken = "validJwtToken";
    private static final String invalidJwtToken = "invalidJwtToken";
    private static final String userEmail = "test@gmail.com";

    @Mock
    private JwtService jwtService;

        @Mock
        private UserDetailsService userDetailsService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @InjectMocks
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testValidJwtToken() throws Exception {

        when(request.getHeader("Authorization")).thenReturn("Bearer " + validJwtToken);

        when(jwtService.extractUsername(validJwtToken)).thenReturn(userEmail);

        UserDetails mockUser=new User(1,"za@gmail.com","123456", Role.USER);


        when(userDetailsService.loadUserByUsername(userEmail)).thenReturn(mockUser);

        when(jwtService.isTokenValid(validJwtToken, mockUser)).thenReturn(true);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain, times(1)).doFilter(request, response);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assertNotNull(authentication);
    }

}