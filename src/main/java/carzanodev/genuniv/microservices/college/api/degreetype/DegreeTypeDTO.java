package carzanodev.genuniv.microservices.college.api.degreetype;

import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
class DegreeTypeDTO {

    @JsonProperty("id")
    private int id;

    @JsonProperty("name")
    private String name;

    @Data
    @AllArgsConstructor
    static class List {
        @JsonProperty("degree_types")
        private Collection<DegreeTypeDTO> degreeTypes;
    }

}
