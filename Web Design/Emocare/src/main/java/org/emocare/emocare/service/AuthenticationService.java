package org.emocare.emocare.service;

import org.emocare.emocare.dto.AuthenticationRequest;
import org.emocare.emocare.dto.AuthenticationResponse;
import org.emocare.emocare.dto.UserDto;
import org.emocare.emocare.model.Token;
import org.emocare.emocare.model.User;
import org.emocare.emocare.repository.TokenRepository;
import org.emocare.emocare.security.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.HashMap;

@Service
public class AuthenticationService
{
    @Autowired
    private UserService userService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    TokenRepository tokenRepository;

    public String register(UserDto aInUserDto)
    {
        var user = userService.register(aInUserDto);
        return generateAndSaveActivationToken(user);
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request)
    {
        var auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()));

        var claims = new HashMap<String, Object>();
        var user = ((User) auth.getPrincipal());
        claims.put("username", user.getUsername());

        var jwtToken =
                jwtService.generateToken(claims, (User) auth.getPrincipal());
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    private String generateAndSaveActivationToken(User user)
    {
        // Generate a token
        String generatedToken = generateActivationCode(6);
        var token = Token.builder()
                .token(generatedToken)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusMinutes(15))
                .user(user)
                .build();
        tokenRepository.save(token);

        return generatedToken;
    }

    private String generateActivationCode(int length)
    {
        String characters = "0123456789";
        StringBuilder codeBuilder = new StringBuilder();
        SecureRandom secureRandom = new SecureRandom();
        for (int i = 0; i < length; i++)
        {
            int randomIndex = secureRandom.nextInt(characters.length());
            codeBuilder.append(characters.charAt(randomIndex));
        }
        return codeBuilder.toString();
    }
}
