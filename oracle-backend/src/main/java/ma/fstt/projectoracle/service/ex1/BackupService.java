package ma.fstt.projectoracle.service.ex1;

import ma.fstt.projectoracle.entity.BackupHistory;
import ma.fstt.projectoracle.repository.BackupHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class BackupService {

    @Autowired
    private TaskScheduler taskScheduler;

    // This will hold the scheduled task reference
    private Runnable scheduledBackupTask;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private BackupHistoryRepository backupHistoryRepository;

    // Path to Docker (ensure this matches the actual path to docker on your system)
    private static final String DOCKER_PATH = "/usr/bin/docker";

    public void executeRmanCommand(String command) throws IOException, InterruptedException {
        String oracleContainerName = "oracle-db";
        String oracleHost = "oracle-db";
        String oraclePort = "1521";
        String oracleSid = "orclpdb1";
        String oracleUsername = "sys as sysdba";
        String oraclePassword = "samirT080317";

        String rmanCommand = String.format(
                "%s exec -it %s rman target /",
                DOCKER_PATH, oracleContainerName, oracleUsername, oraclePassword, oracleHost, oraclePort, oracleSid
        );

        ProcessBuilder processBuilder = new ProcessBuilder(rmanCommand.split(" "));

        // Add the PATH to the environment variables
        Map<String, String> environment = processBuilder.environment();
        environment.put("PATH", "/usr/bin:/bin:/usr/sbin:/sbin:/usr/local/bin");

        processBuilder.redirectErrorStream(true);
        Process process = processBuilder.start();

        try (OutputStreamWriter writer = new OutputStreamWriter(process.getOutputStream());
             BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {

            writer.write(command + "\n");
            writer.write("EXIT;\n");
            writer.flush();

            // Read output asynchronously
            new Thread(() -> {
                try {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        System.out.println(line);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();

            // Wait for the process to complete with a timeout
            boolean completed = process.waitFor(10, TimeUnit.MINUTES);
            if (!completed) {
                process.destroy();
                throw new RuntimeException("RMAN command timed out.");
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new IOException("Error executing RMAN command", e);
        }
    }

    // Trigger a manual backup
    public void triggerBackup(String backupType) throws IOException, InterruptedException {
        String command = backupType.equalsIgnoreCase("incremental")
                ? "BACKUP INCREMENTAL LEVEL 1 DATABASE;"
                : "BACKUP DATABASE PLUS ARCHIVELOG;";

        executeRmanCommand(command);

        // Save backup details to the database
        BackupHistory backup = new BackupHistory();
        backup.setBackupType(backupType);
        backup.setBackupDate(new java.util.Date());
        backupHistoryRepository.save(backup);
    }

    // Restore a backup
    public void restoreBackup() throws IOException, InterruptedException {
        String command = "RUN { RESTORE DATABASE ; RECOVER DATABASE; }";
        executeRmanCommand(command);
    }

    public void restoreBackupFromSCN(String scn) throws IOException, InterruptedException {
        String command = "RUN { RESTORE DATABASE FROM SCN " + scn + "; RECOVER DATABASE; }";
        executeRmanCommand(command);
    }

    public void restoreBackupFromDate(String date) throws IOException, InterruptedException {
        String command = "RUN { RESTORE DATABASE FROM TIME \"" + date + "\"; RECOVER DATABASE; }";
        executeRmanCommand(command);
    }

    public String listBackup() throws IOException, InterruptedException {
        String command = "LIST BACKUP SUMMARY;";
        return executeRmanCommandAndReturnOutput(command);
    }

    public String executeRmanCommandAndReturnOutput(String command) throws IOException, InterruptedException {
        String oracleContainerName = "oracle-db";
        String oracleHost = "oracle-db2";
        String oraclePort = "1521";
        String oracleSid = "orclpdb1";

        String rmanCommand = String.format(
                "%s exec -i %s rman target ayman/ayman@//%s:%s/%s",
                DOCKER_PATH, oracleContainerName
        );

        ProcessBuilder processBuilder = new ProcessBuilder(rmanCommand.split(" "));
        processBuilder.redirectErrorStream(true);
        Process process = processBuilder.start();

        StringBuilder output = new StringBuilder();

        try (OutputStreamWriter writer = new OutputStreamWriter(process.getOutputStream());
             BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {

            writer.write(command + "\n");
            writer.write("EXIT;\n");
            writer.flush();

            // Read and append the output
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }

            // Wait for the process to complete
            boolean completed = process.waitFor(10, TimeUnit.MINUTES);
            if (!completed) {
                process.destroy();
                throw new RuntimeException("RMAN command timed out.");
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new IOException("Error executing RMAN command", e);
        }

        return output.toString();
    }

    // Scheduled backup (daily at 2 AM by default)
    public List<Map<String, Object>> getBackupSCNs() throws SQLException {
        List<Map<String, Object>> backupDetails = new ArrayList<>();

        String query = "SELECT RECID, START_TIME, COMPLETION_TIME FROM V$BACKUP_SET ORDER BY START_TIME";

        jdbcTemplate.query(query, rs -> {
            while (rs.next()) {
                Map<String, Object> backupInfo = new HashMap<>();
                backupInfo.put("RECID", rs.getLong("RECID"));
                backupInfo.put("START_TIME", rs.getTimestamp("START_TIME"));
                backupInfo.put("COMPLETION_TIME", rs.getTimestamp("COMPLETION_TIME"));
                backupDetails.add(backupInfo);
            }
        });

        return backupDetails;
    }

    public void scheduleBackup(String cronExpression) {
        scheduledBackupTask = () -> {
            try {
                String rmanCommand = "RUN { " +
                        "  ALLOCATE CHANNEL c1 DEVICE TYPE DISK; " +
                        "  BACKUP DATABASE PLUS ARCHIVELOG; " +
                        "  RELEASE CHANNEL c1; " +
                        "}";
                executeRmanCommand(rmanCommand);
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        };

        taskScheduler.schedule(scheduledBackupTask, new CronTrigger(cronExpression));
    }
}
