package org.cresplanex.api.state.userpreferenceservice.saga.reply;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class BaseSuccessfullyReply<Data> {
    private Data data;
    private String code;
    private String caption;
    private String timestamp;

    public BaseSuccessfullyReply(Data data, String code, String caption, String timestamp) {
        this.data = data;
        this.code = code;
        this.caption = caption;
        this.timestamp = timestamp;
    }

    public BaseSuccessfullyReply() {
    }

    abstract public String getType();
}
