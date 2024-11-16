package org.cresplanex.api.state.userpreferenceservice.event.model.userpreference;

import org.cresplanex.api.state.userpreferenceservice.event.model.BaseEvent;
import org.cresplanex.api.state.userpreferenceservice.event.model.SuccessfullyJobEvent;

public class SuccessfullyUpdateUserPreferenceEvent extends SuccessfullyJobEvent implements UserPreferenceDomainEvent, BaseEvent {
    public static final String TYPE = "org.cresplanex.nova.service.userpreference.event.UserPreference.UpdateSuccessfully";

    public SuccessfullyUpdateUserPreferenceEvent(
            String jobId,
            Object data
    ) {
        super(jobId, data);
    }

    public SuccessfullyUpdateUserPreferenceEvent() {
    }

    @Override
    public String getType() {
        return TYPE;
    }
}
