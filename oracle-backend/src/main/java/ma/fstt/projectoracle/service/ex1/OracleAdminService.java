package ma.fstt.projectoracle.service.ex1;

import ma.fstt.projectoracle.exceptions.UserCreationException;
import ma.fstt.projectoracle.exceptions.UserDeletionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class OracleAdminService {

    private final JdbcTemplate jdbcTemplate;

    public OracleAdminService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void createUser(String username, String password, String tablespace, String quota, String role) {
        try {
            String createUserSql = "CREATE USER " + username + " IDENTIFIED BY " + password +
                    " DEFAULT TABLESPACE " + tablespace;
            System.out.println( createUserSql);
            String quotaSql = "ALTER USER " + username + " QUOTA " + quota + " ON " + tablespace;
            String grantRoleSql = "GRANT " + role + " TO " + username;

            jdbcTemplate.execute(createUserSql);
            jdbcTemplate.execute(quotaSql);
            jdbcTemplate.execute(grantRoleSql);
        } catch (Exception e) {
            throw new UserCreationException("Error creating user: " + e.getMessage(), e);
        }
    }

    public void dropUser(String username) {
        try {
            String sql = "DROP USER " + username + " CASCADE";
            jdbcTemplate.execute(sql);
        } catch (Exception e) {
            throw new UserDeletionException("Error dropping user: " + e.getMessage(), e);
        }
    }

    public List<Map<String, Object>> getAllUsers() {
        String sql = "SELECT USERNAME, ACCOUNT_STATUS, DEFAULT_TABLESPACE, TEMPORARY_TABLESPACE, CREATED FROM DBA_USERS";
        return jdbcTemplate.queryForList(sql);
    }
}
