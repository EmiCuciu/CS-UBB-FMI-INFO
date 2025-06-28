package com.network.dto;

import java.util.List;

public class ConfigurationUpdateDTO {
    private List<String> newConfiguration;

    public ConfigurationUpdateDTO() {}

    public ConfigurationUpdateDTO(List<String> newConfiguration) {
        this.newConfiguration = newConfiguration;
    }

    public List<String> getNewConfiguration() { return newConfiguration; }
    public void setNewConfiguration(List<String> newConfiguration) { this.newConfiguration = newConfiguration; }
}