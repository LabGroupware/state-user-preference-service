package org.cresplanex.api.state.userpreferenceservice.saga.model;

import lombok.extern.slf4j.Slf4j;
import org.cresplanex.api.state.userpreferenceservice.entity.BaseEntity;
import org.cresplanex.api.state.userpreferenceservice.event.EventDummyId;
import org.cresplanex.api.state.userpreferenceservice.event.model.BeginJobEvent;
import org.cresplanex.api.state.userpreferenceservice.event.model.FailedJobEvent;
import org.cresplanex.api.state.userpreferenceservice.event.model.ProcessedJobEvent;
import org.cresplanex.api.state.userpreferenceservice.event.model.SuccessfullyJobEvent;
import org.cresplanex.api.state.userpreferenceservice.event.publisher.AggregateDomainEventPublisher;
import org.cresplanex.api.state.userpreferenceservice.saga.reply.BaseFailureReply;
import org.cresplanex.api.state.userpreferenceservice.saga.reply.BaseSuccessfullyReply;
import org.cresplanex.api.state.userpreferenceservice.saga.state.SagaState;
import org.cresplanex.core.events.common.DomainEvent;
import org.cresplanex.core.events.publisher.DomainEventPublisher;
import org.cresplanex.core.saga.simpledsl.SimpleSaga;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Slf4j
public abstract class SagaModel<
        Entity extends BaseEntity,
        Event extends DomainEvent,
        Action extends Enum<Action>,
        State extends SagaState<Action, Entity>
> implements SimpleSaga<State>  {

    abstract protected AggregateDomainEventPublisher<Entity, Event> getDomainEventPublisher();
    abstract protected Action[] getActions();

    abstract protected String getBeginEventType();
    abstract protected String getProcessedEventType();
    abstract protected String getFailedEventType();
    abstract protected String getSuccessfullyEventType();

    @Autowired
    private DomainEventPublisher domainEventPublisher;

    public static final String ACTION_CODE_ATTRIBUTE = "actionCode";
    public static final String DETAIL_ATTRIBUTE = "detail";

    protected Object createErrorAttributes(SagaState<?, Entity> state, BaseFailureReply<?> reply){
        return Map.of(
                ACTION_CODE_ATTRIBUTE, state.getNextAction().name(),
                DETAIL_ATTRIBUTE, reply.getData()
        );
    }

    protected String getAggregateId(SagaState<Action, Entity> state){
        return state.getId() == null ? EventDummyId.NOT_INITIALIZED : state.getId();
    }

    protected Action getNext(Action current) {
        List<Action> actions = Arrays.asList(getActions());
        int index = actions.indexOf(current);
        if (index == actions.size() - 1) {
            return null;
        }
        return actions.get(index + 1);
    }

    protected List<String> getActionNames() {
        return Arrays.stream(getActions()).map(Enum::name).toList();
    }

    protected Action getStartAction() {
        return getActions()[0];
    }

    protected void handleFailureReply(State state, BaseFailureReply<?> reply) {
        this.domainEventPublisher.publish(
                getDomainEventPublisher().getChannel(),
                getAggregateId(state),
                Collections.singletonList(
                        new FailedJobEvent(
                                state.getJobId(),
                                reply.getData(),
                                state.getNextAction().name(),
                                reply.getCode(),
                                reply.getCaption(),
                                reply.getTimestamp(),
                                this.createErrorAttributes(state, reply)
                        )
                ),
                getFailedEventType());
    }

    protected <Data> void processedEventPublish(State state, BaseSuccessfullyReply<Data> reply) {
        Data data = reply.getData();
        this.domainEventPublisher.publish(
                getDomainEventPublisher().getChannel(),
                getAggregateId(state),
                Collections.singletonList(
                        new ProcessedJobEvent(
                                state.getJobId(),
                                data,
                                state.getNextAction().name(),
                                reply.getCode(),
                                reply.getCaption(),
                                reply.getTimestamp()
                        )
                ),
                getProcessedEventType());
        state.setNextAction(getNext(state.getNextAction()));
    }

    @Override
    public void onStarting(String sagaId, State data) {
        data.setNextAction(getStartAction());
        data.setStartedAt(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));

        List<String> actionNames = this.getActionNames();

        String firstAction = actionNames.getFirst();
        List<String> nextActions = actionNames.subList(1, actionNames.size());

        this.domainEventPublisher.publish(
                getDomainEventPublisher().getChannel(),
                getAggregateId(data),
                Collections.singletonList(
                        new BeginJobEvent(
                                data.getJobId(),
                                nextActions,
                                firstAction,
                                data.getStartedAt()
                        )
                ),
                getBeginEventType());
    }

    protected <Data> void successfullyEventPublish(State state, Data resultData) {
        this.domainEventPublisher.publish(
                getDomainEventPublisher().getChannel(),
                getAggregateId(state),
                Collections.singletonList(new SuccessfullyJobEvent(state.getJobId(), resultData)),
                getSuccessfullyEventType()
        );
    }

    @Override
    public void onSagaRolledBack(String sagaId, State data) {
    }

    @Override
    public void onSagaFailed(String sagaId, State data) {
    }
}
