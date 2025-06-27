package com.persistence;

import com.model.Configuration;

public interface IConfigurationRepository extends IRepository<Integer, Configuration> {
    Configuration getRandomConfiguration();
}
