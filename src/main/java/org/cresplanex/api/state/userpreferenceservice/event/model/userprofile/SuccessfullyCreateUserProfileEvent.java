package org.cresplanex.api.state.userpreferenceservice.event.model.userprofile;

import org.cresplanex.api.state.userpreferenceservice.event.model.BaseEvent;
import org.cresplanex.api.state.userpreferenceservice.event.model.SuccessfullyJobEvent;

public class SuccessfullyCreateUserProfileEvent extends SuccessfullyJobEvent implements UserProfileDomainEvent, BaseEvent {
    public static final String TYPE = "org.cresplanex.nova.service.userprofile.event.UserProfile.CreateSuccessfully";

    public SuccessfullyCreateUserProfileEvent(
            String jobId,
            Object data
    ) {
        super(jobId, data);
    }

    public SuccessfullyCreateUserProfileEvent() {
    }

    @Override
    public String getType() {
        return TYPE;
    }
}
