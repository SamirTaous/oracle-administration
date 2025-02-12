package ma.fstt.projectoracle.controller.ex1;

import ma.fstt.projectoracle.exceptions.UserCreationException;
import ma.fstt.projectoracle.exceptions.UserDeletionException;
import ma.fstt.projectoracle.service.ex1.OracleAdminService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/oracle")
@CrossOrigin(origins = "http://localhost:5173")
public class OracleAdminController {

    private final OracleAdminService oracleAdminService;

    public OracleAdminController(OracleAdminService oracleAdminService) {
        this.oracleAdminService = oracleAdminService;
    }

    @GetMapping("/users")
    public List<Map<String, Object>> getAllUsers() {
        return oracleAdminService.getAllUsers();
    }

    @PostMapping("/users")
    public String createUser(
            @RequestParam String username,
            @RequestParam String password,
            @RequestParam String tablespace,
            @RequestParam String quota,
            @RequestParam String role
    ) {
        try {
            oracleAdminService.createUser(username, password, tablespace, quota, role);
            return "User " + username + " created successfully.";
        } catch (UserCreationException e) {
            throw new RuntimeException("Error creating user: " + e.getMessage());
        }
    }

    @DeleteMapping("/users/{username}")
    public String dropUser(@PathVariable String username) {
        try {
            oracleAdminService.dropUser(username);
            return "User " + username + " deleted successfully.";
        } catch (UserDeletionException e) {
            throw new RuntimeException("Error deleting user: " + e.getMessage());
        }
    }
}
