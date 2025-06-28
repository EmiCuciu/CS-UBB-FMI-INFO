// Network/src/main/java/network/dto/IntrebareUpdateDTO.java
package network.dto;

public class IntrebareUpdateDTO {
    private String text;
    
    public IntrebareUpdateDTO() {}
    
    public IntrebareUpdateDTO(String text) {
        this.text = text;
    }
    
    public String getText() {
        return text;
    }
    
    public void setText(String text) {
        this.text = text;
    }
}