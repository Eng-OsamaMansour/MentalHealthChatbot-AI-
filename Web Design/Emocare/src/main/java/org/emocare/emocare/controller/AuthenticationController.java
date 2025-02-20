package org.emocare.emocare.controller;

import jakarta.validation.Valid;
import org.emocare.emocare.dto.AuthenticationRequest;
import org.emocare.emocare.dto.AuthenticationResponse;
import org.emocare.emocare.dto.UserDto;
import org.emocare.emocare.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("auth")
public class AuthenticationController
{
    @Autowired
    AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<String> register(
            @RequestBody @Valid UserDto aInUserDto)
    {
        authenticationService.register(aInUserDto);
        return new ResponseEntity<>("Registered", HttpStatus.ACCEPTED);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request)
    {
        return new ResponseEntity<>(authenticationService.authenticate(request),
                HttpStatus.ACCEPTED);
    }
}
