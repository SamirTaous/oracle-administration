package ma.fstt.projectoracle.service.ex1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.*;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Types;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.atomic.AtomicReference;
import java.util.random.RandomGenerator;

@Service
public class PerformanceService {
    @Autowired
    private JdbcTemplate jdbcTemplate;



    public List<Map<String, Object>> getSlowQueries() {
        String query = "SELECT SQL_TEXT  , SQL_ID FROM v$sql WHERE elapsed_time > 1000000 ORDER BY elapsed_time DESC";
        return jdbcTemplate.queryForList(query);
    }
    public String createTuningTask(String sqlId) {
        String taskName = "Tuning_Tasks_" + sqlId;
        if (taskName.length() > 30) {
            throw new IllegalArgumentException("Generated task name exceeds Oracle's 30-character limit.");
        }

        String plsql = """
            DECLARE
              v_task_name VARCHAR2(30);
            BEGIN
              v_task_name := DBMS_SQLTUNE.CREATE_TUNING_TASK(
                sql_id           => :sqlId,
                scope            => 'COMPREHENSIVE',
                time_limit       => 60,
                task_name        => :taskName,
                description      => 'Tuning for SQL query optimization'
              );
            END;
            """;

        try {
            jdbcTemplate.execute((ConnectionCallback<PreparedStatement>) connection -> {
                try (PreparedStatement ps = connection.prepareStatement(plsql)) {
                    ps.setString(1, sqlId);
                    ps.setString(2, taskName);
                    return ps;
                }
            });
        } catch (DataAccessException e) {
            // Log and rethrow as a runtime exception
            throw new RuntimeException("Error creating tuning task for SQL ID: " + sqlId, e);
        }

        return taskName;
    }

    public String recalculateTableStats(String schemaName, String tableName) {
        String plsql = """    
        
                BEGIN
                    DBMS_STATS.GATHER_TABLE_STATS(
                        ownname => :schemaName,
                        tabname =>  :tableName ,
                        estimate_percent => 100,
                        cascade => TRUE
                    );
                END;
                /
                        
        
        
        
        
        
        """ ;
        try {
            jdbcTemplate.execute((ConnectionCallback<PreparedStatement>) connection -> {
                try (PreparedStatement ps = connection.prepareStatement(plsql)) {
                    ps.setString(1, schemaName);
                    ps.setString(2, tableName);
                    return ps;
                }
            });
        } catch (DataAccessException e) {
            // Log and rethrow as a runtime exception
            throw new RuntimeException("Error creating tuning task for SQL ID: " + schemaName, e);
        }
        return tableName;
    }



    public String recalculateIndexStats(String schemaName, String indexName) {
        String plsql = """    
        
                BEGIN
                    DBMS_STATS.GATHER_TABLE_STATS(
                        ownname => :schemaName,
                        indname =>  :indexName ,
                        estimate_percent => 100,
                        cascade => TRUE
                    );
                END;
                /
                        
        
        
        
        
        
        """ ;
        try {
            jdbcTemplate.execute((ConnectionCallback<PreparedStatement>) connection -> {
                try (PreparedStatement ps = connection.prepareStatement(plsql)) {
                    ps.setString(1, schemaName);
                    ps.setString(2, indexName);
                    return ps;
                }
            });
        } catch (DataAccessException e) {
            // Log and rethrow as a runtime exception
            throw new RuntimeException("Error creating tuning task for SQL ID: " + schemaName, e);
        }
        return indexName;
    }











    public String scheduleStatsRecalculation(String schemaName, String repeatInterval) {
        String randomSuffix = String.valueOf(new Random().nextInt(100000)); // Random number between 0 and 99,999
        String jobName = "RECALC_STATS_JOB_" + schemaName.toUpperCase() + "_" + randomSuffix;
        String sql = """
        BEGIN 
            DBMS_SCHEDULER.create_job(
                job_name        => '%s',
                job_type        => 'PLSQL_BLOCK',
                job_action      => 'BEGIN DBMS_STATS.GATHER_SCHEMA_STATS(ownname => ''%s''); END;',
                start_date      => SYSTIMESTAMP,
                repeat_interval => '%s',
                enabled         => TRUE
            );
        END;
    """.formatted(jobName, schemaName, repeatInterval);

        try {
            jdbcTemplate.execute(sql);
            return "Job scheduled successfully for schema: " + schemaName;
        } catch (DataAccessException e) {
            throw new RuntimeException("Error scheduling stats recalculation for schema: " + schemaName, e);
        }
    }



    public List<String> getStatsForIndexesAndTables(String schemaName) {
        String sql = """
            SELECT table_name, index_name, num_rows, distinct_keys, leaf_blocks 
            FROM user_indexes 
            WHERE table_owner = :schemaName
        """;
        try {
            return jdbcTemplate.query(sql, new Object[]{schemaName}, (rs, rowNum) -> {
                String tableName = rs.getString("table_name");
                String indexName = rs.getString("index_name");
                int numRows = rs.getInt("num_rows");
                int distinctKeys = rs.getInt("distinct_keys");
                int leafBlocks = rs.getInt("leaf_blocks");
                return String.format("Table: %s, Index: %s, Rows: %d, Distinct Keys: %d, Leaf Blocks: %d",
                        tableName, indexName, numRows, distinctKeys, leafBlocks);
            });
        } catch (DataAccessException e) {
            throw new RuntimeException("Error retrieving stats for indexes and tables in schema: " + schemaName, e);
        }
    }

    // Method to get the list of scheduled jobs
    public List<String> getScheduledJobs() {
        String sql = """
            SELECT job_name, state, last_start_date, next_run_date 
            FROM all_scheduler_jobs 
            WHERE owner = USER
        """;
        try {
            return jdbcTemplate.query(sql, (rs, rowNum) -> {
                String jobName = rs.getString("job_name");
                String state = rs.getString("state");
                String lastStartDate = rs.getString("last_start_date");
                String nextRunDate = rs.getString("next_run_date");
                return String.format("Job: %s, State: %s, Last Start: %s, Next Run: %s",
                        jobName, state, lastStartDate, nextRunDate);
            });
        } catch (DataAccessException e) {
            throw new RuntimeException("Error retrieving scheduled jobs", e);
        }
    }













}

