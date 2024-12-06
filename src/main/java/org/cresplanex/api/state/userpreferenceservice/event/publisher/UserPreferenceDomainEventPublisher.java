package org.cresplanex.api.state.userpreferenceservice.event.publisher;

import org.cresplanex.api.state.common.event.EventAggregateType;
import org.cresplanex.api.state.common.event.model.userpreference.UserPreferenceDomainEvent;
import org.cresplanex.api.state.common.event.publisher.AggregateDomainEventPublisher;
import org.cresplanex.api.state.userpreferenceservice.entity.UserPreferenceEntity;
import org.cresplanex.core.events.publisher.DomainEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class UserPreferenceDomainEventPublisher extends AggregateDomainEventPublisher<UserPreferenceEntity, UserPreferenceDomainEvent> {

    public UserPreferenceDomainEventPublisher(DomainEventPublisher eventPublisher) {
        super(eventPublisher, UserPreferenceEntity.class, EventAggregateType.USER_PREFERENCE);
    }
}
