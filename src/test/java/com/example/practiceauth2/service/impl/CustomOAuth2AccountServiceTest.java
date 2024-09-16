package com.example.practiceauth2.service.impl;

import com.example.practiceauth2.model.entity.Account;
import com.example.practiceauth2.service.repository.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

// todo 테스트 완성하기
public class CustomOAuth2AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private CustomOAuth2UserService customOAuth2UserService;

    @Mock
    private OAuth2UserRequest oAuth2UserRequest;

    @Mock
    private OAuth2AccessToken oAuth2AccessToken;

    @Mock
    private DefaultOAuth2UserService defaultOAuth2UserService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /*@Test
    public void testLoadUser_NewUser_UserRepositoryCalled() {
        mockOAuth2UserAttributes("test@example.com", "Test User", "google");

        when(accountRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());

        customOAuth2UserService.loadUser(oAuth2UserRequest);

        //verify(accountRepository, times(1)).findByEmail("test@example.com");
        verify(accountRepository, times(1)).save(any(Account.class));
    }

    @Test
    public void testLoadUser_ExistingUser_UserRepositoryCalled() {
        mockOAuth2UserAttributes("test@example.com", "Test User", "google");

        Set<String> roles = new HashSet<>();
        roles.add("ROLE_USER");
        Account existingUser = new Account("test@example.com", "Test User", "google", roles);
        when(accountRepository.findByEmail("test@example.com")).thenReturn(Optional.of(existingUser));

        customOAuth2UserService.loadUser(oAuth2UserRequest);

        verify(accountRepository, times(1)).findByEmail("test@example.com");
        verify(accountRepository, times(1)).save(existingUser);
    }*/

    @Test
    public void testLoadUser_NewUser_ReturnsCorrectOAuth2User() {
        mockOAuth2UserAttributes("test@example.com", "Test User", "google");

        when(accountRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());

        OAuth2User result = customOAuth2UserService.loadUser(oAuth2UserRequest);

        assertNotNull(result);
        assertEquals("Test User", result.getAttribute("name"));
    }

    @Test
    public void testLoadUser_ExistingUser_ReturnsCorrectOAuth2User() {
        mockOAuth2UserAttributes("test@example.com", "Test User", "google");

        Set<String> roles = new HashSet<>();
        roles.add("ROLE_USER");
        Account existingUser = new Account("test@example.com", "Test User", "google", roles);
        when(accountRepository.findByEmail("test@example.com")).thenReturn(Optional.of(existingUser));

        OAuth2User result = customOAuth2UserService.loadUser(oAuth2UserRequest);

        assertNotNull(result);
        assertEquals("Test User", result.getAttribute("name"));
    }

    private void mockOAuth2UserAttributes(String email, String name, String provider) {
        ClientRegistration clientRegistration = ClientRegistration.withRegistrationId(provider)
                .clientId("client-id")
                .clientSecret("client-secret")
                .scope("scope")
                .authorizationUri("http://localhost/auth")
                .tokenUri("http://localhost/token")
                .userInfoUri("http://localhost/userinfo")
                .userNameAttributeName("id")
                .clientName("client-name")
                .redirectUri("http://localhost/callback")
                .authorizationGrantType(new AuthorizationGrantType("authorization_code"))
                .build();

        when(oAuth2UserRequest.getClientRegistration()).thenReturn(clientRegistration);
        when(oAuth2UserRequest.getAccessToken()).thenReturn(oAuth2AccessToken);
        when(oAuth2AccessToken.getTokenValue()).thenReturn("mock-token-value");

        // Create a map ensuring no null values
        Map<String, Object> attributes = Map.of(
                "email", email,
                "name", name,
                "login", "login"
        );

        // Mock the OAuth2UserService to return the mocked OAuth2User
        OAuth2User mockedOAuth2User = new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),
                attributes,
                "name"
        );
        when(defaultOAuth2UserService.loadUser(oAuth2UserRequest)).thenReturn(mockedOAuth2User);

        // Use the spy for customOAuth2UserService to mock loadUser method correctly
        customOAuth2UserService = spy(customOAuth2UserService);
        doReturn(mockedOAuth2User).when(customOAuth2UserService).loadUser(oAuth2UserRequest);
    }
}