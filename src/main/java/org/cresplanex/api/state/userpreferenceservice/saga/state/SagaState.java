package org.cresplanex.api.state.userpreferenceservice.saga.state;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.cresplanex.api.state.userpreferenceservice.entity.BaseEntity;

@Setter
@Getter
@NoArgsConstructor
public abstract class SagaState<Action extends Enum<Action>, Entity extends BaseEntity> {

    private String jobId;
    private Action nextAction;
    private String startedAt;

    abstract public String getId();

    abstract public Class<Entity> getEntityClass();
}
