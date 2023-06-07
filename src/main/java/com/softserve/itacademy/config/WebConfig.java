package com.softserve.itacademy.config;

import com.softserve.itacademy.model.ToDo;
import com.softserve.itacademy.model.User;
import com.softserve.itacademy.repository.RoleRepository;
import com.softserve.itacademy.repository.ToDoRepository;
import com.softserve.itacademy.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.time.LocalDateTime;
import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/login-form");
    }

    @Bean
    public CommandLineRunner dataLoader(UserRepository userRepo, RoleRepository roleRepo, ToDoRepository toDoRepository, PasswordEncoder encoder) {
        return args -> {

            ToDo toDo1 = new ToDo();
            toDo1.setId(55L);
            toDo1.setTitle("Anna's todo");
            toDo1.setCreatedAt(LocalDateTime.now());

            ToDo toDo2 = new ToDo();
            toDo2.setId(56);
            toDo2.setTitle("Eric's todo");
            toDo2.setCreatedAt(LocalDateTime.now());

            User user = new User();
            user.setFirstName("Ann");
            user.setLastName("Woloshka");
            user.setEmail("user@gmail.com");
            user.setPassword(encoder.encode("password"));
            user.setRole(roleRepo.getOne(1L));
            user.setMyTodos(List.of(toDo1));
            userRepo.save(user);

            User admin = new User();
            admin.setFirstName("Eric");
            admin.setLastName("Konopolsky");
            admin.setEmail("admin@gmail.com");
            admin.setPassword(encoder.encode("password"));
            admin.setRole(roleRepo.getOne(1L));
            admin.setMyTodos(List.of(toDo2));
            userRepo.save(admin);

            toDo1.setOwner(user);
            toDoRepository.save(toDo1);
            toDo2.setOwner(admin);
            toDoRepository.save(toDo2);
        };
    }
}
