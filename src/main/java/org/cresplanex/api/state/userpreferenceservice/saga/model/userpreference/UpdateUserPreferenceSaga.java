package org.cresplanex.api.state.userpreferenceservice.saga.model.userpreference;

import org.cresplanex.api.state.userpreferenceservice.entity.UserPreferenceEntity;
import org.cresplanex.api.state.userpreferenceservice.event.model.userpreference.*;
import org.cresplanex.api.state.userpreferenceservice.event.publisher.AggregateDomainEventPublisher;
import org.cresplanex.api.state.userpreferenceservice.event.publisher.UserPreferenceDomainEventPublisher;
import org.cresplanex.api.state.userpreferenceservice.saga.SagaCommandChannel;
import org.cresplanex.api.state.userpreferenceservice.saga.data.userpreference.UpdateUserPreferenceResultData;
import org.cresplanex.api.state.userpreferenceservice.saga.model.SagaModel;
import org.cresplanex.api.state.userpreferenceservice.saga.proxy.UserPreferenceServiceProxy;
import org.cresplanex.api.state.userpreferenceservice.saga.reply.userpreference.FailureUpdateUserPreferenceReply;
import org.cresplanex.api.state.userpreferenceservice.saga.reply.userpreference.UpdateUserPreferenceReply;
import org.cresplanex.api.state.userpreferenceservice.saga.state.userpreference.UpdateUserPreferenceSagaState;
import org.cresplanex.core.saga.orchestration.SagaDefinition;
import org.springframework.stereotype.Component;

@Component
public class UpdateUserPreferenceSaga extends SagaModel<
        UserPreferenceEntity, UserPreferenceDomainEvent, UpdateUserPreferenceSaga.Action, UpdateUserPreferenceSagaState> {
    public static final String TYPE = "org.cresplanex.nova.service.userprofile.saga.UpdateUserPreferenceSaga";

    private final SagaDefinition<UpdateUserPreferenceSagaState> sagaDefinition;
    private final UserPreferenceDomainEventPublisher domainEventPublisher;

    public UpdateUserPreferenceSaga(
            UserPreferenceServiceProxy userPreferenceService,
            UserPreferenceDomainEventPublisher domainEventPublisher
    ) {
        this.sagaDefinition = step()
                .invokeParticipant(
                        userPreferenceService.updateUserPreference,
                        UpdateUserPreferenceSagaState::makeUpdateUserPreferenceCommand
                )
                .onReply(
                        UpdateUserPreferenceReply.class,
                        UpdateUserPreferenceReply.TYPE,
                        this::handleUpdateUserPreferenceReply
                )
                .onReply(
                        FailureUpdateUserPreferenceReply.class,
                        FailureUpdateUserPreferenceReply.TYPE,
                        this::handleFailureReply
                )
                .withCompensation(
                        userPreferenceService.undoUpdateUserPreference,
                        UpdateUserPreferenceSagaState::makeUndoUpdateUserPreferenceCommand
                )
                .build();
        this.domainEventPublisher = domainEventPublisher;
    }

    @Override
    protected AggregateDomainEventPublisher<UserPreferenceEntity, UserPreferenceDomainEvent> getDomainEventPublisher() {
        return domainEventPublisher;
    }

    @Override
    protected Action[] getActions() {
        return Action.values();
    }

    @Override
    protected String getBeginEventType() {
        return BeginJobEventUpdateUserPreference.TYPE;
    }

    @Override
    protected String getProcessedEventType() {
        return ProcessedJobEventUpdateUserPreference.TYPE;
    }

    @Override
    protected String getFailedEventType() {
        return FailedJobEventUpdateUserPreference.TYPE;
    }

    @Override
    protected String getSuccessfullyEventType() {
        return SuccessfullyUpdateUserPreferenceEvent.TYPE;
    }

    private void handleUpdateUserPreferenceReply(UpdateUserPreferenceSagaState state, UpdateUserPreferenceReply reply) {
        UpdateUserPreferenceReply.Data data = reply.getData();
        state.setUserPreferenceDto(data.getUserPreference());
        state.setPrevUserPreferenceDto(data.getPrevUserPreference());
        processedEventPublish(state, reply);
    }

    @Override
    public void onSagaCompletedSuccessfully(String sagaId, UpdateUserPreferenceSagaState data) {
        UpdateUserPreferenceResultData resultData = new UpdateUserPreferenceResultData(data.getPrevUserPreferenceDto());
        successfullyEventPublish(data, resultData);
    }

    public enum Action {
        UPDATE_USER_PREFERENCE
    }

    @Override
    public SagaDefinition<UpdateUserPreferenceSagaState> getSagaDefinition() {
        return sagaDefinition;
    }

    @Override
    public String getSagaType() {
        return UpdateUserPreferenceSaga.TYPE;
    }

    @Override
    public String getSagaCommandSelfChannel() {
        return SagaCommandChannel.USER_PREFERENCE;
    }
}
