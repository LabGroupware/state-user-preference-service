package org.cresplanex.api.state.userpreferenceservice.service;

import lombok.extern.slf4j.Slf4j;
import org.cresplanex.api.state.common.entity.EntityWithPrevious;
import org.cresplanex.api.state.common.service.BaseService;
import org.cresplanex.api.state.userpreferenceservice.entity.UserPreferenceEntity;
import org.cresplanex.api.state.userpreferenceservice.exception.UserPreferenceNotFoundException;
import org.cresplanex.api.state.userpreferenceservice.repository.UserPreferenceRepository;
import org.cresplanex.api.state.userpreferenceservice.saga.model.userpreference.UpdateUserPreferenceSaga;
import org.cresplanex.api.state.userpreferenceservice.saga.state.userpreference.UpdateUserPreferenceSagaState;
import org.cresplanex.core.saga.orchestration.SagaInstanceFactory;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserPreferenceService extends BaseService {

    private final UserPreferenceRepository userPreferenceRepository;
    private final SagaInstanceFactory sagaInstanceFactory;

    private final UpdateUserPreferenceSaga updateUserPreferenceSaga;

    public UserPreferenceEntity findById(String userPreferenceId) {
        return userPreferenceRepository.findById(userPreferenceId).orElseThrow(() -> {
            return new UserPreferenceNotFoundException(
                    UserPreferenceNotFoundException.FindType.BY_ID,
                    userPreferenceId
            );
        });
    }

    public UserPreferenceEntity findByUserId(String userId) {
        return userPreferenceRepository.findByUserId(userId).orElseThrow(() -> {
            return new UserPreferenceNotFoundException(
                    UserPreferenceNotFoundException.FindType.BY_USER_ID,
                    userId
            );
        });
    }

    public List<UserPreferenceEntity> get() {
        return userPreferenceRepository.findAll();
    }

    public UserPreferenceEntity create(UserPreferenceEntity preference) {
        preference = userPreferenceRepository.save(preference);
//        throw new UnsupportedOperationException("Not implemented");
        return preference;
    }

    public void undoCreate(String userPreferenceId) {
        UserPreferenceEntity preference = findById(userPreferenceId);
        if (preference == null) {
            throw new UserPreferenceNotFoundException(
                    UserPreferenceNotFoundException.FindType.BY_ID,
                    userPreferenceId
            );
        }
        userPreferenceRepository.delete(preference);
    }

    @Transactional
    public String beginUpdate(String operatorId, UserPreferenceEntity preference) {
        UpdateUserPreferenceSagaState.InitialData initialData = UpdateUserPreferenceSagaState.InitialData.builder()
                .userPreferenceId(preference.getUserPreferenceId())
                .language(preference.getLanguage())
                .theme(preference.getTheme())
                .timezone(preference.getTimezone())
                .build();
        UpdateUserPreferenceSagaState state = new UpdateUserPreferenceSagaState();
        state.setInitialData(initialData);
        state.setOperatorId(operatorId);

        String jobId = getJobId();
        state.setJobId(jobId);

        sagaInstanceFactory.create(updateUserPreferenceSaga, state);

        return jobId;
    }

    public EntityWithPrevious<UserPreferenceEntity> update(String userPreferenceId, UserPreferenceEntity preference) {
        UserPreferenceEntity existingPreference = findById(userPreferenceId);
        if (existingPreference == null) {
            throw new UserPreferenceNotFoundException(
                    UserPreferenceNotFoundException.FindType.BY_ID,
                    userPreferenceId
            );
        }
        UserPreferenceEntity updatedPreference = existingPreference.clone();
        updatedPreference.setLanguage(preference.getLanguage());
        updatedPreference.setTheme(preference.getTheme());
        updatedPreference.setTimezone(preference.getTimezone());
        return new EntityWithPrevious<>(userPreferenceRepository.save(updatedPreference), existingPreference);
    }

    public void undoUpdate(String userPreferenceId, UserPreferenceEntity preference) {
        UserPreferenceEntity existingPreference = findById(userPreferenceId);
        if (existingPreference == null) {
            throw new UserPreferenceNotFoundException(
                    UserPreferenceNotFoundException.FindType.BY_ID,
                    userPreferenceId
            );
        }
        existingPreference.setLanguage(preference.getLanguage());
        existingPreference.setTheme(preference.getTheme());
        existingPreference.setTimezone(preference.getTimezone());
        userPreferenceRepository.save(existingPreference);
    }
}
