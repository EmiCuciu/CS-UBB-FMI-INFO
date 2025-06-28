package network.dto;

public class RaspunsIntrebareDTO {
    private Integer id;
    private String textIntrebare;
    private String raspunsJucator;
    private String raspunsCorect;
    private int punctaj;
    private boolean corect;
    
    public RaspunsIntrebareDTO() {}
    
    public RaspunsIntrebareDTO(Integer id, String textIntrebare, String raspunsJucator, 
                              String raspunsCorect, int punctaj, boolean corect) {
        this.id = id;
        this.textIntrebare = textIntrebare;
        this.raspunsJucator = raspunsJucator;
        this.raspunsCorect = raspunsCorect;
        this.punctaj = punctaj;
        this.corect = corect;
    }
    
    // Getters and setters
    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    public String getTextIntrebare() {
        return textIntrebare;
    }
    
    public void setTextIntrebare(String textIntrebare) {
        this.textIntrebare = textIntrebare;
    }
    
    public String getRaspunsJucator() {
        return raspunsJucator;
    }
    
    public void setRaspunsJucator(String raspunsJucator) {
        this.raspunsJucator = raspunsJucator;
    }
    
    public String getRaspunsCorect() {
        return raspunsCorect;
    }
    
    public void setRaspunsCorect(String raspunsCorect) {
        this.raspunsCorect = raspunsCorect;
    }
    
    public int getPunctaj() {
        return punctaj;
    }
    
    public void setPunctaj(int punctaj) {
        this.punctaj = punctaj;
    }
    
    public boolean isCorect() {
        return corect;
    }
    
    public void setCorect(boolean corect) {
        this.corect = corect;
    }
}