package carzanodev.genuniv.microservices.college.persistence.repository;

import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import carzanodev.genuniv.microservices.college.persistence.entity.DegreeType;
import carzanodev.genuniv.microservices.common.persistence.repository.InformationRepository;

@Repository
public interface DegreeTypeRepository extends JpaRepository<DegreeType, Integer>, InformationRepository {

    @Query("SELECT d FROM DegreeType d WHERE d.isActive = TRUE")
    Set<DegreeType> findAllActive();

    @Query("SELECT d FROM DegreeType d WHERE d.id = :id AND d.isActive = TRUE")
    Optional<DegreeType> findActiveById(int id);

}
