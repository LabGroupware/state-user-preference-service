package org.cresplanex.api.state.userpreferenceservice.event.model.userpreference;

import org.cresplanex.api.state.userpreferenceservice.event.model.BaseEvent;
import org.cresplanex.api.state.userpreferenceservice.event.model.ProcessedJobEvent;

public class ProcessedJobEventUpdateUserPreference extends ProcessedJobEvent implements UserPreferenceDomainEvent, BaseEvent {
    public final static String TYPE = "org.cresplanex.nova.service.userpreference.event.UserPreference.UpdateProcessed";

    public ProcessedJobEventUpdateUserPreference(
            String jobId,
            Object data,
            String actionCode,
            String internalCode,
            String internalCaption,
            String timestamp
    ) {
        super(jobId, data, actionCode, internalCode, internalCaption, timestamp);
    }

    public ProcessedJobEventUpdateUserPreference() {
    }

    @Override
    public String getType() {
        return TYPE;
    }
}
