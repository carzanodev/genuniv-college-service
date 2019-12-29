package carzanodev.genuniv.microservices.college.cache;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import carzanodev.genuniv.microservices.college.cache.model.ScheduleDTO;
import carzanodev.genuniv.microservices.college.config.IntraServiceProperties;
import carzanodev.genuniv.microservices.common.cache.ApiCache;
import carzanodev.genuniv.microservices.common.model.dto.StandardResponse;

@Component
class ScheduleApiCache extends ApiCache<Integer, ScheduleDTO, ScheduleDTO.List> {

    @Autowired
    public ScheduleApiCache(RestTemplate restTemplate, IntraServiceProperties properties) {
        super(
                restTemplate,
                properties.getGeneralInfo().getScheduleApiUrl(),
                properties.getGeneralInfo().getScheduleApiUrl() + "/info");
    }

    @Override
    protected Collection<ScheduleDTO> getResultData(StandardResponse<ScheduleDTO.List> response) {
        return response.getResponse().getSchedules();
    }

    @Override
    protected Integer id(ScheduleDTO scheduleDTO) {
        return scheduleDTO.getId();
    }

}
