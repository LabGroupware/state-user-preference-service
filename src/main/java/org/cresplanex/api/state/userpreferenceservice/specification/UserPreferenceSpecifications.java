package org.cresplanex.api.state.userpreferenceservice.specification;

import jakarta.persistence.criteria.Predicate;
import org.cresplanex.api.state.userpreferenceservice.entity.UserPreferenceEntity;
import org.cresplanex.api.state.userpreferenceservice.filter.userpreference.LanguageFilter;
import org.hibernate.type.descriptor.java.StringJavaType;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class UserPreferenceSpecifications {

    public static Specification<UserPreferenceEntity> whereUserIds(Iterable<String> userIds) {
        List<String> userIdList = new ArrayList<>();
        userIds.forEach(userId -> {
            userIdList.add(new StringJavaType().wrap(userId, null));
        });

        return (root, query, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction();
            predicate = criteriaBuilder.and(predicate, root.get("userId").in(userIdList));
            return predicate;
        };
    }

    public static Specification<UserPreferenceEntity> whereUserPreferenceIds(Iterable<String> userPreferenceIds) {
        List<String> userPreferenceIdList = new ArrayList<>();
        userPreferenceIds.forEach(userPreferenceId -> {
            userPreferenceIdList.add(new StringJavaType().wrap(userPreferenceId, null));
        });

        return (root, query, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction();
            predicate = criteriaBuilder.and(predicate, root.get("userPreferenceId").in(userPreferenceIdList));
            return predicate;
        };
    }

    public static Specification<UserPreferenceEntity> withLanguageFilter(LanguageFilter languageFilter) {
        List<String> languageList = new ArrayList<>();
        if (languageFilter != null && languageFilter.isValid()) {
            languageFilter.getLanguages().forEach(language -> {
                languageList.add(new StringJavaType().wrap(language, null));
            });
        }

        return (root, query, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction();
            if (languageFilter != null && languageFilter.isValid()) {
                predicate = criteriaBuilder.and(predicate, root.get("language").in(languageList));
            }
            return predicate;
        };
    }
}
