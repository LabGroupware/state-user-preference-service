package org.cresplanex.api.state.userpreferenceservice.saga.command.userpreference;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class UndoCreateUserPreferenceCommand extends UserPreferenceSagaCommand {
    public static final String TYPE = "org.cresplanex.nova.service.userprofile.saga.command.userpreference.UndoCreateUserPreferenceCommand";

    private String userPreferenceId;
}
