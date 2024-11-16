package org.cresplanex.api.state.userpreferenceservice.saga.dispatcher;

import lombok.AllArgsConstructor;
import org.cresplanex.api.state.common.constants.ServiceType;
import org.cresplanex.api.state.common.saga.SagaCommandChannel;
import org.cresplanex.api.state.common.saga.dispatcher.BaseSagaCommandDispatcher;
import org.cresplanex.api.state.userpreferenceservice.saga.handler.UserProfileSagaCommandHandlers;
import org.cresplanex.core.saga.participant.SagaCommandDispatcher;
import org.cresplanex.core.saga.participant.SagaCommandDispatcherFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@AllArgsConstructor
@Configuration
public class UserProfileSagaCommandDispatcher extends BaseSagaCommandDispatcher {

    @Bean
    public SagaCommandDispatcher userProfileSagaCommandHandlersDispatcher(
            UserProfileSagaCommandHandlers userProfileSagaCommandHandlers,
            SagaCommandDispatcherFactory sagaCommandDispatcherFactory) {
        return sagaCommandDispatcherFactory.make(
                this.getDispatcherId(ServiceType.NOVA_USER_PREFERENCE, SagaCommandChannel.USER_PROFILE),
                userProfileSagaCommandHandlers.commandHandlers());
    }
}
