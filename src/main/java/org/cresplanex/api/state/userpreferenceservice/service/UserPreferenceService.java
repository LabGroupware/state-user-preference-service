package org.cresplanex.api.state.userpreferenceservice.service;

import lombok.extern.slf4j.Slf4j;
import org.cresplanex.api.state.common.entity.EntityWithPrevious;
import org.cresplanex.api.state.common.entity.ListEntityWithCount;
import org.cresplanex.api.state.common.enums.PaginationType;
import org.cresplanex.api.state.common.service.BaseService;
import org.cresplanex.api.state.userpreferenceservice.entity.UserPreferenceEntity;
import org.cresplanex.api.state.userpreferenceservice.enums.UserPreferenceSortType;
import org.cresplanex.api.state.userpreferenceservice.exception.UserPreferenceNotFoundException;
import org.cresplanex.api.state.userpreferenceservice.filter.userpreference.LanguageFilter;
import org.cresplanex.api.state.userpreferenceservice.repository.UserPreferenceRepository;
import org.cresplanex.api.state.userpreferenceservice.saga.model.userpreference.UpdateUserPreferenceSaga;
import org.cresplanex.api.state.userpreferenceservice.saga.state.userpreference.UpdateUserPreferenceSagaState;
import org.cresplanex.api.state.userpreferenceservice.specification.UserPreferenceSpecifications;
import org.cresplanex.core.saga.orchestration.SagaInstanceFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
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

    @Transactional(readOnly = true)
    public UserPreferenceEntity findById(String userPreferenceId) {
        return internalFindById(userPreferenceId);
    }

    private UserPreferenceEntity internalFindById(String userPreferenceId) {
        return userPreferenceRepository.findById(userPreferenceId).orElseThrow(() -> new UserPreferenceNotFoundException(
                UserPreferenceNotFoundException.FindType.BY_ID,
                userPreferenceId
        ));
    }

    @Transactional(readOnly = true)
    public UserPreferenceEntity findByUserId(String userId) {
        return userPreferenceRepository.findByUserId(userId).orElseThrow(() -> new UserPreferenceNotFoundException(
                UserPreferenceNotFoundException.FindType.BY_USER_ID,
                userId
        ));
    }

    @Transactional(readOnly = true)
    public ListEntityWithCount<UserPreferenceEntity> get(
            PaginationType paginationType,
            int limit,
            int offset,
            String cursor,
            UserPreferenceSortType sortType,
            boolean withCount,
            LanguageFilter languageFilter
    ) {
        Specification<UserPreferenceEntity> spec = Specification.where(
                UserPreferenceSpecifications.withLanguageFilter(languageFilter));

        List<UserPreferenceEntity> data = switch (paginationType) {
            case OFFSET ->
                    userPreferenceRepository.findListWithOffsetPagination(spec, sortType, PageRequest.of(offset / limit, limit));
            case CURSOR -> userPreferenceRepository.findList(spec, sortType); // TODO: Implement cursor pagination
            default -> userPreferenceRepository.findList(spec, sortType);
        };

        int count = 0;
        if (withCount){
            count = userPreferenceRepository.countList(spec);
        }
        return new ListEntityWithCount<>(
                data,
                count
        );
    }

    @Transactional(readOnly = true)
    public List<UserPreferenceEntity> getByUserIds(
            List<String> userIds,
            UserPreferenceSortType sortType
    ) {
        return userPreferenceRepository.findListByUserIds(userIds, sortType);
    }

    @Transactional(readOnly = true)
    public List<UserPreferenceEntity> getByUserPreferenceIds(
            List<String> userPreferenceIds,
            UserPreferenceSortType sortType
    ) {
        return userPreferenceRepository.findListByUserPreferenceIds(userPreferenceIds, sortType);
    }

    public UserPreferenceEntity create(UserPreferenceEntity preference) {
        preference = userPreferenceRepository.save(preference);
        return preference;
    }

    public void undoCreate(String userPreferenceId) {
        userPreferenceRepository.deleteById(userPreferenceId);
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

    public EntityWithPrevious<UserPreferenceEntity> update(String operatorId, String userPreferenceId, UserPreferenceEntity preference) {
        UserPreferenceEntity existingPreference = internalFindById(userPreferenceId);
        UserPreferenceEntity updatedPreference = existingPreference.clone();
        updatedPreference.setLanguage(preference.getLanguage());
        updatedPreference.setTheme(preference.getTheme());
        updatedPreference.setTimezone(preference.getTimezone());
        return new EntityWithPrevious<>(userPreferenceRepository.save(updatedPreference), existingPreference);
    }

    public void undoUpdate(String userPreferenceId, UserPreferenceEntity preference) {
        UserPreferenceEntity existingPreference = internalFindById(userPreferenceId);
        existingPreference.setLanguage(preference.getLanguage());
        existingPreference.setTheme(preference.getTheme());
        existingPreference.setTimezone(preference.getTimezone());
        userPreferenceRepository.save(existingPreference);
    }
}
