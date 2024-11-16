package org.cresplanex.api.state.userpreferenceservice.saga.reply.userpreference;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.cresplanex.api.state.userpreferenceservice.dto.userpreference.UserPreferenceDto;
import org.cresplanex.api.state.userpreferenceservice.saga.reply.BaseSuccessfullyReply;

public class CreateUserPreferenceReply extends BaseSuccessfullyReply<CreateUserPreferenceReply.Data> {
    public static final String TYPE = "org.cresplanex.nova.service.userprofile.saga.reply.userpreference.CreateUserPreferenceReply";

    public CreateUserPreferenceReply(Data data, String code, String caption, String timestamp) {
        super(data, code, caption, timestamp);
    }

    public CreateUserPreferenceReply() {
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class Data {
        private UserPreferenceDto userPreference;
    }

    @Override
    public String getType() {
        return TYPE;
    }
}
