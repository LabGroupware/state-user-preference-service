package org.cresplanex.api.state.userpreferenceservice.mapper.dto;

import org.cresplanex.api.state.common.dto.userpreference.UserPreferenceDto;
import org.cresplanex.api.state.userpreferenceservice.entity.UserPreferenceEntity;

public class DtoMapper {

    public static UserPreferenceDto convert(UserPreferenceEntity userPreferenceEntity) {
        return UserPreferenceDto.builder()
                .userPreferenceId(userPreferenceEntity.getUserPreferenceId())
                .userId(userPreferenceEntity.getUserId())
                .language(userPreferenceEntity.getLanguage())
                .theme(userPreferenceEntity.getTheme())
                .timezone(userPreferenceEntity.getTimezone())
                .notificationSettingId(userPreferenceEntity.getNotificationSettingId())
                .build();
    }
}
