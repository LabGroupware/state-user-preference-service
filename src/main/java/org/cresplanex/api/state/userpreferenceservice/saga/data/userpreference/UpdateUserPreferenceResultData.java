package org.cresplanex.api.state.userpreferenceservice.saga.data.userpreference;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.cresplanex.api.state.userpreferenceservice.dto.userpreference.UserPreferenceDto;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UpdateUserPreferenceResultData {

    private UserPreferenceDto userPreference;
}
