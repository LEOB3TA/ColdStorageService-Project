package unibo.serviceaccessbe.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class TextMessageDTO {
    @JsonProperty("message")
    private String message;

    @JsonCreator
    public TextMessageDTO(String message) {
        this.message = message;
    }
}