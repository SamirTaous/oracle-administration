package ma.fstt.projectoracle.service.ex1;

import jakarta.annotation.PostConstruct;
import ma.fstt.projectoracle.entity.BackupSchedule;
import ma.fstt.projectoracle.repository.BackupScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.concurrent.ScheduledFuture;

@Service
public class DynamicBackupScheduler {

    @Autowired
    private BackupScheduleRepository scheduleRepository;

    private ThreadPoolTaskScheduler taskScheduler;
    private ScheduledFuture<?> scheduledTask;

    @PostConstruct
    public void initializeScheduler() {
        taskScheduler = new ThreadPoolTaskScheduler();
        taskScheduler.setPoolSize(1);
        taskScheduler.initialize();

        // Load the initial schedule from the database
        BackupSchedule currentSchedule = scheduleRepository.findAll().stream().findFirst().orElse(null);
        if (currentSchedule != null) {
            scheduleBackupTask(currentSchedule.getCronExpression());
        }
    }

    // Method to execute RMAN commands
    private void executeRmanCommand(String command) throws Exception {
        ProcessBuilder processBuilder = new ProcessBuilder("rman", "target", "/");
        processBuilder.redirectErrorStream(true);

        Process process = processBuilder.start();
        try (OutputStreamWriter writer = new OutputStreamWriter(process.getOutputStream());
             BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {

            writer.write(command);
            writer.flush();

            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        }
    }

    // Method to trigger a backup (full or incremental)
    private void triggerBackup() {
        try {
            String command = "BACKUP DATABASE PLUS ARCHIVELOG;";
            executeRmanCommand(command);
            System.out.println("Backup completed successfully.");
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Failed to execute RMAN backup: " + e.getMessage());
        }
    }

    // Schedule the backup task dynamically
    public void scheduleBackupTask(String cronExpression) {
        if (scheduledTask != null) {
            scheduledTask.cancel(true); // Cancel the existing task
        }

        scheduledTask = taskScheduler.schedule(() -> triggerBackup(), new CronTrigger(cronExpression));
    }

    // Update the schedule and save it to the database
    public void updateSchedule(String cronExpression) {
        BackupSchedule schedule = scheduleRepository.findAll().stream().findFirst().orElse(new BackupSchedule());
        schedule.setCronExpression(cronExpression);
        schedule.setLastModified(new java.util.Date());
        scheduleRepository.save(schedule);

        // Apply the new schedule
        scheduleBackupTask(cronExpression);
    }
}
