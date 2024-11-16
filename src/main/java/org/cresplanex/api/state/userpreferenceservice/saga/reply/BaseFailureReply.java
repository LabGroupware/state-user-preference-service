package org.cresplanex.api.state.userpreferenceservice.saga.reply;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class BaseFailureReply<Data> {
    private Data data;
    private String code;
    private String caption;
    private String timestamp;

    public BaseFailureReply(Data data, String code, String caption, String timestamp) {
        this.data = data;
        this.code = code;
        this.caption = caption;
        this.timestamp = timestamp;
    }

    public BaseFailureReply() {
    }

    abstract public String getType();
}
