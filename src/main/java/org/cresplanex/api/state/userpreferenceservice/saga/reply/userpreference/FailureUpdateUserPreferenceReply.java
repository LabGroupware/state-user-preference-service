package org.cresplanex.api.state.userpreferenceservice.saga.reply.userpreference;

import org.cresplanex.api.state.userpreferenceservice.saga.reply.BaseFailureReply;

public class FailureUpdateUserPreferenceReply extends BaseFailureReply<Object> {
    public static final String TYPE = "org.cresplanex.nova.service.userpreference.saga.reply.userpreference.FailureCreateUserPreferenceReply";

    public FailureUpdateUserPreferenceReply(Object data, String code, String caption, String timestamp) {
        super(data, code, caption, timestamp);
    }

    public FailureUpdateUserPreferenceReply() {
    }

    @Override
    public String getType() {
        return TYPE;
    }
}
