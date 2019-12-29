package carzanodev.genuniv.microservices.college.persistence.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import carzanodev.genuniv.microservices.college.persistence.entity.Offering;
import carzanodev.genuniv.microservices.common.persistence.repository.InformationRepository;

@Repository
public interface OfferingRepository extends JpaRepository<Offering, Long>, InformationRepository {

    @Query("SELECT o FROM Offering o WHERE o.isActive = TRUE")
    List<Offering> findAllActive();

    @Query("SELECT o FROM Offering o WHERE o.id = :id AND o.isActive = TRUE")
    Optional<Offering> findActiveById(long id);

}
