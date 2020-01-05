package carzanodev.genuniv.microservices.college.api.classroom;

import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
class ClassroomDTO {

    @JsonProperty("id")
    private int id;

    @JsonProperty("name")
    private String name;

    @Data
    @AllArgsConstructor
    static class List {
        @JsonProperty("classrooms")
        private Collection<ClassroomDTO> classrooms;
    }

}
