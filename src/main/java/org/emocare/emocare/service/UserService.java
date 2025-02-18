package org.emocare.emocare.service;

import lombok.NonNull;
import org.emocare.emocare.dto.UserDto;
import org.emocare.emocare.model.User;
import org.emocare.emocare.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService
{
    @Autowired
    private UserRepository userRepository;

    public List<UserDto> readAll()
    {
        List<User> allUsers = userRepository.findAll();
        List<UserDto> lList =
                allUsers.stream().map(this::mapUserToUserDto).toList();
        return lList;
    }

    public UserDto read(@NonNull String username)
    {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        UserDto userDto = mapUserToUserDto(user);
        return userDto;
    }

    public UserDto create(UserDto user)
    {
        User savedUser = userRepository.save(mapUserDtoToUser(user));
        UserDto userDto = mapUserToUserDto(savedUser);
        return userDto;
    }

    public UserDto update(UserDto user)
    {
        User updatedUser = userRepository.save(mapUserDtoToUser(user));
        UserDto userDto = mapUserToUserDto(updatedUser);
        return userDto;
    }

    public void delete(String username)
    {
        userRepository.deleteById(username);
    }

    private UserDto mapUserToUserDto(User user)
    {
        return UserDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .dateOfBirth(user.getDateOfBirth())
                .gender(user.getGender())
                .build();
    }

    private User mapUserDtoToUser(UserDto user)
    {
        return User.builder()
                .id(user.getId())
                .username(user.getUsername())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .dateOfBirth(user.getDateOfBirth())
                .gender(user.getGender())
                .build();
    }
}
