package org.cresplanex.api.state.userpreferenceservice.saga.command.userpreference;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class UpdateUserPreferenceCommand extends UserPreferenceSagaCommand {
    public static final String TYPE = "org.cresplanex.nova.service.userpreference.saga.command.userpreference.UpdateUserPreferenceCommand";

    private String userPreferenceId;
    private String theme;
    private String language;
    private String timezone;
}
