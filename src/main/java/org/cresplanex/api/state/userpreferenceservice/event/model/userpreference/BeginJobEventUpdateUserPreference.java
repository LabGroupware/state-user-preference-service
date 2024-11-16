package org.cresplanex.api.state.userpreferenceservice.event.model.userpreference;

import org.cresplanex.api.state.userpreferenceservice.event.model.BaseEvent;
import org.cresplanex.api.state.userpreferenceservice.event.model.BeginJobEvent;

import java.util.List;

public class BeginJobEventUpdateUserPreference extends BeginJobEvent implements UserPreferenceDomainEvent, BaseEvent {
    public static final String TYPE = "org.cresplanex.nova.service.userpreference.event.UserPreference.UpdateBegin";

    public BeginJobEventUpdateUserPreference(String jobId, List<String> toActionCodes, String pendingActionCode, String timestamp) {
        super(jobId, toActionCodes, pendingActionCode, timestamp);
    }

    public BeginJobEventUpdateUserPreference() {
    }

    @Override
    public String getType() {
        return TYPE;
    }
}
