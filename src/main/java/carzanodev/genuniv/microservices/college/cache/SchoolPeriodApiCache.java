package carzanodev.genuniv.microservices.college.cache;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import carzanodev.genuniv.microservices.college.cache.model.SchoolPeriodDTO;
import carzanodev.genuniv.microservices.college.config.IntraServiceProperties;
import carzanodev.genuniv.microservices.common.cache.ApiCache;
import carzanodev.genuniv.microservices.common.model.dto.StandardResponse;

@Component
class SchoolPeriodApiCache extends ApiCache<Integer, SchoolPeriodDTO, SchoolPeriodDTO.List> {

    @Autowired
    public SchoolPeriodApiCache(RestTemplate restTemplate, IntraServiceProperties properties) {
        super(
                restTemplate,
                properties.getGeneralInfo().getSchoolPeriodApiUrl(),
                properties.getGeneralInfo().getSchoolPeriodApiUrl() + "/info");
    }

    @Override
    protected Collection<SchoolPeriodDTO> getResultData(StandardResponse<SchoolPeriodDTO.List> response) {
        return response.getResponse().getSchoolPeriods();
    }

    @Override
    protected Integer id(SchoolPeriodDTO schoolPeriodDTO) {
        return schoolPeriodDTO.getId();
    }

}
