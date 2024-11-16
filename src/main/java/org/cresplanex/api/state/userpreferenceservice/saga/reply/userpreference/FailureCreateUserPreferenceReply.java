package org.cresplanex.api.state.userpreferenceservice.saga.reply.userpreference;

import org.cresplanex.api.state.userpreferenceservice.saga.reply.BaseFailureReply;

import java.util.Map;

public class FailureCreateUserPreferenceReply extends BaseFailureReply<Object> {
    public static final String TYPE = "org.cresplanex.nova.service.userprofile.saga.reply.userpreference.FailureCreateUserPreferenceReply";

    public FailureCreateUserPreferenceReply(Object data, String code, String caption, String timestamp) {
        super(data, code, caption, timestamp);
    }

    public FailureCreateUserPreferenceReply() {
    }

    @Override
    public String getType() {
        return TYPE;
    }
}
