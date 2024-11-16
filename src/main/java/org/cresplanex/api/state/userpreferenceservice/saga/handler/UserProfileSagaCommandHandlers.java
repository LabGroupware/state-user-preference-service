package org.cresplanex.api.state.userpreferenceservice.saga.handler;

import lombok.RequiredArgsConstructor;
import org.cresplanex.api.state.common.constants.UserPreferenceServiceApplicationCode;
import org.cresplanex.api.state.common.saga.LockTargetType;
import org.cresplanex.api.state.common.saga.SagaCommandChannel;
import org.cresplanex.api.state.common.saga.command.userpreference.CreateUserPreferenceCommand;
import org.cresplanex.api.state.common.saga.reply.userpreference.CreateUserPreferenceReply;
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
import static org.cresplanex.core.saga.participant.SagaReplyMessageBuilder.withLock;

@Component
@RequiredArgsConstructor
public class UserProfileSagaCommandHandlers {

    private final UserPreferenceService userPreferenceService;

    public CommandHandlers commandHandlers() {
        return SagaCommandHandlersBuilder
                .fromChannel(SagaCommandChannel.USER_PROFILE)
                .onMessage(CreateUserPreferenceCommand.Exec.class,
                        CreateUserPreferenceCommand.Exec.TYPE,
                        this::handleCreateUserPreferenceCommand
                )
                .onMessage(CreateUserPreferenceCommand.Undo.class,
                        CreateUserPreferenceCommand.Undo.TYPE,
                        this::handleUndoCreateUserPreferenceCommand
                )
                .withPreLock(this::undoCreateUserPreferencePreLock)
                .build();
    }

    private LockTarget undoCreateUserPreferencePreLock(
            CommandMessage<CreateUserPreferenceCommand.Undo> cmd, PathVariables pvs) {
        return new LockTarget(LockTargetType.USER_PREFERENCE, cmd.getCommand().getUserPreferenceId());
    }

    private Message handleCreateUserPreferenceCommand(CommandMessage<CreateUserPreferenceCommand.Exec> cmd) {
        try {
            CreateUserPreferenceCommand.Exec command = cmd.getCommand();
            UserPreferenceEntity userPreference = new UserPreferenceEntity();
            userPreference.setUserId(command.getUserId());
            userPreference = userPreferenceService.create(userPreference);
            CreateUserPreferenceReply.Success reply = new CreateUserPreferenceReply.Success(
                    new CreateUserPreferenceReply.Success.Data(DtoMapper.convert(userPreference)),
                    UserPreferenceServiceApplicationCode.SUCCESS,
                    "User preference created successfully",
                    LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
            );
            return withLock(LockTargetType.USER_PREFERENCE, userPreference.getUserPreferenceId())
                    .withSuccess(reply, CreateUserPreferenceReply.Success.TYPE);
        } catch (Exception e) {
            CreateUserPreferenceReply.Failure reply = new CreateUserPreferenceReply.Failure(
                    null,
                    UserPreferenceServiceApplicationCode.INTERNAL_SERVER_ERROR,
                    "Failed to create user preference",
                    LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
            );
            return withFailure(reply, CreateUserPreferenceReply.Failure.TYPE);
        }
    }

    private Message handleUndoCreateUserPreferenceCommand(CommandMessage<CreateUserPreferenceCommand.Undo> cmd) {
        try {
            CreateUserPreferenceCommand.Undo command = cmd.getCommand();
            String userPreferenceId = command.getUserPreferenceId();
            userPreferenceService.undoCreate(userPreferenceId);
            return withSuccess();
        } catch (Exception e) {
            return withFailure();
        }
    }
}
