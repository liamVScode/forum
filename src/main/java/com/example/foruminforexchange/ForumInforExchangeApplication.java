package com.example.foruminforexchange;

import com.example.foruminforexchange.model.Role;
import com.example.foruminforexchange.model.User;
import com.example.foruminforexchange.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class ForumInforExchangeApplication implements CommandLineRunner{

    @Autowired
    private UserRepo userRepo;
    public static void main(String[] args) {
        SpringApplication.run(ForumInforExchangeApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        User adminAccount = userRepo.findByRole(Role.ADMIN);

        if(adminAccount == null){
            User user = new User();

            user.setEmail("admin@gmail.com");
            user.setFirstName("admin");
            user.setLastName("admin");
            user.setRole(Role.ADMIN);
            user.setPassword(new BCryptPasswordEncoder().encode("admin"));
            userRepo.save(user);
        }
    }
}
