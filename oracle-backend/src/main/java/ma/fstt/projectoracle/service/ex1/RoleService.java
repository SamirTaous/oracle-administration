package ma.fstt.projectoracle.service.ex1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class RoleService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    //get all roles
    public List<Map<String, Object>> listRoles() {
        String sql = "SELECT ROLE, ROLE_ID FROM DBA_ROLES";
        return jdbcTemplate.queryForList(sql);
    }//get all profiles
   public List<Map<String, Object>> listProfiles() {
       String sql = "SELECT  PROFILE, RESOURCE_NAME , RESOURCE_TYPE  FROM dba_profiles";
       return jdbcTemplate.queryForList(sql);
   } public void createRole(String roleName) {
        String sql = "CREATE ROLE " + roleName;
        jdbcTemplate.execute(sql);
    } public void grantPrivilegeToRole(String privilege, String roleName) {
        String sql = "GRANT " + privilege + " TO " + roleName;
        jdbcTemplate.execute(sql);
    }

    // assign role to a user
    public void assignRoleToUser(String roleName, String userName) {
        String sql = "GRANT " + roleName + " TO " + userName;
        jdbcTemplate.execute(sql);
    }

    // revoke role from a user
    public void revokeRoleFromUser(String roleName, String userName) {
        String sql = "REVOKE " + roleName + " FROM " + userName;
        jdbcTemplate.execute(sql);
    }

    // delete role
    public void deleteRole(String roleName) {
        String sql = "DROP ROLE " + roleName;
        jdbcTemplate.execute(sql);
    }
    public void updateRoleName(String oldRoleName, String newRoleName) {
        // Dropping and re-creating a role is required as Oracle doesn't support direct role renaming
        String dropRoleSql = "DROP ROLE " + oldRoleName;
        String createRoleSql = "CREATE ROLE " + newRoleName;
        jdbcTemplate.execute(dropRoleSql);
        jdbcTemplate.execute(createRoleSql);
    }

    // create profile
    public void createProfile(String profileName, String resourceType, String limitValue) {
        String sql = "CREATE PROFILE " + profileName + " LIMIT " + resourceType + " " + limitValue;

        jdbcTemplate.execute(sql);
    }

    // assign profile to a user
    public void assignProfileToUser(String profileName, String userName) {
        String sql = "ALTER USER " + userName + " PROFILE " + profileName;
        jdbcTemplate.execute(sql);
    }

    // update profile
    public void updateProfile(String profileName, String resourceLimit) {
        String sql = "ALTER PROFILE " + profileName + " LIMIT " + resourceLimit;
        jdbcTemplate.execute(sql);
    }

    // delete profile
    public void deleteProfile(String profileName) {
        String sql = "DROP PROFILE " + profileName + " CASCADE";
        jdbcTemplate.execute(sql);
    }











}
