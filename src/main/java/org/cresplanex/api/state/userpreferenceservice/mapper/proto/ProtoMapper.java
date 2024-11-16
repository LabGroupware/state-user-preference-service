package org.cresplanex.api.state.userpreferenceservice.mapper.proto;

import build.buf.gen.userpreference.v1.UserPreference;
import org.cresplanex.api.state.userpreferenceservice.entity.UserPreferenceEntity;

import org.cresplanex.api.state.userpreferenceservice.utils.ValueFromNullable;

public class ProtoMapper {

    public static UserPreference convert(UserPreferenceEntity userPreferenceEntity) {

        return UserPreference.newBuilder()
                .setUserPreferenceId(userPreferenceEntity.getUserPreferenceId())
                .setUserId(userPreferenceEntity.getUserId())
                .setLanguage(ValueFromNullable.toNullableString(userPreferenceEntity.getLanguage()))
                .setTheme(ValueFromNullable.toNullableString(userPreferenceEntity.getTheme()))
                .setTimezone(ValueFromNullable.toNullableString(userPreferenceEntity.getTimezone()))
                .setNotificationSettingId(ValueFromNullable.toNullableString(userPreferenceEntity.getNotificationSettingId()))
                .build();
    }
}
