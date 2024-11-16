package org.cresplanex.api.state.userpreferenceservice.event.model.userpreference;

import org.cresplanex.api.state.userpreferenceservice.event.model.BaseEvent;
import org.cresplanex.api.state.userpreferenceservice.event.model.FailedJobEvent;

public class FailedJobEventUpdateUserPreference extends FailedJobEvent implements UserPreferenceDomainEvent, BaseEvent {
    public static final String TYPE = "org.cresplanex.nova.service.userpreference.event.UserPreference.UpdateFailed";

    public FailedJobEventUpdateUserPreference(
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

    public FailedJobEventUpdateUserPreference() {
    }

    @Override
    public String getType() {
        return TYPE;
    }
}
