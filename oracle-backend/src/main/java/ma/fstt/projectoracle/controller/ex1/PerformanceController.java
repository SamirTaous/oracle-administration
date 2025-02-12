package ma.fstt.projectoracle.controller.ex1;


import ma.fstt.projectoracle.service.ex1.PerformanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/performance")
@CrossOrigin(origins = "http://localhost:5173")
public class PerformanceController {
    @Autowired
    private PerformanceService performanceService;




    // Endpoint to get slow queries
    @GetMapping("/slow-queries")
    public List<Map<String, Object>> getSlowQueries() {
        return performanceService.getSlowQueries();
    }

    // Endpoint to tune SQL based on sqlId
    @PostMapping("/tune-sql")
    public String tuneSQL(@RequestParam String sqlId) {
        return performanceService.createTuningTask(sqlId);
    }

    // Endpoint to recalculate table statistics
    @PostMapping("/recalculate-table-stats")
    public String recalculateTableStats(@RequestParam String schemaName, @RequestParam String tableName) {
        return performanceService.recalculateTableStats(schemaName, tableName);
    }

    // Endpoint to recalculate index statistics
    @PostMapping("/recalculate-index-stats")
    public String recalculateIndexStats(@RequestParam String schemaName, @RequestParam String indexName) {
        return performanceService.recalculateIndexStats(schemaName, indexName);
    }

    // Endpoint to schedule statistics recalculation
    @PostMapping("/schedule-stats-recalculation")
    public String scheduleStatsRecalculation(@RequestParam String schemaName, @RequestParam String repeatInterval) {
        return performanceService.scheduleStatsRecalculation(schemaName, repeatInterval);
    }



    @GetMapping("/indexes-and-tables/{schemaName}")
    public List<String> getStatsForIndexesAndTables(@PathVariable String schemaName) {
        return performanceService.getStatsForIndexesAndTables(schemaName);
    }


    @GetMapping("/scheduled-jobs")
    public List<String> getScheduledJobs() {
        return performanceService.getScheduledJobs();
    }













}

