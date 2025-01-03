package org.cresplanex.api.state.userpreferenceservice.saga.state.userpreference;

import lombok.*;
import org.cresplanex.api.state.common.dto.userpreference.UserPreferenceDto;
import org.cresplanex.api.state.common.saga.command.userpreference.UpdateUserPreferenceCommand;
import org.cresplanex.api.state.common.saga.state.SagaState;
import org.cresplanex.api.state.userpreferenceservice.entity.UserPreferenceEntity;
import org.cresplanex.api.state.userpreferenceservice.saga.model.userpreference.UpdateUserPreferenceSaga;

@Getter
@Setter
@NoArgsConstructor
public class UpdateUserPreferenceSagaState
        extends SagaState<UpdateUserPreferenceSaga.Action, UserPreferenceEntity> {
    private InitialData initialData;
    private UserPreferenceDto userPreferenceDto = UserPreferenceDto.empty();
    private UserPreferenceDto prevUserPreferenceDto = UserPreferenceDto.empty();
    private String operatorId;

    @Override
    public String getId() {
        return initialData.userPreferenceId;
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

    public UpdateUserPreferenceCommand.Exec makeUpdateUserPreferenceCommand() {
        return new UpdateUserPreferenceCommand.Exec(
                this.operatorId,
                initialData.getUserPreferenceId(),
                initialData.getTheme(),
                initialData.getLanguage(),
                initialData.getTimezone()
        );
    }

    public UpdateUserPreferenceCommand.Undo makeUndoUpdateUserPreferenceCommand() {
        return new UpdateUserPreferenceCommand.Undo(
                userPreferenceDto.getUserPreferenceId(),
                prevUserPreferenceDto.getTheme(),
                prevUserPreferenceDto.getLanguage(),
                prevUserPreferenceDto.getTimezone()
        );
    }
}
