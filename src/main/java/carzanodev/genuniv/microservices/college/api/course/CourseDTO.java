package carzanodev.genuniv.microservices.college.api.course;

import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
class CourseDTO {

    @JsonProperty("id")
    private long id;

    @JsonProperty("code")
    private String code;

    @JsonProperty("name")
    private String name;

    @JsonProperty("description")
    private String description;

    @JsonProperty("unit")
    private double unit;

    @JsonProperty("lecture_hours")
    private int lectureHours;

    @JsonProperty("lab_hours")
    private int labHours;

    @JsonProperty("college_id")
    private int collegeId;

    @Data
    @AllArgsConstructor
    static class List {
        @JsonProperty("courses")
        private Collection<CourseDTO> courses;
    }

}
