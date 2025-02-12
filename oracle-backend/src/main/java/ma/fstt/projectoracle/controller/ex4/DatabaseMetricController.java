package ma.fstt.projectoracle.controller.ex4;

import ma.fstt.projectoracle.entity.DatabaseMetric;
import ma.fstt.projectoracle.service.ex4.DatabaseMetricService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class DatabaseMetricController {
    @Autowired
    private DatabaseMetricService databaseService;

    @GetMapping("/api/awr-report")
    public List<DatabaseMetric> getAwrReport() {
        return databaseService.getAwrReport();
    }

    @GetMapping("/api/ash-report")
    public List<DatabaseMetric> getAshReport() {
        return databaseService.getAshReport();
    }

    @GetMapping("/api/real-time-stats")
    public List<DatabaseMetric> getRealTimeStats() {
        return databaseService.getRealTimeStats();
    }
}
