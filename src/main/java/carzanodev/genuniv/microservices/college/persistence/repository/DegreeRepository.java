package carzanodev.genuniv.microservices.college.persistence.repository;

import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import carzanodev.genuniv.microservices.college.persistence.entity.Degree;
import carzanodev.genuniv.microservices.common.persistence.repository.InformationRepository;

@Repository
public interface DegreeRepository extends JpaRepository<Degree, Integer>, InformationRepository {

    @Query("SELECT d FROM Degree d WHERE d.isActive = TRUE")
    Set<Degree> findAllActive();

    @Query("SELECT d FROM Degree d WHERE d.id = :id AND d.isActive = TRUE")
    Optional<Degree> findActiveById(int id);

}
