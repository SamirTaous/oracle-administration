package ma.fstt.projectoracle.service.ex1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class UserService {



    @Autowired
    private JdbcTemplate jdbcTemplate;

    // Create a new user
    public void createUser(String username, String password) {
        String sql = "CREATE USER " + username + " IDENTIFIED BY " + password;
        jdbcTemplate.execute(sql);
        // Grant privileges
        jdbcTemplate.execute("GRANT CONNECT, RESOURCE TO " + username);
    }

    // List all users
    public List<Map<String, Object>> listUsers() {
        String sql = "SELECT USERNAME, ACCOUNT_STATUS FROM DBA_USERS";
        return jdbcTemplate.queryForList(sql);
    }

    // Update user's password
    public void updateUserPassword(String username, String newPassword) {
        String sql = "ALTER USER " + username + " IDENTIFIED BY " + newPassword;
        jdbcTemplate.execute(sql);
    }



    // Delete a user
    public void deleteUser(String username) {
        String sql = "DROP USER " + username + " CASCADE";
        jdbcTemplate.execute(sql);
    }
}
