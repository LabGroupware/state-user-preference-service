package org.cresplanex.api.state.userpreferenceservice.dto.userpreference;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UserPreferenceDto {

    private String userPreferenceId;

    private String userId;

    private String timezone;

    private String theme;

    private String language;

    private String notificationSettingId;
}
