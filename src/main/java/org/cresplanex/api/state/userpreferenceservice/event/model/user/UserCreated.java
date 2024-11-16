package org.cresplanex.api.state.userpreferenceservice.event.model.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.cresplanex.api.state.userpreferenceservice.event.model.BaseEvent;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserCreated implements UserDomainEvent, BaseEvent {
    public static final String TYPE = "org.cresplanex.account.event.User.Created";

    private String userId;
    private String name;
    private String email;
    private String nickname;

    public String getType() {
        return TYPE;
    }
}
