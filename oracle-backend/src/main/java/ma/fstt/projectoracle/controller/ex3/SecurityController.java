package ma.fstt.projectoracle.controller.ex3;

import ma.fstt.projectoracle.service.ex3.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/security")
@CrossOrigin(origins = "http://localhost:5173")
public class SecurityController {

    @Autowired
    private SecurityService securityService;

    @PostMapping("/configure-tde")
    public ResponseEntity<String> configureTDE(@RequestParam String password) {
        try {
            securityService.configureTDE(password);
            return ResponseEntity.ok("TDE configured successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("mot de passe incorrecte " + e.getMessage());
        }
    }


    @PostMapping("/encrypt-tablespace")
    public ResponseEntity<String> encryptTablespace(@RequestParam String tablespaceName) {
        try {
            securityService.encryptTablespace(tablespaceName);
            return ResponseEntity.ok("Tablespace " + tablespaceName + " encrypted successfully.");
        } catch (IllegalStateException e) {
            // Handle already-encrypted case
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            // Handle general errors
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error encrypting tablespace: " + e.getMessage());
        }
    }


    // show encrypted ts

    @GetMapping("/encrypted-tablespaces")
    public ResponseEntity<List<Map<String, Object>>> getEncryptedTablespaces() {
        try {
            List<Map<String, Object>> tablespaces = securityService.getEncryptedTablespaces();
            return ResponseEntity.ok(tablespaces);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null); // Return 500 if there's an error
        }
    }

//show key state getEncryptedTablespaces
    @GetMapping("/key-state")
    public ResponseEntity<List<Map<String, Object>>> getKeyState() {
        try {
            List<Map<String, Object>> keyState = securityService.getKeyState();
            return ResponseEntity.ok(keyState);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null); // Return 500 if there's an error
        }
    }













    @PostMapping("/enable-audit")
    public Map<String, String> enableAudit() {
        return securityService.enableAudit();
    }

    @PostMapping("/configure-vpd")
    public Map<String, String> configureVpd(@RequestBody Map<String, String> request) {
        return securityService.configureVpd(request.get("policyName"), request.get("tableName"), request.get("predicate"));
    }









}