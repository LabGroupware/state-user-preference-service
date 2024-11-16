package org.cresplanex.api.state.userpreferenceservice.saga.proxy;

import org.cresplanex.api.state.userpreferenceservice.saga.SagaCommandChannel;
import org.cresplanex.api.state.userpreferenceservice.saga.command.userpreference.UndoUpdateUserPreferenceCommand;
import org.cresplanex.api.state.userpreferenceservice.saga.command.userpreference.UpdateUserPreferenceCommand;
import org.cresplanex.core.saga.simpledsl.CommandEndpoint;
import org.cresplanex.core.saga.simpledsl.CommandEndpointBuilder;
import org.springframework.stereotype.Component;

@Component
public class UserPreferenceServiceProxy {

    public final CommandEndpoint<UpdateUserPreferenceCommand> updateUserPreference
            = CommandEndpointBuilder
            .forCommand(UpdateUserPreferenceCommand.class)
            .withChannel(SagaCommandChannel.USER_PREFERENCE)
            .withCommandType(UpdateUserPreferenceCommand.TYPE)
            .build();

    public final CommandEndpoint<UndoUpdateUserPreferenceCommand> undoUpdateUserPreference
            = CommandEndpointBuilder
            .forCommand(UndoUpdateUserPreferenceCommand.class)
            .withChannel(SagaCommandChannel.USER_PREFERENCE)
            .withCommandType(UndoUpdateUserPreferenceCommand.TYPE)
            .build();
}
