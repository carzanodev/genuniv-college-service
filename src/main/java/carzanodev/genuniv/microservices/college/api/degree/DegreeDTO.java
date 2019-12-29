package carzanodev.genuniv.microservices.college.api.degree;

import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
class DegreeDTO {

    @JsonProperty("id")
    private int id;

    @JsonProperty("code")
    private String code;

    @JsonProperty("name")
    private String name;

    @JsonProperty("college_id")
    private int collegeId;

    @JsonProperty("degree_type_id")
    private int degreeTypeId;

    @Data
    @AllArgsConstructor
    static class List {
        @JsonProperty("degrees")
        private Collection<DegreeDTO> degrees;
    }

}
