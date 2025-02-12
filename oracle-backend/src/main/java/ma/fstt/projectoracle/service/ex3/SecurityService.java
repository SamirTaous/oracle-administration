package ma.fstt.projectoracle.service.ex3;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.stereotype.Service;

import java.sql.CallableStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Service
public class SecurityService {

    @Autowired
    private JdbcTemplate jdbcTemplate;


     // configure tde in you pdb
     public void configureTDE(String password) {
         try {


             // Step 4: Set the master encryption key and force the keystore to be used
             String setKeyForceKeystore =
                     "ADMINISTER KEY MANAGEMENT SET KEY FORCE KEYSTORE IDENTIFIED BY welcome125 WITH BACKUP";
             jdbcTemplate.execute(setKeyForceKeystore);



             // Add more queries if needed for specific TDE configuration in the PDB.

             System.out.println("TDE configuration completed successfully.");
         } catch (Exception e) {
             System.err.println("Error configuring TDE: " + e.getMessage());
             throw new RuntimeException("Failed to configure TDE", e);
         }
     }



    /// show table spaces
    public List<Map<String, Object>> showTablespaces() {
        String query = "SELECT TABLESPACE_NAME, STATUS, CONTENTS, BLOCK_SIZE FROM DBA_TABLESPACES";
        try {
            return jdbcTemplate.queryForList(query);
        } catch (Exception e) {
            System.err.println("Error retrieving tablespaces: " + e.getMessage());
            throw new RuntimeException("Failed to retrieve tablespaces", e);
        }
    }




    public void encryptTablespace(String tablespaceName) {
        if (!isTablespaceEncrypted(tablespaceName)) {
            try {
                String encryptQuery = "ALTER TABLESPACE " + tablespaceName + " ENCRYPTION ONLINE encrypt";
                jdbcTemplate.execute(encryptQuery);
                System.out.println("Tablespace " + tablespaceName + " encrypted successfully.");
            } catch (Exception e) {
                System.err.println("Error encrypting tablespace: " + e.getMessage());
                throw new RuntimeException("Error encrypting tablespace: " + tablespaceName, e);
            }
        } else {
            // Instead of throwing a RuntimeException, log the info
            System.err.println("Tablespace " + tablespaceName + " is already encrypted.");
            throw new IllegalStateException("Tablespace '" + tablespaceName + "' is already encrypted.");
        }
    }


    public List<Map<String, Object>> getEncryptedTablespaces() {
        String query = "SELECT * FROM V$ENCRYPTED_TABLESPACES";
        try {
            return jdbcTemplate.queryForList(query);
        } catch (Exception e) {
            System.err.println("Error retrieving encrypted tablespaces: " + e.getMessage());
            throw new RuntimeException("Failed to retrieve encrypted tablespaces", e);
        }
    }



    public boolean isTablespaceEncrypted(String tablespaceName) {
        String sql = String.format("""
        SELECT encrypted
        FROM dba_tablespaces
        WHERE tablespace_name = '%s';
    """, tablespaceName);

        try {
            // Execute the query and retrieve the result
            String encrypted = jdbcTemplate.queryForObject(sql, String.class);

            // Return true if the tablespace is encrypted, false otherwise
            return "YES".equalsIgnoreCase(encrypted);
        } catch (Exception e) {
            // Handle any exceptions, such as if the tablespace does not exist
            System.err.println("Error checking if tablespace is encrypted: " + e.getMessage());
            return false; // Return false if any error occurs
        }
    }












    public List<Map<String, Object>> getKeyState() {
        String query = "SELECT * FROM V$ENCRYPTION_KEYS";
        try {
            return jdbcTemplate.queryForList(query);
        } catch (Exception e) {
            System.err.println("Error retrieving key states: " + e.getMessage());
            throw new RuntimeException("Failed to retrieve key states", e);
        }
    }















    // show all tables
    // encrypt a tablespace
    //encrypt /a table















    public Map<String, String> enableAudit() {
        try {
            jdbcTemplate.execute("AUDIT ALL BY ACCESS");
            return Map.of("status", "Auditing enabled successfully");
        } catch (Exception e) {
            e.printStackTrace();
            return Map.of("status", "Error enabling auditing", "error", e.getMessage());
        }
    }

    public Map<String, String> configureVpd(String policyName, String tableName, String predicateFunction) {
        try {
            String sql = "BEGIN DBMS_RLS.ADD_POLICY(object_schema => 'DBA_USER', object_name => ?, policy_name => ?, function_schema => 'DBA_USER', policy_function => ?); END;";
            jdbcTemplate.update(sql, tableName, policyName, predicateFunction);
            return Map.of("status", "VPD policy added successfully");
        } catch (DataAccessException e) {
            e.printStackTrace();
            return Map.of("status", "Error configuring VPD", "error", e.getMessage());
        }
    }}


