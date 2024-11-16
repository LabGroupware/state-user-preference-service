package org.cresplanex.api.state.userpreferenceservice.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class EntityWithPrevious<Data> {

    private Data previous;
    private Data current;
}
