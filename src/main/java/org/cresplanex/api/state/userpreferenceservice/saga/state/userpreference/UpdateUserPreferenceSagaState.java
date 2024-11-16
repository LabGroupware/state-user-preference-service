package org.cresplanex.api.state.userpreferenceservice.saga.state.userpreference;

import lombok.*;
import org.cresplanex.api.state.userpreferenceservice.dto.userpreference.UserPreferenceDto;
import org.cresplanex.api.state.userpreferenceservice.entity.UserPreferenceEntity;
import org.cresplanex.api.state.userpreferenceservice.saga.command.userpreference.UndoUpdateUserPreferenceCommand;
import org.cresplanex.api.state.userpreferenceservice.saga.command.userpreference.UpdateUserPreferenceCommand;
import org.cresplanex.api.state.userpreferenceservice.saga.model.userpreference.UpdateUserPreferenceSaga;
import org.cresplanex.api.state.userpreferenceservice.saga.state.SagaState;

@Getter
@Setter
@NoArgsConstructor
public class UpdateUserPreferenceSagaState extends SagaState<UpdateUserPreferenceSaga.Action, UserPreferenceEntity> {
    private InitialData initialData;
    private UserPreferenceDto userPreferenceDto;
    private UserPreferenceDto prevUserPreferenceDto;

    @Override
    public String getId() {
        return userPreferenceDto.getUserPreferenceId();
    }

    @Override
    public Class<UserPreferenceEntity> getEntityClass() {
        return UserPreferenceEntity.class;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class InitialData {
        private String userPreferenceId;
        private String theme;
        private String language;
        private String timezone;
    }

    public UpdateUserPreferenceCommand makeUpdateUserPreferenceCommand() {
        return new UpdateUserPreferenceCommand(
                initialData.getUserPreferenceId(),
                initialData.getTheme(),
                initialData.getLanguage(),
                initialData.getTimezone()
        );
    }

    public UndoUpdateUserPreferenceCommand makeUndoUpdateUserPreferenceCommand() {
        return new UndoUpdateUserPreferenceCommand(
                userPreferenceDto.getUserPreferenceId(),
                prevUserPreferenceDto.getTheme(),
                prevUserPreferenceDto.getLanguage(),
                prevUserPreferenceDto.getTimezone()
        );
    }
}
