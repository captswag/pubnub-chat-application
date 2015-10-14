package pojo;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Anjith Sasindran
 * on 11-Oct-15.
 */
public class Message {

    @SerializedName("username")
    String username;
    @SerializedName("message")
    String message;

    public Message(String username, String message) {
        this.username = username;
        this.message = message;
    }

    public String getUsername() {
        return username;
    }

    public String getMessage() {
        return message;
    }
}
