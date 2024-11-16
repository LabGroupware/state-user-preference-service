package org.cresplanex.api.state.userpreferenceservice.saga.reply.userpreference;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.cresplanex.api.state.userpreferenceservice.dto.userpreference.UserPreferenceDto;
import org.cresplanex.api.state.userpreferenceservice.saga.reply.BaseSuccessfullyReply;

public class UpdateUserPreferenceReply extends BaseSuccessfullyReply<UpdateUserPreferenceReply.Data> {
    public static final String TYPE = "org.cresplanex.nova.service.userpreference.saga.reply.userpreference.UpdateUserPreferenceReply";

    public UpdateUserPreferenceReply(Data data, String code, String caption, String timestamp) {
        super(data, code, caption, timestamp);
    }

    public UpdateUserPreferenceReply() {
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class Data {
        private UserPreferenceDto userPreference;
        private UserPreferenceDto prevUserPreference;
    }

    @Override
    public String getType() {
        return TYPE;
    }
}
