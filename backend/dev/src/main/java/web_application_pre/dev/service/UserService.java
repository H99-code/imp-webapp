package web_application_pre.dev.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import web_application_pre.dev.entity.Item;
import web_application_pre.dev.entity.User;
import web_application_pre.dev.repository.ItemRepository;
import web_application_pre.dev.repository.UserRepository;


import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;


    @Autowired
    private ItemRepository itemRepository;



    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();



    public void init() {
        hashExistingPasswords();
    }

    @Transactional
    public User registerUser(User user) throws Exception {
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new Exception("Username already exists");
        }

        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new Exception("Email already registered");
        }


        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    public List<Item> getAllItem(){
        return itemRepository.findAll();
    }
    @Transactional

    public User addItemToUser(String userId, Item item) {

        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User nicht gefunden"));


        item.setUser(user);
        user.getItems().add(item);


        itemRepository.save(item);


        return user;
    }









    @Transactional
    public User getUserById(String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        return user;
    }



    @Transactional
    public void deleteItemFromUser(String userId, Long itemId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Item not found"));

        if (!item.getUser().getId().equals(userId)) {
            throw new RuntimeException("Item does not belong to user");
        }
        itemRepository.delete(item);
    }
    @Transactional

    public User loginUser(User user) throws Exception {
        System.out.println("Login attempt for user: " + user.getUsername());

        var optionalUser = userRepository.findByUsername(user.getUsername());

        if (optionalUser.isEmpty()) {
            System.out.println("User not found: " + user.getUsername());
            throw new Exception("Invalid username or password");
        }

        User existingUser = optionalUser.get();
        System.out.println("Stored hashed password: " + existingUser.getPassword());

        // Passwort-Überprüfung
        if (!passwordEncoder.matches(user.getPassword(), existingUser.getPassword())) {
            System.out.println("Password does not match for user: " + user.getUsername());
            throw new Exception("Invalid username or password");
        }

        System.out.println("Login successful for user: " + user.getUsername());
        System.out.println("User ID: " + existingUser.getId());

        return existingUser;
    }



    public void hashExistingPasswords() {
        List<User> users = userRepository.findAll();
        for (User user : users) {
            if (!user.getPassword().startsWith("$2a$")) {
                System.out.println("Hashing password for user: " + user.getUsername());
                user.setPassword(passwordEncoder.encode(user.getPassword()));
                userRepository.save(user);
            }

        }
    }
}