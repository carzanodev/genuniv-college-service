package carzanodev.genuniv.microservices.college.api.offering;

import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OfferingDTO {

    @JsonProperty("id")
    private long id;

    @JsonProperty("course_id")
    private long courseId;

    @JsonProperty("schedule_id")
    private int scheduleId;

    @JsonProperty("faculty_id")
    private long facultyId;

    @JsonProperty("classroom_id")
    private int classroomId;

    @JsonProperty("capacity")
    private int capacity;

    @JsonProperty("school_period_id")
    private int schoolPeriodId;

    @JsonProperty("school_year_id")
    private int schoolYearId;

    @Data
    @AllArgsConstructor
    public static class List {
        @JsonProperty("offerings")
        private Collection<OfferingDTO> offerings;
    }

}
