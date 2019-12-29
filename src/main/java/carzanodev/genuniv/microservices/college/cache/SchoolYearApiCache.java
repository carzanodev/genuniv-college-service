package carzanodev.genuniv.microservices.college.cache;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import carzanodev.genuniv.microservices.college.cache.model.SchoolYearDTO;
import carzanodev.genuniv.microservices.college.config.IntraServiceProperties;
import carzanodev.genuniv.microservices.common.cache.ApiCache;
import carzanodev.genuniv.microservices.common.model.dto.StandardResponse;

@Component
class SchoolYearApiCache extends ApiCache<Integer, SchoolYearDTO, SchoolYearDTO.List> {

    @Autowired
    public SchoolYearApiCache(RestTemplate restTemplate, IntraServiceProperties properties) {
        super(
                restTemplate,
                properties.getGeneralInfo().getSchoolYearApiUrl(),
                properties.getGeneralInfo().getSchoolYearApiUrl() + "/info");
    }

    @Override
    protected Collection<SchoolYearDTO> getResultData(StandardResponse<SchoolYearDTO.List> response) {
        return response.getResponse().getSchoolYears();
    }

    @Override
    protected Integer id(SchoolYearDTO schoolYearDTO) {
        return schoolYearDTO.getId();
    }

}
