package models.jira;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class GetAllSprintsResponse {
    boolean isLast;
    int maxResults;
    int startAt;
    int total;
    List<SprintItem> values;

    @Data
    public static class SprintItem {
        int id;
        String self;
        String state;
        String name;
        String startDate;
        String endDate;
        String completeDate;
        int originBoardId;
        String goal;
    }
}
