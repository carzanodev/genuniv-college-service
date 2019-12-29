package carzanodev.genuniv.microservices.college.cache;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import carzanodev.genuniv.microservices.college.cache.model.FacultyDTO;
import carzanodev.genuniv.microservices.college.config.IntraServiceProperties;
import carzanodev.genuniv.microservices.common.cache.ApiCache;
import carzanodev.genuniv.microservices.common.model.dto.StandardResponse;

@Component
class FacultyApiCache extends ApiCache<Long, FacultyDTO, FacultyDTO.List> {

    private static final ParameterizedTypeReference<StandardResponse<FacultyDTO.List>> responseType = new ParameterizedTypeReference<>() {
    };

    @Autowired
    public FacultyApiCache(RestTemplate restTemplate, IntraServiceProperties properties) {
        super(
                restTemplate,
                properties.getPersonalRecords().getFacultyApiUrl(),
                properties.getPersonalRecords().getFacultyApiUrl() + "/info");
    }

    @Override
    protected ParameterizedTypeReference<StandardResponse<FacultyDTO.List>> getResponseType() {
        return responseType;
    }

    @Override
    protected Collection<FacultyDTO> getResultData(StandardResponse<FacultyDTO.List> response) {
        return response.getResponse().getFaculties();
    }

    @Override
    protected Long id(FacultyDTO facultyDTO) {
        return facultyDTO.getId();
    }

}
