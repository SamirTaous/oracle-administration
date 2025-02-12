package ma.fstt.projectoracle.controller.ex1;
import ma.fstt.projectoracle.service.ex1.DynamicBackupScheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/schedule")
public class BackupScheduleController {

    @Autowired
    private DynamicBackupScheduler scheduler;

    // Endpoint to update the backup schedule
    @PostMapping("/update")
    public ResponseEntity<String> updateSchedule(@RequestParam String cronExpression) {
        try {
            scheduler.updateSchedule(cronExpression);
            return ResponseEntity.ok("Backup schedule updated successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to update schedule: " + e.getMessage());
        }
    }
}
