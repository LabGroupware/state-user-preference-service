package org.cresplanex.api.state.userpreferenceservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.cresplanex.api.state.common.entity.EntityWithPrevious;
import org.cresplanex.api.state.common.saga.local.userpreference.NotFoundUserPreferenceException;
import org.cresplanex.api.state.common.service.BaseService;
import org.cresplanex.api.state.userpreferenceservice.entity.UserPreferenceEntity;
import org.cresplanex.api.state.userpreferenceservice.exception.UserPreferenceNotFoundException;
import org.cresplanex.api.state.userpreferenceservice.repository.UserPreferenceRepository;
import org.cresplanex.api.state.userpreferenceservice.saga.model.userpreference.UpdateUserPreferenceSaga;
import org.cresplanex.api.state.userpreferenceservice.saga.state.userpreference.UpdateUserPreferenceSagaState;
import org.cresplanex.core.saga.orchestration.SagaInstanceFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserPreferenceLocalValidateService {

    private final UserPreferenceRepository userPreferenceRepository;

    public void validateUserPreferences(List<String> userPreferenceIds) throws NotFoundUserPreferenceException {
        userPreferenceRepository.countByUserPreferenceIdIn(userPreferenceIds)
                .ifPresent(count -> {
                    if (count != userPreferenceIds.size()) {
                        throw new NotFoundUserPreferenceException(userPreferenceIds);
                    }
                });
    }
}
