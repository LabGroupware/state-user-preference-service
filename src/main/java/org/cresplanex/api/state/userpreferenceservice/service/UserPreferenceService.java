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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

        Sort sort = createSort(sortType);

        Pageable pageable = switch (paginationType) {
            case OFFSET -> PageRequest.of(offset / limit, limit, sort);
            case CURSOR -> PageRequest.of(0, limit, sort); // TODO: Implement cursor pagination
            default -> Pageable.unpaged(sort);
        };

        Page<UserPreferenceEntity> data = userPreferenceRepository.findAll(spec, pageable);

        int count = 0;
        if (withCount){
            count = (int) data.getTotalElements();
        }
        return new ListEntityWithCount<>(
                data.getContent(),
                count
        );
    }

    @Transactional(readOnly = true)
    public List<UserPreferenceEntity> getByUserIds(
            List<String> userIds,
            UserPreferenceSortType sortType
    ) {
        Specification<UserPreferenceEntity> spec = Specification
                .where(UserPreferenceSpecifications.whereUserIds(userIds));

        return userPreferenceRepository.findAll(spec, createSort(sortType));
    }

    @Transactional(readOnly = true)
    public List<UserPreferenceEntity> getByUserPreferenceIds(
            List<String> userPreferenceIds,
            UserPreferenceSortType sortType
    ) {
        Specification<UserPreferenceEntity> spec = Specification
                .where(UserPreferenceSpecifications.whereUserPreferenceIds(userPreferenceIds));

        return userPreferenceRepository.findAll(spec, createSort(sortType));
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
        UserPreferenceEntity newPreference = internalFindById(userPreferenceId);
        UserPreferenceEntity existingPreference = newPreference.clone();
        newPreference.setLanguage(preference.getLanguage());
        newPreference.setTheme(preference.getTheme());
        newPreference.setTimezone(preference.getTimezone());
        return new EntityWithPrevious<>(userPreferenceRepository.save(newPreference), existingPreference);
    }

    public void undoUpdate(String userPreferenceId, UserPreferenceEntity preference) {
        UserPreferenceEntity existingPreference = internalFindById(userPreferenceId);
        existingPreference.setLanguage(preference.getLanguage());
        existingPreference.setTheme(preference.getTheme());
        existingPreference.setTimezone(preference.getTimezone());
        userPreferenceRepository.save(existingPreference);
    }

    private Sort createSort(UserPreferenceSortType sortType) {
        return switch (sortType) {
            case CREATED_AT_ASC -> Sort.by(Sort.Order.asc("createdAt"));
            case CREATED_AT_DESC -> Sort.by(Sort.Order.desc("createdAt"));
        };
    }
}
