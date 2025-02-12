package ma.fstt.projectoracle.service.ex1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;



@Service
public class QuotaService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // Set quota for a user on a tablespace
    public void setQuota(String tablespaceName, String userName, String quotaSize) {
        String sql = "ALTER USER " + userName + " QUOTA " + quotaSize + " ON " + tablespaceName;
        jdbcTemplate.execute(sql);
    }

    // Remove quota for a user on a tablespace
    public void removeQuota(String tablespaceName, String userName) {
        String sql = "ALTER USER " + userName + " QUOTA 0 ON " + tablespaceName;
        jdbcTemplate.execute(sql);
    }

    // Create a password policy (via profile)
    public void createPasswordPolicy(String profileName, String passwordSettings) {
        String sql = "CREATE PROFILE " + profileName + " LIMIT " + passwordSettings;
        jdbcTemplate.execute(sql);
    }

    // Assign password policy (profile) to a user
    public void assignPasswordPolicyToUser(String profileName, String userName) {
        String sql = "ALTER USER " + userName + " PROFILE " + profileName;
        jdbcTemplate.execute(sql);
    }

    // Update password policy (profile settings)
    public void updatePasswordPolicy(String profileName, String passwordSettings) {
        String sql = "ALTER PROFILE " + profileName + " LIMIT " + passwordSettings;
        jdbcTemplate.execute(sql);
    }

    // Delete password policy (profile)
    public void deletePasswordPolicy(String profileName) {
        String sql = "DROP PROFILE " + profileName + " CASCADE";
        jdbcTemplate.execute(sql);
    }





















}
