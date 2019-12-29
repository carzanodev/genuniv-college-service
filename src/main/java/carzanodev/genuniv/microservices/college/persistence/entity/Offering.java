package carzanodev.genuniv.microservices.college.persistence.entity;

import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import lombok.Data;

@Entity
@Data
public class Offering {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToOne
    @JoinColumn(name = "course_id")
    private Course course;

    @Column(name = "schedule_id")
    private Integer scheduleId;

    @Column(name = "faculty_id")
    private Long facultyId;

    @OneToOne
    @JoinColumn(name = "classroom_id")
    private Classroom classroom;

    @Column(name = "capacity")
    private int capacity;

    @Column(name = "school_period_id")
    private Integer schoolPeriodId;

    @Column(name = "school_year_id")
    private Integer schoolYearId;

    @Column(name = "is_active")
    private boolean isActive = true;

    @Column(name = "inserted_at", insertable = false, updatable = false)
    private Timestamp insertedAt;

    @Column(name = "updated_at", insertable = false, updatable = false)
    private Timestamp updatedAt;

}
