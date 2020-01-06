package carzanodev.genuniv.microservices.college.api.degree;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import carzanodev.genuniv.microservices.college.persistence.entity.College;
import carzanodev.genuniv.microservices.college.persistence.entity.Degree;
import carzanodev.genuniv.microservices.college.persistence.entity.DegreeType;
import carzanodev.genuniv.microservices.college.persistence.repository.CollegeRepository;
import carzanodev.genuniv.microservices.college.persistence.repository.DegreeRepository;
import carzanodev.genuniv.microservices.college.persistence.repository.DegreeTypeRepository;
import carzanodev.genuniv.microservices.common.api.service.InformationService;
import carzanodev.genuniv.microservices.common.config.CommonExceptionHandler.InvalidReferenceValueException;
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
class DegreeService extends InformationService {

    private final DegreeRepository degreeRepo;
    private final CollegeRepository collegeRepo;
    private final DegreeTypeRepository degreeTypeRepo;

    @Autowired
    DegreeService(DegreeRepository degreeRepo, CollegeRepository collegeRepo, DegreeTypeRepository degreeTypeRepo) {
        this.degreeRepo = degreeRepo;
        this.collegeRepo = collegeRepo;
        this.degreeTypeRepo = degreeTypeRepo;
    }

    @Override
    protected InformationRepository getInfoRepo() {
        return degreeRepo;
    }

    StandardResponse<DegreeDTO.List> getAllDegrees(boolean isActiveOnly) {
        List<DegreeDTO> degreeDtos = (isActiveOnly ? degreeRepo.findAllActive() : degreeRepo.findAll())
                .stream()
                .map(this::entityToDto)
                .collect(Collectors.toList());

        DegreeDTO.List response = new DegreeDTO.List(degreeDtos);
        ResponseMeta meta = ResponseMeta.createBasicMeta(LIST_MSG.make(degreeDtos.size()));

        return new StandardResponse<>(response, meta);
    }

    StandardResponse<DegreeDTO> getDegreeById(int id, boolean isActiveOnly) throws InvalidTargetEntityException {
        Optional<Degree> degree = isActiveOnly ? degreeRepo.findActiveById(id) : degreeRepo.findById(id);

        if (degree.isEmpty()) {
            throw new InvalidTargetEntityException("degree", String.valueOf(id));
        }

        DegreeDTO response = entityToDto(degree.get());
        ResponseMeta meta = ResponseMeta.createBasicMeta(RETRIEVE_MSG.make(id));

        return new StandardResponse<>(response, meta);
    }

    StandardResponse<DegreeDTO> addDegree(DegreeDTO degreeDto) throws InvalidTargetEntityException, InvalidReferenceValueException, NonEmptyException {
        Degree saved = degreeRepo.save(dtoToNewEntity(degreeDto));

        DegreeDTO response = entityToDto(saved);
        ResponseMeta meta = ResponseMeta.createBasicMeta(CREATE_MSG.make(saved.getId()));

        return new StandardResponse<>(response, meta);
    }

    StandardResponse<DegreeDTO> updateDegree(int id, DegreeDTO degreeDto, boolean isActiveOnly) throws InvalidTargetEntityException, InvalidReferenceValueException, NonEmptyException {
        Degree saved = degreeRepo.save(dtoToUpdatedEntity(id, degreeDto, isActiveOnly));

        DegreeDTO response = entityToDto(saved);
        ResponseMeta meta = ResponseMeta.createBasicMeta(UPDATE_MSG.make(saved.getId()));

        return new StandardResponse<>(response, meta);
    }

    StandardResponse<DegreeDTO> deleteDegree(int id) throws InvalidTargetEntityException {
        Optional<Degree> degreeFromDb = degreeRepo.findById(id);

        if (degreeFromDb.isEmpty()) {
            throw new InvalidTargetEntityException("degree", String.valueOf(id));
        }

        Degree degree = degreeFromDb.get();
        degreeRepo.delete(degree);

        DegreeDTO response = entityToDto(degree);
        ResponseMeta meta = ResponseMeta.createBasicMeta(DELETE_MSG.make(response.getId()));

        return new StandardResponse<>(response, meta);
    }

    private DegreeDTO entityToDto(Degree entity) {
        return new DegreeDTO(
                entity.getId(),
                entity.getCode(),
                entity.getName(),
                entity.getCollege().getId(),
                entity.getDegreeType().getId());
    }

    private Degree dtoToNewEntity(DegreeDTO degreeDto) throws InvalidTargetEntityException, InvalidReferenceValueException, NonEmptyException {
        return dtoToUpdatedEntity(0, degreeDto, false);
    }

    private Degree dtoToUpdatedEntity(int id, DegreeDTO degreeDto, boolean isActiveOnly) throws InvalidTargetEntityException, InvalidReferenceValueException, NonEmptyException {
        Optional<Degree> d;
        if (id == 0) {
            d = Optional.of(new Degree());
        } else {
            d = isActiveOnly ? degreeRepo.findActiveById(id) : degreeRepo.findById(id);
        }

        if (d.isEmpty()) {
            throw new InvalidTargetEntityException("degree", String.valueOf(id));
        }

        Degree degree = d.get();

        String code = degreeDto.getCode();
        if (!StringUtils.isEmpty(code)) {
            degree.setCode(code);
        } else {
            throw new NonEmptyException("code");
        }

        String name = degreeDto.getName();
        if (!StringUtils.isEmpty(name)) {
            degree.setName(name);
        } else {
            throw new NonEmptyException("name");
        }

        int collegeId = degreeDto.getCollegeId();
        if (collegeId > 0) {
            Optional<College> college = collegeRepo.findById(collegeId);

            if (college.isEmpty()) {
                throw new InvalidReferenceValueException("college_id", String.valueOf(collegeId));
            }

            degree.setCollege(college.get());
        } else {
            throw new NonEmptyException("college_id");
        }

        int degreeTypeId = degreeDto.getDegreeTypeId();
        if (degreeTypeId > 0) {
            Optional<DegreeType> degreeType = degreeTypeRepo.findById(degreeTypeId);

            if (degreeType.isEmpty()) {
                throw new InvalidReferenceValueException("degree_type_id", String.valueOf(degreeTypeId));
            }

            degree.setDegreeType(degreeType.get());
        } else {
            throw new NonEmptyException("degree_type_id");
        }

        return degree;
    }

}