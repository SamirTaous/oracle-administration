package ma.fstt.projectoracle.controller.ex1;

import ma.fstt.projectoracle.service.ex1.QuotaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api")
public class QuotaController {

    @Autowired
    private QuotaService quotaService;

    // Set quota for a user on a tablespace
    @PostMapping("/quotas")
    public ResponseEntity<String> setQuota(@RequestParam String tablespaceName,
                                           @RequestParam String userName,
                                           @RequestParam String quotaSize) {
        quotaService.setQuota(tablespaceName, userName, quotaSize);
        return ResponseEntity.ok("Quota set successfully for user");
    }

    // Remove quota for a user on a tablespace
    @DeleteMapping("/quotas")
    public ResponseEntity<String> removeQuota(@RequestParam String tablespaceName,
                                              @RequestParam String userName) {
        quotaService.removeQuota(tablespaceName, userName);
        return ResponseEntity.ok("Quota removed successfully for user");
    }

    // Create a password policy (via profile)
    @PostMapping("/password-policies")
    public ResponseEntity<String> createPasswordPolicy(@RequestParam String profileName,
                                                       @RequestParam String passwordSettings) {
        quotaService.createPasswordPolicy(profileName, passwordSettings);
        return ResponseEntity.ok("Password policy created successfully");
    }

    // Assign password policy (profile) to a user
    @PostMapping("/password-policies/assign")
    public ResponseEntity<String> assignPasswordPolicyToUser(@RequestParam String profileName,
                                                             @RequestParam String userName) {
        quotaService.assignPasswordPolicyToUser(profileName, userName);
        return ResponseEntity.ok("Password policy assigned to user successfully");
    }

    // Update password policy (profile settings)
    @PutMapping("/password-policies")
    public ResponseEntity<String> updatePasswordPolicy(@RequestParam String profileName,
                                                       @RequestParam String passwordSettings) {
        quotaService.updatePasswordPolicy(profileName, passwordSettings);
        return ResponseEntity.ok("Password policy updated successfully");
    }

    // Delete password policy (profile)
    @DeleteMapping("/password-policies")
    public ResponseEntity<String> deletePasswordPolicy(@RequestParam String profileName) {
        quotaService.deletePasswordPolicy(profileName);
        return ResponseEntity.ok("Password policy deleted successfully");
    }
}

