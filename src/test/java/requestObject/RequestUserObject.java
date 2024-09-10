package requestObject;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString

public class RequestUserObject extends GeneralObject{
    public RequestUserObject(String filePath) {
        super(filePath);
        password += "***<>,alabama";
        userName += System.currentTimeMillis();
    }

    @JsonProperty("userName")
    private String userName;
    @JsonProperty("password")
    private String password;
}
