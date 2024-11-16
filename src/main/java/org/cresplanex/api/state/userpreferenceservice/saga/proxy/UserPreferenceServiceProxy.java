package org.cresplanex.api.state.userpreferenceservice.saga.proxy;

import org.cresplanex.api.state.common.saga.SagaCommandChannel;
import org.cresplanex.api.state.common.saga.command.userpreference.UpdateUserPreferenceCommand;
import org.cresplanex.core.saga.simpledsl.CommandEndpoint;
import org.cresplanex.core.saga.simpledsl.CommandEndpointBuilder;
import org.springframework.stereotype.Component;

@Component
public class UserPreferenceServiceProxy {

    public final CommandEndpoint<UpdateUserPreferenceCommand.Exec> updateUserPreference
            = CommandEndpointBuilder
            .forCommand(UpdateUserPreferenceCommand.Exec.class)
            .withChannel(SagaCommandChannel.USER_PREFERENCE)
            .withCommandType(UpdateUserPreferenceCommand.Exec.TYPE)
            .build();

    public final CommandEndpoint<UpdateUserPreferenceCommand.Undo> undoUpdateUserPreference
            = CommandEndpointBuilder
            .forCommand(UpdateUserPreferenceCommand.Undo.class)
            .withChannel(SagaCommandChannel.USER_PREFERENCE)
            .withCommandType(UpdateUserPreferenceCommand.Undo.TYPE)
            .build();
}
