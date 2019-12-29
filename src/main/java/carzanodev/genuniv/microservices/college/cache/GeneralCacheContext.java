package carzanodev.genuniv.microservices.college.cache;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import carzanodev.genuniv.microservices.college.cache.model.FacultyDTO;
import carzanodev.genuniv.microservices.college.cache.model.ScheduleDTO;
import carzanodev.genuniv.microservices.college.cache.model.SchoolPeriodDTO;
import carzanodev.genuniv.microservices.college.cache.model.SchoolYearDTO;
import carzanodev.genuniv.microservices.college.config.LoadingCacheProperties;
import carzanodev.genuniv.microservices.common.util.cache.CacheContext;
import carzanodev.genuniv.microservices.common.util.cache.DataCache;
import carzanodev.genuniv.microservices.common.util.cache.PeriodicCacheLoader;

@Component
public class GeneralCacheContext implements CacheContext {

    public static Map<Integer, SchoolYearDTO> SCHOOL_YEAR = new HashMap<>();
    public static Map<Integer, SchoolPeriodDTO> SCHOOL_PERIOD = new HashMap<>();
    public static Map<Integer, ScheduleDTO> SCHEDULE = new HashMap<>();
    public static Map<Integer, FacultyDTO> FACULTY = new HashMap<>();

    private SchoolYearApiCache schoolYearCache;
    private SchoolPeriodApiCache schoolPeriodCache;
    private ScheduleApiCache scheduleCache;
    private FacultyApiCache facultyCache;

    private PeriodicCacheLoader<SchoolYearApiCache> schoolYearLoader;
    private PeriodicCacheLoader<SchoolPeriodApiCache> schoolPeriodLoader;
    private PeriodicCacheLoader<ScheduleApiCache> scheduleLoader;
    private PeriodicCacheLoader<FacultyApiCache> facultyLoader;

    private int slowLoadInterval;
    private int fastLoadInterval;

    private static final int FORCE_LOAD_INTERVAL = 300;

    @Autowired
    public GeneralCacheContext(SchoolYearApiCache schoolYearCache, SchoolPeriodApiCache schoolPeriodCache, ScheduleApiCache scheduleCache,
                               FacultyApiCache facultyCache,
                               LoadingCacheProperties loadingCacheProperties) {
        this.schoolYearCache = schoolYearCache;
        this.schoolPeriodCache = schoolPeriodCache;
        this.scheduleCache = scheduleCache;
        this.facultyCache = facultyCache;

        this.slowLoadInterval = loadingCacheProperties.getSlowInterval();
        this.fastLoadInterval = loadingCacheProperties.getFastInterval();

    }

    @PostConstruct
    private void init() {
        this.schoolYearCache.registerContext(this);
        this.schoolPeriodCache.registerContext(this);
        this.scheduleCache.registerContext(this);
        this.facultyCache.registerContext(this);

        this.schoolYearLoader = new PeriodicCacheLoader<>(schoolYearCache, slowLoadInterval, FORCE_LOAD_INTERVAL);
        this.schoolPeriodLoader = new PeriodicCacheLoader<>(schoolPeriodCache, slowLoadInterval, FORCE_LOAD_INTERVAL);
        this.scheduleLoader = new PeriodicCacheLoader<>(scheduleCache, slowLoadInterval, FORCE_LOAD_INTERVAL);
        this.facultyLoader = new PeriodicCacheLoader<>(facultyCache, slowLoadInterval, FORCE_LOAD_INTERVAL);

        this.schoolYearLoader.start();
        this.schoolPeriodLoader.start();
        this.scheduleLoader.start();
        this.facultyLoader.start();
    }

    @Override
    @SuppressWarnings("unchecked")
    public void loadToContext(DataCache cacheInstance) {
        if (cacheInstance instanceof SchoolYearApiCache) {
            SCHOOL_YEAR = cacheInstance.getCache();
        } else if (cacheInstance instanceof SchoolPeriodApiCache) {
            SCHOOL_PERIOD = cacheInstance.getCache();
        } else if (cacheInstance instanceof ScheduleApiCache) {
            SCHEDULE = cacheInstance.getCache();
        } else if (cacheInstance instanceof FacultyApiCache) {
            FACULTY = cacheInstance.getCache();
        }
    }

    @PreDestroy
    private void endSafe() {
        this.schoolYearLoader.stop(true);
        this.schoolPeriodLoader.stop(true);
        this.scheduleLoader.stop(true);
        this.facultyLoader.stop(true);
    }

}
