package ma.fstt.projectoracle.controller.ex1;

import ma.fstt.projectoracle.service.ex1.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/oracle-users")
@CrossOrigin(origins = "http://localhost:5173")
public class UserController {

    @Autowired
    private UserService oracleUserService;

    // Create a new user
    @PostMapping
    public void createUser(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        String password = body.get("password");
        oracleUserService.createUser(username, password);
    }
    // List all users
    @GetMapping
    public List<Map<String, Object>> listUsers() {
        return oracleUserService.listUsers();
    }

    // Update user password
    @PutMapping("/{username}")
    public void updateUserPassword(@PathVariable String username, @RequestParam String newPassword) {
        oracleUserService.updateUserPassword(username, newPassword);
    }

    // Delete a user
    @DeleteMapping("/{username}")
    public void deleteUser(@PathVariable String username) {
        oracleUserService.deleteUser(username);
    }
}