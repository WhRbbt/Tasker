package tasker.domain.data;

import java.time.LocalDate;
import java.util.List;

public record Task(String name, String description, LocalDate deadline, TaskStatus status, List<String> tags) {}