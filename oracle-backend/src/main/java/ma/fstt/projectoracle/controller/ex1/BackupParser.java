package ma.fstt.projectoracle.controller.ex1;

import java.util.*;
import java.util.regex.*;
import com.fasterxml.jackson.databind.ObjectMapper;

public class BackupParser {
    public static void main(String[] args) {
        String backupData = "YOUR_BACKUP_DATA";  // Insert your raw backup data string here

        // Define regex patterns to capture key backup details
        String backupSetPattern = "(BS Key\\s+\\d+.*?Completion Time\\s+.*?)(?=BS Key|$)";
        String filePattern = "(File\\s+LV.*?Name\\s+.+?)";

        Pattern backupSetRegex = Pattern.compile(backupSetPattern, Pattern.DOTALL);
        Pattern fileRegex = Pattern.compile(filePattern, Pattern.DOTALL);

        Matcher backupMatcher = backupSetRegex.matcher(backupData);
        List<Map<String, Object>> backupSets = new ArrayList<>();

        while (backupMatcher.find()) {
            String backupSetData = backupMatcher.group(1);

            // Parse the backup set details (e.g., BS Key, Size, Completion Time)
            Map<String, Object> backupSet = new HashMap<>();
            backupSet.put("bsKey", extractValue(backupSetData, "BS Key\\s+(\\d+)"));
            backupSet.put("size", extractValue(backupSetData, "Size\\s+([\\d\\.]+[A-Za-z]+)"));
            backupSet.put("completionTime", extractValue(backupSetData, "Completion Time\\s+(\\S+ \\S+)"));

            // Parse the datafiles in the backup set
            List<Map<String, String>> datafiles = new ArrayList<>();
            Matcher fileMatcher = fileRegex.matcher(backupSetData);
            while (fileMatcher.find()) {
                Map<String, String> datafile = new HashMap<>();
                datafile.put("fileName", extractValue(fileMatcher.group(1), "Name\\s+(.+)"));
                datafiles.add(datafile);
            }

            backupSet.put("datafiles", datafiles);
            backupSets.add(backupSet);
        }

        // Convert the parsed data to JSON (optional)
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String json = objectMapper.writeValueAsString(backupSets);
            System.out.println(json);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Helper method to extract value using regex
    private static String extractValue(String data, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(data);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }
}
