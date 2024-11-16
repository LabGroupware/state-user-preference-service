package org.cresplanex.api.state.userpreferenceservice.saga.handler;

import lombok.RequiredArgsConstructor;
import org.cresplanex.api.state.userpreferenceservice.constants.ServerErrorCode;
import org.cresplanex.api.state.userpreferenceservice.entity.EntityWithPrevious;
import org.cresplanex.api.state.userpreferenceservice.entity.UserPreferenceEntity;
import org.cresplanex.api.state.userpreferenceservice.mapper.dto.DtoMapper;
import org.cresplanex.api.state.userpreferenceservice.saga.LockTargetType;
import org.cresplanex.api.state.userpreferenceservice.saga.SagaCommandChannel;
import org.cresplanex.api.state.userpreferenceservice.saga.command.userpreference.UndoUpdateUserPreferenceCommand;
import org.cresplanex.api.state.userpreferenceservice.saga.command.userpreference.UpdateUserPreferenceCommand;
import org.cresplanex.api.state.userpreferenceservice.saga.reply.userpreference.FailureUpdateUserPreferenceReply;
import org.cresplanex.api.state.userpreferenceservice.saga.reply.userpreference.UpdateUserPreferenceReply;
import org.cresplanex.api.state.userpreferenceservice.service.UserPreferenceService;
import org.cresplanex.core.commands.consumer.CommandHandlers;
import org.cresplanex.core.commands.consumer.CommandMessage;
import org.cresplanex.core.commands.consumer.PathVariables;
import org.cresplanex.core.messaging.common.Message;
import org.cresplanex.core.saga.lock.LockTarget;
import org.cresplanex.core.saga.participant.SagaCommandHandlersBuilder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.cresplanex.core.commands.consumer.CommandHandlerReplyBuilder.withFailure;
import static org.cresplanex.core.commands.consumer.CommandHandlerReplyBuilder.withSuccess;
import static org.cresplanex.core.saga.participant.SagaReplyMessageBuilder.withLock;

@Component
@RequiredArgsConstructor
public class UserPreferenceSagaCommandHandlers {

    private final UserPreferenceService userPreferenceService;

    public CommandHandlers commandHandlers() {
        return SagaCommandHandlersBuilder
                .fromChannel(SagaCommandChannel.USER_PREFERENCE)
                .onMessage(UpdateUserPreferenceCommand.class,
                        UpdateUserPreferenceCommand.TYPE,
                        this::handleUpdateUserPreferenceCommand
                )
                .withPreLock(this::updateUserPreferencePreLock)
                .onMessage(UndoUpdateUserPreferenceCommand.class,
                        UndoUpdateUserPreferenceCommand.TYPE,
                        this::handleUndoUpdateUserPreferenceCommand
                )
                .withPreLock(this::undoUpdateUserPreferencePreLock)
                .build();
    }

    private LockTarget updateUserPreferencePreLock(
            CommandMessage<UpdateUserPreferenceCommand> cmd,
            PathVariables pathVariables
    ) {
        return new LockTarget(LockTargetType.USER_PREFERENCE, cmd.getCommand().getUserPreferenceId());
    }

    private LockTarget undoUpdateUserPreferencePreLock(
            CommandMessage<UndoUpdateUserPreferenceCommand> cmd,
            PathVariables pathVariables
    ) {
        return new LockTarget(LockTargetType.USER_PREFERENCE, cmd.getCommand().getUserPreferenceId());
    }

    private Message handleUpdateUserPreferenceCommand(CommandMessage<UpdateUserPreferenceCommand> cmd) {
        try {
            UpdateUserPreferenceCommand command = cmd.getCommand();
            UserPreferenceEntity userPreference = new UserPreferenceEntity();
            userPreference.setLanguage(command.getLanguage());
            userPreference.setTheme(command.getTheme());
            userPreference.setTimezone(command.getTimezone());
            EntityWithPrevious<UserPreferenceEntity> entityWithPrev =
                    userPreferenceService.update(command.getUserPreferenceId(), userPreference);
            UpdateUserPreferenceReply reply = new UpdateUserPreferenceReply(
                    new UpdateUserPreferenceReply.Data(
                            DtoMapper.convert(entityWithPrev.getCurrent()),
                            DtoMapper.convert(entityWithPrev.getPrevious())
                    ),
                    ServerErrorCode.USER_PREFERENCE_SUCCESS,
                    "User preference updated successfully",
                    LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
            );
            return withSuccess(reply, UpdateUserPreferenceReply.TYPE);
        } catch (Exception e) {
            FailureUpdateUserPreferenceReply reply = new FailureUpdateUserPreferenceReply(
                    null,
                    ServerErrorCode.USER_PREFERENCE_INTERNAL_ERROR,
                    "Failed to create user preference",
                    LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
            );
            return withFailure(reply, FailureUpdateUserPreferenceReply.TYPE);
        }
    }

    private Message handleUndoUpdateUserPreferenceCommand(CommandMessage<UndoUpdateUserPreferenceCommand> cmd) {
        try {
            UndoUpdateUserPreferenceCommand command = cmd.getCommand();
            String userPreferenceId = command.getUserPreferenceId();
            UserPreferenceEntity userPreference = new UserPreferenceEntity();
            userPreference.setLanguage(command.getOriginLanguage());
            userPreference.setTheme(command.getOriginTheme());
            userPreference.setTimezone(command.getOriginTimezone());
            userPreferenceService.undoUpdate(userPreferenceId, userPreference);
            return withSuccess();
        } catch (Exception e) {
            return withFailure();
        }
    }
}
