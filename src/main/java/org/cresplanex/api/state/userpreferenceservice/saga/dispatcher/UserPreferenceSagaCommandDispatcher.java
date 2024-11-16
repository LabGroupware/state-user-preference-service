package org.cresplanex.api.state.userpreferenceservice.saga.dispatcher;

import lombok.AllArgsConstructor;
import org.cresplanex.api.state.userpreferenceservice.config.ApplicationConfiguration;
import org.cresplanex.api.state.userpreferenceservice.saga.SagaCommandChannel;
import org.cresplanex.api.state.userpreferenceservice.saga.handler.UserPreferenceSagaCommandHandlers;
import org.cresplanex.core.saga.participant.SagaCommandDispatcher;
import org.cresplanex.core.saga.participant.SagaCommandDispatcherFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@AllArgsConstructor
@Configuration
public class UserPreferenceSagaCommandDispatcher {

    private final ApplicationConfiguration applicationConfiguration;

    @Bean
    public SagaCommandDispatcher userProfileSagaCommandHandlersDispatcher(
            UserPreferenceSagaCommandHandlers userPreferenceSagaCommandHandlers,
            SagaCommandDispatcherFactory sagaCommandDispatcherFactory) {
        return sagaCommandDispatcherFactory.make("%s.sagaCommandDispatcher.%s".formatted(
                applicationConfiguration.getName(), SagaCommandChannel.USER_PREFERENCE),
                userPreferenceSagaCommandHandlers.commandHandlers());
    }
}
