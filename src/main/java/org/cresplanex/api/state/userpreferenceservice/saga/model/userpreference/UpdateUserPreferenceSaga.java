package org.cresplanex.api.state.userpreferenceservice.saga.model.userpreference;

import org.cresplanex.api.state.common.constants.UserPreferenceServiceApplicationCode;
import org.cresplanex.api.state.common.event.model.userpreference.UserPreferenceDomainEvent;
import org.cresplanex.api.state.common.event.model.userpreference.UserPreferenceUpdated;
import org.cresplanex.api.state.common.event.publisher.AggregateDomainEventPublisher;
import org.cresplanex.api.state.common.saga.SagaCommandChannel;
import org.cresplanex.api.state.common.saga.data.userpreference.UpdateUserPreferenceResultData;
import org.cresplanex.api.state.common.saga.local.userpreference.NotFoundUserPreferenceException;
import org.cresplanex.api.state.common.saga.model.SagaModel;
import org.cresplanex.api.state.common.saga.reply.userpreference.UpdateUserPreferenceReply;
import org.cresplanex.api.state.common.saga.type.UserPreferenceSagaType;
import org.cresplanex.api.state.userpreferenceservice.entity.UserPreferenceEntity;
import org.cresplanex.api.state.userpreferenceservice.event.publisher.UserPreferenceDomainEventPublisher;
import org.cresplanex.api.state.userpreferenceservice.saga.proxy.UserPreferenceServiceProxy;
import org.cresplanex.api.state.userpreferenceservice.saga.state.userpreference.UpdateUserPreferenceSagaState;
import org.cresplanex.api.state.userpreferenceservice.service.UserPreferenceLocalValidateService;
import org.cresplanex.api.state.userpreferenceservice.service.UserPreferenceService;
import org.cresplanex.core.saga.orchestration.SagaDefinition;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UpdateUserPreferenceSaga extends SagaModel<
        UserPreferenceEntity,
        UserPreferenceDomainEvent,
        UpdateUserPreferenceSaga.Action,
        UpdateUserPreferenceSagaState> {

    private final SagaDefinition<UpdateUserPreferenceSagaState> sagaDefinition;
    private final UserPreferenceDomainEventPublisher domainEventPublisher;
    private final UserPreferenceLocalValidateService userPreferenceLocalService;

    public UpdateUserPreferenceSaga(
            UserPreferenceLocalValidateService userPreferenceLocalService,
            UserPreferenceServiceProxy userPreferenceService,
            UserPreferenceDomainEventPublisher domainEventPublisher
    ) {
        this.sagaDefinition = step()
                .invokeLocal(this::validateUserPreference)
                .onException(NotFoundUserPreferenceException.class, this::failureLocalExceptionPublish)
                .step()
                .invokeParticipant(
                        userPreferenceService.updateUserPreference,
                        UpdateUserPreferenceSagaState::makeUpdateUserPreferenceCommand
                )
                .onReply(
                        UpdateUserPreferenceReply.Success.class,
                        UpdateUserPreferenceReply.Success.TYPE,
                        this::handleUpdateUserPreferenceReply
                )
                .onReply(
                        UpdateUserPreferenceReply.Failure.class,
                        UpdateUserPreferenceReply.Failure.TYPE,
                        this::handleFailureReply
                )
                .withCompensation(
                        userPreferenceService.undoUpdateUserPreference,
                        UpdateUserPreferenceSagaState::makeUndoUpdateUserPreferenceCommand
                )
                .build();
        this.userPreferenceLocalService = userPreferenceLocalService;
        this.domainEventPublisher = domainEventPublisher;
    }

    @Override
    protected AggregateDomainEventPublisher<UserPreferenceEntity, UserPreferenceDomainEvent>
    getDomainEventPublisher() {
        return domainEventPublisher;
    }

    @Override
    protected Action[] getActions() {
        return Action.values();
    }

    @Override
    protected String getBeginEventType() {
        return UserPreferenceUpdated.BeginJobDomainEvent.TYPE;
    }

    @Override
    protected String getProcessedEventType() {
        return UserPreferenceUpdated.ProcessedJobDomainEvent.TYPE;
    }

    @Override
    protected String getFailedEventType() {
        return UserPreferenceUpdated.FailedJobDomainEvent.TYPE;
    }

    @Override
    protected String getSuccessfullyEventType() {
        return UserPreferenceUpdated.SuccessJobDomainEvent.TYPE;
    }

    private void validateUserPreference(UpdateUserPreferenceSagaState state) throws NotFoundUserPreferenceException {
        this.userPreferenceLocalService.validateUserPreferences(
                List.of(state.getInitialData().getUserPreferenceId())
        );

        this.localProcessedEventPublish(
                state, UserPreferenceServiceApplicationCode.SUCCESS, "User preference validated"
        );
    }

    private void handleUpdateUserPreferenceReply(
            UpdateUserPreferenceSagaState state, UpdateUserPreferenceReply.Success reply) {
        UpdateUserPreferenceReply.Success.Data data = reply.getData();
        state.setUserPreferenceDto(data.getUserPreference());
        state.setPrevUserPreferenceDto(data.getPrevUserPreference());
        this.processedEventPublish(state, reply);
    }

    @Override
    public void onSagaCompletedSuccessfully(String sagaId, UpdateUserPreferenceSagaState data) {
        UpdateUserPreferenceResultData resultData = new UpdateUserPreferenceResultData(data.getPrevUserPreferenceDto());
        successfullyEventPublish(data, resultData);
    }

    public enum Action {
        VALIDATE_USER_PREFERENCE,
        UPDATE_USER_PREFERENCE
    }

    @Override
    public SagaDefinition<UpdateUserPreferenceSagaState> getSagaDefinition() {
        return sagaDefinition;
    }

    @Override
    public String getSagaType() {
        return UserPreferenceSagaType.UPDATE_USER_PREFERENCE;
    }

    @Override
    public String getSagaCommandSelfChannel() {
        return SagaCommandChannel.USER_PREFERENCE;
    }
}
