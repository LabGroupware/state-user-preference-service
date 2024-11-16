package org.cresplanex.api.state.userpreferenceservice.saga.command.userpreference;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class UndoUpdateUserPreferenceCommand extends UserPreferenceSagaCommand {
    public static final String TYPE = "org.cresplanex.nova.service.userpreference.saga.command.userpreference.UndoUpdateUserPreferenceCommand";

    private String userPreferenceId;
    private String originTheme;
    private String originLanguage;
    private String originTimezone;
}
