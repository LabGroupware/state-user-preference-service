package org.cresplanex.api.state.userpreferenceservice.event.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.cresplanex.core.events.common.DomainEvent;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BeginJobEvent implements DomainEvent {

    private String jobId;
    private List<String> toActionCodes;
    private String pendingActionCode;
    private String timestamp;
}
