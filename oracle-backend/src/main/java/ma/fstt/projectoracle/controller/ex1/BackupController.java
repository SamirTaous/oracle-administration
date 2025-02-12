package ma.fstt.projectoracle.controller.ex1;

import ma.fstt.projectoracle.service.ex1.BackupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/backup")
@CrossOrigin(origins = "http://localhost:5173")

public class BackupController {

    @Autowired
    private BackupService backupService;

    // Trigger manual backup
    @PostMapping("/manual")
    public ResponseEntity<String> manualBackup(@RequestParam String backupType) {
        try {
            backupService.triggerBackup(backupType);
            return ResponseEntity.ok("Backup triggered successfully!");
        } catch (IOException | InterruptedException e) {
            return ResponseEntity.status(500).body("Failed to trigger backup: " + e.getMessage());
        }
    }

    // Restore backup
    @PostMapping("/restore")
    public ResponseEntity<String> restoreBackup() {
        try {
            backupService.restoreBackup();
            return ResponseEntity.ok("Backup restored successfully!");
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Failed to restore backup: " + e.getMessage());
        } catch (InterruptedException e) {
            return ResponseEntity.status(500).body("Backup restore interrupted: " + e.getMessage());
        }
    }

    @PostMapping("/restore/scn")
    public ResponseEntity<String> restoreBackupFromSCN(@RequestParam String scn) {
        try {
            backupService.restoreBackupFromSCN(scn);
            return ResponseEntity.ok("Backup restored successfully from SCN: " + scn);
        } catch (IOException | InterruptedException e) {
            return ResponseEntity.status(500).body("Failed to restore backup from SCN: " + e.getMessage());
        }
    }

    // Restore backup from a specific date
    @PostMapping("/restore/date")
    public ResponseEntity<String> restoreBackupFromDate(@RequestParam String date) {
        try {
            backupService.restoreBackupFromDate(date);
            return ResponseEntity.ok("Backup restored successfully from date: " + date);
        } catch (IOException | InterruptedException e) {
            return ResponseEntity.status(500).body("Failed to restore backup from date: " + e.getMessage());
        }




    }











    // Get backup history as raw text (old method)
    @GetMapping("/list")
    public ResponseEntity<Object> listBackups() {
        try {
            String rmanOutput = backupService.listBackup(); // Get RMAN output
            List<Map<String, Object>> parsedData = parseRmanOutput(rmanOutput);
            return ResponseEntity.ok(parsedData); // Return parsed JSON
        } catch (IOException | InterruptedException e) {
            return ResponseEntity.status(500).body(Map.of("error", "Error fetching backups: " + e.getMessage()));
        }
    }

    private List<Map<String, Object>> parseRmanOutput(String rmanOutput) {
        List<Map<String, Object>> backups = new ArrayList<>();
        String[] lines = rmanOutput.split("\n");
        boolean dataStart = false;

        for (String line : lines) {
            // Detect the start of the data section
            if (line.startsWith("Key")) {
                dataStart = true;
                continue;
            }

            // Skip non-data lines or separator lines
            if (!dataStart || line.trim().isEmpty() || line.contains("===") || line.contains("-------")) {
                continue;
            }

            // Split columns by whitespace
            String[] columns = line.trim().split("\\s+");
            if (columns.length < 10) {
                continue; // Skip malformed lines
            }

            try {
                Map<String, Object> backup = new LinkedHashMap<>();
                backup.put("Key", Integer.parseInt(columns[0])); // Parse Key
                backup.put("Type", columns[1]);
                backup.put("Level", columns[2]);
                backup.put("Status", columns[3]);
                backup.put("DeviceType", columns[4]);
                backup.put("CompletionTime", columns[5]);
                backup.put("Pieces", Integer.parseInt(columns[6])); // Parse Pieces
                backup.put("Copies", Integer.parseInt(columns[7])); // Parse Copies
                backup.put("Compressed", columns[8]);
                backup.put("Tag", columns[9]);
                backups.add(backup);
            } catch (NumberFormatException e) {
                // Log and skip lines that cause parsing issues
                System.err.println("Skipping malformed line: " + line);
            }
        }

        return backups;
    }



    @GetMapping("/scns")
    public ResponseEntity<List<Map<String, Object>>> getBackupSCNs() {
        try {
            List<Map<String, Object>> scns = backupService.getBackupSCNs();
            return ResponseEntity.ok(scns);
        } catch (SQLException e) {
            return ResponseEntity.status(500).body(null);
        }
    }



    @PostMapping("/schedule")
    public String scheduleBackup(@RequestParam String cronExpression) {
        try {
            backupService.scheduleBackup(cronExpression);
            return "Backup scheduled successfully with cron expression: " + cronExpression;
        } catch (Exception e) {
            e.printStackTrace();
            return "Error scheduling backup: " + e.getMessage();
        }
    }




}
