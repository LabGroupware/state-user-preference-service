package org.cresplanex.api.state.userpreferenceservice.saga.handler;

import lombok.RequiredArgsConstructor;
import org.cresplanex.api.state.common.constants.UserPreferenceServiceApplicationCode;
import org.cresplanex.api.state.common.entity.EntityWithPrevious;
import org.cresplanex.api.state.common.saga.LockTargetType;
import org.cresplanex.api.state.common.saga.SagaCommandChannel;
import org.cresplanex.api.state.common.saga.command.userpreference.UpdateUserPreferenceCommand;
import org.cresplanex.api.state.common.saga.reply.userpreference.UpdateUserPreferenceReply;
import org.cresplanex.api.state.userpreferenceservice.entity.UserPreferenceEntity;
import org.cresplanex.api.state.userpreferenceservice.mapper.dto.DtoMapper;
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

@Component
@RequiredArgsConstructor
public class UserPreferenceSagaCommandHandlers {

    private final UserPreferenceService userPreferenceService;

    public CommandHandlers commandHandlers() {
        return SagaCommandHandlersBuilder
                .fromChannel(SagaCommandChannel.USER_PREFERENCE)
                .onMessage(UpdateUserPreferenceCommand.Exec.class,
                        UpdateUserPreferenceCommand.Exec.TYPE,
                        this::handleUpdateUserPreferenceCommand
                )
                .withPreLock(this::updateUserPreferencePreLock)
                .onMessage(UpdateUserPreferenceCommand.Undo.class,
                        UpdateUserPreferenceCommand.Undo.TYPE,
                        this::handleUndoUpdateUserPreferenceCommand
                )
                .withPreLock(this::undoUpdateUserPreferencePreLock)
                .build();
    }

    private LockTarget updateUserPreferencePreLock(
            CommandMessage<UpdateUserPreferenceCommand.Exec> cmd,
            PathVariables pathVariables
    ) {
        return new LockTarget(LockTargetType.USER_PREFERENCE, cmd.getCommand().getUserPreferenceId());
    }

    private LockTarget undoUpdateUserPreferencePreLock(
            CommandMessage<UpdateUserPreferenceCommand.Undo> cmd,
            PathVariables pathVariables
    ) {
        return new LockTarget(LockTargetType.USER_PREFERENCE, cmd.getCommand().getUserPreferenceId());
    }

    private Message handleUpdateUserPreferenceCommand(CommandMessage<UpdateUserPreferenceCommand.Exec> cmd) {
        try {
            UpdateUserPreferenceCommand.Exec command = cmd.getCommand();
            UserPreferenceEntity userPreference = new UserPreferenceEntity();
            userPreference.setLanguage(command.getLanguage());
            userPreference.setTheme(command.getTheme());
            userPreference.setTimezone(command.getTimezone());
            EntityWithPrevious<UserPreferenceEntity> entityWithPrev =
                    userPreferenceService.update(command.getUserPreferenceId(), userPreference);
            UpdateUserPreferenceReply.Success reply = new UpdateUserPreferenceReply.Success(
                    new UpdateUserPreferenceReply.Success.Data(
                            DtoMapper.convert(entityWithPrev.getCurrent()),
                            DtoMapper.convert(entityWithPrev.getPrevious())
                    ),
                    UserPreferenceServiceApplicationCode.SUCCESS,
                    "User preference updated successfully",
                    LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
            );
            return withSuccess(reply, UpdateUserPreferenceReply.Success.TYPE);
        } catch (Exception e) {
            UpdateUserPreferenceReply.Failure reply = new UpdateUserPreferenceReply.Failure(
                    null,
                    UserPreferenceServiceApplicationCode.INTERNAL_SERVER_ERROR,
                    "Failed to create user preference",
                    LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
            );
            return withFailure(reply, UpdateUserPreferenceReply.Failure.TYPE);
        }
    }

    private Message handleUndoUpdateUserPreferenceCommand(CommandMessage<UpdateUserPreferenceCommand.Undo> cmd) {
        try {
            UpdateUserPreferenceCommand.Undo command = cmd.getCommand();
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
