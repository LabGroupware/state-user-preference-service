package org.cresplanex.api.state.userpreferenceservice.event.model.userprofile;

import org.cresplanex.api.state.userpreferenceservice.event.model.BaseEvent;
import org.cresplanex.api.state.userpreferenceservice.event.model.BeginJobEvent;

import java.util.List;

public class BeginJobEventCreateUserProfile extends BeginJobEvent implements UserProfileDomainEvent, BaseEvent {
    public static final String TYPE = "org.cresplanex.nova.service.userprofile.event.UserProfile.CreateBegin";

    public BeginJobEventCreateUserProfile(String jobId, List<String> toActionCodes, String pendingActionCode, String timestamp) {
        super(jobId, toActionCodes, pendingActionCode, timestamp);
    }

    public BeginJobEventCreateUserProfile() {
    }

    @Override
    public String getType() {
        return TYPE;
    }
}
