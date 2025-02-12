package ma.fstt.projectoracle.service.ex4;

import ma.fstt.projectoracle.entity.DatabaseMetric;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DatabaseMetricService {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<DatabaseMetric> getAwrReport() {
        return executeQuery("SELECT * FROM dba_hist_snapshot");
    }

    public List<DatabaseMetric> getAshReport() {
        return executeQuery("SELECT * FROM v$active_session_history");
    }

    public List<DatabaseMetric> getRealTimeStats() {
        List<DatabaseMetric> stats = new ArrayList<>();
        try {
            stats.add(new DatabaseMetric("cpu_usage", executeSingleValueQuery("SELECT value FROM v$sysmetric WHERE metric_name = 'CPU Usage Per Sec'")));
            stats.add(new DatabaseMetric("io_throughput", executeSingleValueQuery("SELECT value FROM v$sysmetric WHERE metric_name = 'I/O Throughput'")));
            stats.add(new DatabaseMetric("memory_usage", executeSingleValueQuery("SELECT value FROM v$sysmetric WHERE metric_name = 'Memory Usage'")));
        } catch (Exception e) {
            e.printStackTrace();
            stats.add(new DatabaseMetric("error", e.getMessage()));
        }
        return stats;
    }


    private List<DatabaseMetric> executeQuery(String query) {
        return jdbcTemplate.query(query, (RowMapper<DatabaseMetric>) (rs, rowNum) ->
                new DatabaseMetric(rs.getMetaData().getColumnName(1), rs.getString(1)));
    }

    private String executeSingleValueQuery(String query) {
        try {
            return jdbcTemplate.queryForObject(query, String.class);
        } catch (EmptyResultDataAccessException e) {
            return "No data available"; // Return a default value or log the issue
        } catch (Exception e) {
            e.printStackTrace();
            return "Error: " + e.getMessage();
        }
    }

}
