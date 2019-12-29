package carzanodev.genuniv.microservices.college.api.college;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import carzanodev.genuniv.microservices.college.persistence.entity.College;
import carzanodev.genuniv.microservices.college.persistence.repository.CollegeRepository;
import carzanodev.genuniv.microservices.common.api.service.InformationService;
import carzanodev.genuniv.microservices.common.config.CommonExceptionHandler.InvalidTargetEntityException;
import carzanodev.genuniv.microservices.common.config.CommonExceptionHandler.NonEmptyException;
import carzanodev.genuniv.microservices.common.model.dto.ResponseMeta;
import carzanodev.genuniv.microservices.common.model.dto.StandardResponse;
import carzanodev.genuniv.microservices.common.persistence.repository.InformationRepository;

import static carzanodev.genuniv.microservices.common.util.MetaMessage.CREATE_MSG;
import static carzanodev.genuniv.microservices.common.util.MetaMessage.DELETE_MSG;
import static carzanodev.genuniv.microservices.common.util.MetaMessage.LIST_MSG;
import static carzanodev.genuniv.microservices.common.util.MetaMessage.RETRIEVE_MSG;
import static carzanodev.genuniv.microservices.common.util.MetaMessage.UPDATE_MSG;

@Service
class CollegeService extends InformationService {

    private final CollegeRepository collegeRepo;

    @Autowired
    CollegeService(CollegeRepository collegeRepo) {
        this.collegeRepo = collegeRepo;
    }

    @Override
    protected InformationRepository getInfoRepo() {
        return collegeRepo;
    }

    StandardResponse<CollegeDTO.List> getAllColleges(boolean isActiveOnly) {
        List<CollegeDTO> collegeDtos = (isActiveOnly ? collegeRepo.findAllActive() : collegeRepo.findAll())
                .stream()
                .map(this::entityToDto)
                .collect(Collectors.toList());

        CollegeDTO.List response = new CollegeDTO.List(collegeDtos);
        ResponseMeta meta = ResponseMeta.createBasicMeta(LIST_MSG.make(collegeDtos.size()));

        return new StandardResponse<>(response, meta);
    }

    StandardResponse<CollegeDTO> getCollegeById(int id, boolean isActiveOnly) throws InvalidTargetEntityException {
        Optional<College> college = isActiveOnly ? collegeRepo.findActiveById(id) : collegeRepo.findById(id);

        if (college.isEmpty()) {
            throw new InvalidTargetEntityException("college", String.valueOf(id));
        }

        CollegeDTO response = entityToDto(college.get());
        ResponseMeta meta = ResponseMeta.createBasicMeta(RETRIEVE_MSG.make(id));

        return new StandardResponse<>(response, meta);
    }

    StandardResponse<CollegeDTO> addCollege(CollegeDTO collegeDto) throws InvalidTargetEntityException, NonEmptyException {
        College saved = collegeRepo.save(dtoToNewEntity(collegeDto));

        CollegeDTO response = entityToDto(saved);
        ResponseMeta meta = ResponseMeta.createBasicMeta(CREATE_MSG.make(saved.getId()));

        return new StandardResponse<>(response, meta);
    }

    StandardResponse<CollegeDTO> updateCollege(int id, CollegeDTO collegeDto, boolean isActiveOnly) throws InvalidTargetEntityException, NonEmptyException {
        College saved = collegeRepo.save(dtoToUpdatedEntity(id, collegeDto, isActiveOnly));

        CollegeDTO response = entityToDto(saved);
        ResponseMeta meta = ResponseMeta.createBasicMeta(UPDATE_MSG.make(saved.getId()));

        return new StandardResponse<>(response, meta);
    }

    StandardResponse<CollegeDTO> deleteCollege(int id) throws InvalidTargetEntityException {
        Optional<College> collegeFromDb = collegeRepo.findById(id);

        if (collegeFromDb.isEmpty()) {
            throw new InvalidTargetEntityException("college", String.valueOf(id));
        }

        College college = collegeFromDb.get();
        collegeRepo.delete(college);

        CollegeDTO response = entityToDto(college);
        ResponseMeta meta = ResponseMeta.createBasicMeta(DELETE_MSG.make(response.getId()));

        return new StandardResponse<>(response, meta);
    }

    private CollegeDTO entityToDto(College entity) {
        return new CollegeDTO(entity.getId(), entity.getName());
    }

    private College dtoToNewEntity(CollegeDTO collegeDto) throws InvalidTargetEntityException, NonEmptyException {
        return dtoToUpdatedEntity(0, collegeDto, false); // isActive here has no effect
    }

    private College dtoToUpdatedEntity(int id, CollegeDTO collegeDto, boolean isActiveOnly) throws InvalidTargetEntityException, NonEmptyException {
        Optional<College> c;
        if (id == 0) {
            c = Optional.of(new College());
        } else {
            c = isActiveOnly ? collegeRepo.findActiveById(id) : collegeRepo.findById(id);
        }

        if (c.isEmpty()) {
            throw new InvalidTargetEntityException("college", String.valueOf(id));
        }

        College college = c.get();

        String name = collegeDto.getName();
        if (!StringUtils.isEmpty(name)) {
            college.setName(name);
        } else {
            throw new NonEmptyException("name");
        }

        return college;
    }

}