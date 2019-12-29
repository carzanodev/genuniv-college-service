package carzanodev.genuniv.microservices.college.cache.model;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ScheduleDTO {

    @JsonProperty("id")
    private int id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("time_slot")
    private String timeSlot;

    @Data
    @NoArgsConstructor
    public static class List {
        @JsonProperty("schedules")
        private Set<ScheduleDTO> schedules;
    }

}
