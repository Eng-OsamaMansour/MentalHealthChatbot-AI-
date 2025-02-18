package org.emocare.emocare.controller;

import jakarta.validation.Valid;
import org.emocare.emocare.dto.UserDto;
import org.emocare.emocare.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Validated
@RequestMapping(path = "/user")
public class UserController
{
    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<List<UserDto>> readAll()
    {
        List<UserDto> users = userService.readAll();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping ("/{username}")
    public ResponseEntity<UserDto> read(@PathVariable String username)
    {
        UserDto user = userService.read(username);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<UserDto> create(@RequestBody @Valid UserDto user)
    {
        UserDto lUser = userService.create(user);
        return new ResponseEntity<>(lUser, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<UserDto> update(@RequestBody @Valid UserDto user)
    {
        UserDto lUser = userService.update(user);
        return new ResponseEntity<>(lUser, HttpStatus.OK);
    }

    @DeleteMapping("/{username}")
    public ResponseEntity<Void> delete(@PathVariable String username)
    {
        userService.delete(username);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
