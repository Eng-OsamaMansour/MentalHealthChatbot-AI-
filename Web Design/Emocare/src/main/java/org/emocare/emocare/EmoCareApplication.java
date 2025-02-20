package org.emocare.emocare;

import org.emocare.emocare.model.Role;
import org.emocare.emocare.model.User;
import org.emocare.emocare.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Date;

@SpringBootApplication
public class EmoCareApplication
{

    public static void main(String[] args)
    {
        SpringApplication.run(EmoCareApplication.class, args);
    }

    @Bean
    public CommandLineRunner runner(UserRepository aInUserRepository,
            PasswordEncoder passwordEncoder)
    {
        return args -> {
            if (aInUserRepository.findByUsername("admin").isEmpty())
            {
                User lUser = User.builder()
                        .username("admin")
                        .firstName("emocare")
                        .lastName("admin")
                        .email("admin@emocare.com")
                        .password(passwordEncoder.encode("admin123"))
                        .gender("MALE")
                        .city("Test")
                        .dateOfBirth(new Date())
                        .role(Role.ADMIN)
                        .build();
                aInUserRepository.save(lUser);
            }
        };
    }
}
