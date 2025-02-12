package ma.fstt.projectoracle.controller.ex1 ;
import ma.fstt.projectoracle.service.ex1.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api")
public class RoleController {

    @Autowired
    private RoleService roleService;

    // Get all roles
    @GetMapping("/roles")
    public ResponseEntity<List<Map<String, Object>>> getAllRoles() {
        List<Map<String, Object>> roles = roleService.listRoles();
        return ResponseEntity.ok(roles);
    }

    // Get all profiles
    @GetMapping("/profiles")
    public ResponseEntity<List<Map<String, Object>>> getAllProfiles() {
        List<Map<String, Object>> profiles = roleService.listProfiles();
        return ResponseEntity.ok(profiles);
    }

    // Create role
    @PostMapping("/roles")
    public ResponseEntity<String> createRole(@RequestParam String roleName) {
        roleService.createRole(roleName);
        return ResponseEntity.ok("Role created successfully");
    }

    // Grant privilege to a role
    @PostMapping("/roles/grant")
    public ResponseEntity<String> grantPrivilegeToRole(@RequestParam String privilege, @RequestParam String roleName) {
        roleService.grantPrivilegeToRole(privilege, roleName);
        return ResponseEntity.ok("Privilege granted to role successfully");
    }

    // Assign role to a user
    @PostMapping("/roles/assign")
    public ResponseEntity<String> assignRoleToUser(@RequestParam String roleName, @RequestParam String userName) {
        roleService.assignRoleToUser(roleName, userName);
        return ResponseEntity.ok("Role assigned to user successfully");
    }

    // Revoke role from a user
    @PostMapping("/roles/revoke")
    public ResponseEntity<String> revokeRoleFromUser(@RequestParam String roleName, @RequestParam String userName) {
        roleService.revokeRoleFromUser(roleName, userName);
        return ResponseEntity.ok("Role revoked from user successfully");
    }

    // Delete role
    @DeleteMapping("/roles")
    public ResponseEntity<String> deleteRole(@RequestParam String roleName) {
        roleService.deleteRole(roleName);
        return ResponseEntity.ok("Role deleted successfully");
    }

    // Update role name
    @PutMapping("/roles")
    public ResponseEntity<String> updateRoleName(@RequestParam String oldRoleName, @RequestParam String newRoleName) {
        roleService.updateRoleName(oldRoleName, newRoleName);
        return ResponseEntity.ok("Role name updated successfully");
    }

    // Create profile
    @PostMapping("/profiles/create")
    public String createProfile(@RequestParam String profileName,
                                @RequestParam String resourceType,
                                @RequestParam String limitValue) {
        try {
            // Pass the three arguments to the service method
            roleService.createProfile(profileName, resourceType, limitValue);
            return "Profile " + profileName + " created successfully with the resource "
                    + resourceType + " and limit " + limitValue + "!";
        } catch (Exception e) {
            return "Error creating profile: " + e.getMessage();
        }
    }

    // Assign profile to a user
    @PostMapping("/profiles/assign")
    public ResponseEntity<String> assignProfileToUser(@RequestParam String profileName, @RequestParam String userName) {
        roleService.assignProfileToUser(profileName, userName);
        return ResponseEntity.ok("Profile assigned to user successfully");
    }

    // Update profile
    @PutMapping("/profiles")
    public ResponseEntity<String> updateProfile(@RequestParam String profileName, @RequestParam String resourceLimit) {
        roleService.updateProfile(profileName, resourceLimit);
        return ResponseEntity.ok("Profile updated successfully");
    }

    // Delete profile
    @DeleteMapping("/profiles")
    public ResponseEntity<String> deleteProfile(@RequestParam String profileName) {
        roleService.deleteProfile(profileName);
        return ResponseEntity.ok("Profile deleted successfully");
    }
}
