package responseObject;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ResponseGetUserFailed {
    @JsonProperty("code")
    private String code;
    @JsonProperty("message")
    private String message;

}


