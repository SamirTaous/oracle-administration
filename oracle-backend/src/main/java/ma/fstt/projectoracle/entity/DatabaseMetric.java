package ma.fstt.projectoracle.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class DatabaseMetric {
    @JsonProperty("metric_name")
    private String metricName;
    @JsonProperty("value")
    private String value;

    public DatabaseMetric(String metricName, String value) {
        this.metricName = metricName;
        this.value = value;
    }
}