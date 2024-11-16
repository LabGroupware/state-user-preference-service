package org.cresplanex.api.state.userpreferenceservice.service;

import build.buf.gen.job.v1.CreateJobRequest;
import build.buf.gen.job.v1.JobServiceGrpc;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.cresplanex.api.state.userpreferenceservice.entity.EntityWithPrevious;
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
@Transactional
public class UserPreferenceService {

    private final UserPreferenceRepository userPreferenceRepository;
    private final SagaInstanceFactory sagaInstanceFactory;

    @GrpcClient("jobService")
    private JobServiceGrpc.JobServiceBlockingStub jobServiceBlockingStub;

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
        return userPreferenceRepository.save(preference);
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

    public String beginUpdate(UserPreferenceEntity preference) {
        UpdateUserPreferenceSagaState.InitialData initialData = UpdateUserPreferenceSagaState.InitialData.builder()
                .userPreferenceId(preference.getUserPreferenceId())
                .language(preference.getLanguage())
                .theme(preference.getTheme())
                .timezone(preference.getTimezone())
                .build();
        UpdateUserPreferenceSagaState state = new UpdateUserPreferenceSagaState();
        state.setInitialData(initialData);

        String jobId = jobServiceBlockingStub.createJob(
                CreateJobRequest.newBuilder().build()
        ).getJobId();
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
