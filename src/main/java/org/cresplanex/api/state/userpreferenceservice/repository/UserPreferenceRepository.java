package org.cresplanex.api.state.userpreferenceservice.repository;

import org.cresplanex.api.state.userpreferenceservice.entity.UserPreferenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserPreferenceRepository extends JpaRepository<UserPreferenceEntity, String> {

    Optional<UserPreferenceEntity> findByUserId(String userId);
}
