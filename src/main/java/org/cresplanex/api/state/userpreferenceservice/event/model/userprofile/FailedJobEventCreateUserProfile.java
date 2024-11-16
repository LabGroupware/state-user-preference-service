package org.cresplanex.api.state.userpreferenceservice.event.model.userprofile;

import org.cresplanex.api.state.userpreferenceservice.event.model.BaseEvent;
import org.cresplanex.api.state.userpreferenceservice.event.model.FailedJobEvent;

public class FailedJobEventCreateUserProfile extends FailedJobEvent implements UserProfileDomainEvent, BaseEvent {
    public static final String TYPE = "org.cresplanex.nova.service.userprofile.event.UserProfile.CreateFailed";

    public FailedJobEventCreateUserProfile(
            String jobId,
            Object data,
            String actionCode,
            String internalCode,
            String internalCaption,
            String timestamp,
            Object endedErrorAttributes
    ) {
        super(jobId, data, actionCode, internalCode, internalCaption, timestamp, endedErrorAttributes);
    }

    public FailedJobEventCreateUserProfile() {
    }

    @Override
    public String getType() {
        return TYPE;
    }
}
