package network.dto;

import java.util.List;

public class ConfigurationDTO {
    private Integer id;
    private List<String> words;

    // Constructors, getters and setters
    public ConfigurationDTO() {}

    public ConfigurationDTO(Integer id, List<String> words) {
        this.id = id;
        this.words = words;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<String> getWords() {
        return words;
    }

    public void setWords(List<String> words) {
        this.words = words;
    }
}