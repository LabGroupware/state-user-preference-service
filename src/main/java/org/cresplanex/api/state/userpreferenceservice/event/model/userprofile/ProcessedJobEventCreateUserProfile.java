package org.cresplanex.api.state.userpreferenceservice.event.model.userprofile;

import org.cresplanex.api.state.userpreferenceservice.event.model.BaseEvent;
import org.cresplanex.api.state.userpreferenceservice.event.model.ProcessedJobEvent;

public class ProcessedJobEventCreateUserProfile extends ProcessedJobEvent implements UserProfileDomainEvent, BaseEvent {
    public final static String TYPE = "org.cresplanex.nova.service.userprofile.event.UserProfile.CreateProcessed";

    public ProcessedJobEventCreateUserProfile(
            String jobId,
            Object data,
            String actionCode,
            String internalCode,
            String internalCaption,
            String timestamp
    ) {
        super(jobId, data, actionCode, internalCode, internalCaption, timestamp);
    }

    public ProcessedJobEventCreateUserProfile() {
    }

    @Override
    public String getType() {
        return TYPE;
    }
}
