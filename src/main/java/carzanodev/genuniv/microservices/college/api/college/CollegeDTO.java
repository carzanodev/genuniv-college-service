package carzanodev.genuniv.microservices.college.api.college;

import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
class CollegeDTO {

    @JsonProperty("id")
    private int id;

    @JsonProperty("name")
    private String name;

    @Data
    @AllArgsConstructor
    static class List {
        @JsonProperty("colleges")
        private Collection<CollegeDTO> colleges;
    }

}
