package org.cresplanex.api.state.userpreferenceservice.specification;

import jakarta.persistence.criteria.Predicate;
import org.cresplanex.api.state.userpreferenceservice.entity.UserPreferenceEntity;
import org.cresplanex.api.state.userpreferenceservice.filter.userpreference.LanguageFilter;
import org.springframework.data.jpa.domain.Specification;

public class UserPreferenceSpecifications {

    public static Specification<UserPreferenceEntity> withLanguageFilter(LanguageFilter languageFilter) {
        return (root, query, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction();
            if (languageFilter != null && languageFilter.isValid()) {
                if (languageFilter.getLanguages() != null && !languageFilter.getLanguages().isEmpty()) {
                    predicate = criteriaBuilder.and(predicate, root.get("language").in(languageFilter.getLanguages()));
                }
            }
            return predicate;
        };
    }
}
