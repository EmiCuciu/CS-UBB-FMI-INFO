package com.example.laboratoriss.Repository.Hibernate;

import com.example.laboratoriss.Domain.Medicament;
import com.example.laboratoriss.Repository.IMedicamentRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MedicamentRepository extends AbstractHibernateRepository<Integer, Medicament>
        implements IMedicamentRepository {

    private static final Logger logger = LogManager.getLogger();

    public MedicamentRepository() {
        logger.info("Initializing MedicamentRepository with Hibernate");
    }


}