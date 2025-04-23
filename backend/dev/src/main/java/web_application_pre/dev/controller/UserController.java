package web_application_pre.dev.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import web_application_pre.dev.entity.Item;
import web_application_pre.dev.entity.User;
import web_application_pre.dev.service.UserService;

import java.util.Map;
import java.util.UUID;


@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserService userService;


    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("/{userId}/items")
    public ResponseEntity<?> addItemToUser(@PathVariable String userId, @RequestBody Item item) {
        System.out.println("Item hinzufügen für User ID: " + userId + ", Item: " + item);

        // Überprüfe, ob das Item vollständig ist
        if (item == null || item.getName() == null || item.getLink() == null || item.getType() == null || item.getPrice() == null) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "Das Item ist unvollständig oder ungültig."
            ));
        }

        // Hol den User aus der DB
        User user = userService.getUserById(userId);
        if (user == null) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "User mit der ID " + userId + " nicht gefunden."
            ));
        }

        // Füge das Item zum User hinzu
        User updatedUser = userService.addItemToUser(userId, item);
        if (updatedUser == null) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "Fehler beim Hinzufügen des Items zum User."
            ));
        }

        // Erfolgreiche Antwort
        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Item erfolgreich hinzugefügt",
                "user", updatedUser,
                "item", item
        ));
    }









    @CrossOrigin(origins = "http://localhost:3000")
    @DeleteMapping("/{userId}/items/{itemId}")
    public ResponseEntity<?> deleteItemFromUser(@PathVariable String userId, @PathVariable Long itemId) {
        userService.deleteItemFromUser(userId, itemId);
        return ResponseEntity.ok().build();
    }
    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/{userId}/items")
    public ResponseEntity<?> getUserItems(@PathVariable String userId) {
        try {
            User user = userService.getUserById(userId);
            if (user == null) {
                return ResponseEntity.badRequest().body(Map.of(
                        "success", false,
                        "message", "User nicht gefunden."
                ));
            }
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "items", user.getItems()
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        }
    }



    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers() {
        try {
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "users", userService.getAllUsers()
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        }
    }









    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody User user) {
        try {
            User userAccount = userService.loginUser(user);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Login erfolgreich!",
                    "user", Map.of(
                            "id", userAccount.getId(),
                            "username", userAccount.getUsername(),
                            "email", userAccount.getEmail()
                    )
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        }
    }
    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        try {
            // Generiere eine UUID für den neuen User
            user.setId(UUID.randomUUID().toString());

            User newUser = userService.registerUser(user);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Registration successful!",
                    "user", newUser
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        }
    }


    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/hash-passwords")
    public ResponseEntity<?> hashPasswords() {
        try {
            userService.hashExistingPasswords();
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Alle Passwörter wurden erfolgreich gehashed."
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "Fehler beim Hashen der Passwörter: " + e.getMessage()
            ));
        }
    }
}