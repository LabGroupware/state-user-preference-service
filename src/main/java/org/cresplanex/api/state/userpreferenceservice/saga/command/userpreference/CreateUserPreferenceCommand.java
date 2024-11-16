package org.cresplanex.api.state.userpreferenceservice.saga.command.userpreference;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CreateUserPreferenceCommand extends UserPreferenceSagaCommand {
    public static final String TYPE = "org.cresplanex.nova.service.userprofile.saga.command.userpreference.CreateUserPreferenceCommand";

    private String userId;
}
