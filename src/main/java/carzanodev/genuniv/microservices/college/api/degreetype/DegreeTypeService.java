package carzanodev.genuniv.microservices.college.api.degreetype;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import carzanodev.genuniv.microservices.college.persistence.entity.DegreeType;
import carzanodev.genuniv.microservices.college.persistence.repository.DegreeTypeRepository;
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
class DegreeTypeService extends InformationService {

    private final DegreeTypeRepository degreeTypeRepo;

    @Autowired
    DegreeTypeService(DegreeTypeRepository degreeTypeRepo) {
        this.degreeTypeRepo = degreeTypeRepo;
    }

    @Override
    protected InformationRepository getInfoRepo() {
        return degreeTypeRepo;
    }

    StandardResponse<DegreeTypeDTO.List> getAllDegreeTypes(boolean isActiveOnly) {
        List<DegreeTypeDTO> degreeTypeDtos = (isActiveOnly ? degreeTypeRepo.findAllActive() : degreeTypeRepo.findAll())
                .stream()
                .map(this::entityToDto)
                .collect(Collectors.toList());

        DegreeTypeDTO.List response = new DegreeTypeDTO.List(degreeTypeDtos);
        ResponseMeta meta = ResponseMeta.createBasicMeta(LIST_MSG.make(degreeTypeDtos.size()));

        return new StandardResponse<>(response, meta);
    }

    StandardResponse<DegreeTypeDTO> getDegreeTypeById(int id, boolean isActiveOnly) throws InvalidTargetEntityException {
        Optional<DegreeType> degreeType = isActiveOnly ? degreeTypeRepo.findActiveById(id) : degreeTypeRepo.findById(id);

        if (degreeType.isEmpty()) {
            throw new InvalidTargetEntityException("degree_type", String.valueOf(id));
        }

        DegreeTypeDTO response = entityToDto(degreeType.get());
        ResponseMeta meta = ResponseMeta.createBasicMeta(RETRIEVE_MSG.make(id));

        return new StandardResponse<>(response, meta);
    }

    StandardResponse<DegreeTypeDTO> addDegreeType(DegreeTypeDTO degreeTypeDto) throws InvalidTargetEntityException, NonEmptyException {
        DegreeType saved = degreeTypeRepo.save(dtoToNewEntity(degreeTypeDto));

        DegreeTypeDTO response = entityToDto(saved);
        ResponseMeta meta = ResponseMeta.createBasicMeta(CREATE_MSG.make(saved.getId()));

        return new StandardResponse<>(response, meta);
    }

    StandardResponse<DegreeTypeDTO> updateDegreeType(int id, DegreeTypeDTO degreeTypeDto, boolean isActiveOnly) throws InvalidTargetEntityException, NonEmptyException {
        DegreeType saved = degreeTypeRepo.save(dtoToUpdatedEntity(id, degreeTypeDto, isActiveOnly));

        DegreeTypeDTO response = entityToDto(saved);
        ResponseMeta meta = ResponseMeta.createBasicMeta(UPDATE_MSG.make(saved.getId()));

        return new StandardResponse<>(response, meta);
    }

    StandardResponse<DegreeTypeDTO> deleteDegreeType(int id) throws InvalidTargetEntityException {
        Optional<DegreeType> degreeTypeFromDb = degreeTypeRepo.findById(id);

        if (degreeTypeFromDb.isEmpty()) {
            throw new InvalidTargetEntityException("degree_type", String.valueOf(id));
        }

        DegreeType degreeType = degreeTypeFromDb.get();
        degreeTypeRepo.delete(degreeType);

        DegreeTypeDTO response = entityToDto(degreeType);
        ResponseMeta meta = ResponseMeta.createBasicMeta(DELETE_MSG.make(response.getId()));

        return new StandardResponse<>(response, meta);
    }

    private DegreeTypeDTO entityToDto(DegreeType entity) {
        return new DegreeTypeDTO(entity.getId(), entity.getName());
    }

    private DegreeType dtoToNewEntity(DegreeTypeDTO degreeTypeDto) throws InvalidTargetEntityException, NonEmptyException {
        return dtoToUpdatedEntity(0, degreeTypeDto, false);
    }

    private DegreeType dtoToUpdatedEntity(int id, DegreeTypeDTO degreeTypeDto, boolean isActiveOnly) throws InvalidTargetEntityException, NonEmptyException {
        Optional<DegreeType> dt;
        if (id == 0) {
            dt = Optional.of(new DegreeType());
        } else {
            dt = isActiveOnly ? degreeTypeRepo.findActiveById(id) : degreeTypeRepo.findById(id);
        }

        if (dt.isEmpty()) {
            throw new InvalidTargetEntityException("degree_type", String.valueOf(id));
        }

        DegreeType degreeType = dt.get();

        String name = degreeTypeDto.getName();
        if (!StringUtils.isEmpty(name)) {
            degreeType.setName(name);
        } else {
            throw new NonEmptyException("name");
        }

        return degreeType;
    }

}