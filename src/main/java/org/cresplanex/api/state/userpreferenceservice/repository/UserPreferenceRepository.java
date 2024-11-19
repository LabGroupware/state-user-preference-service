package org.cresplanex.api.state.userpreferenceservice.repository;

import org.cresplanex.api.state.userpreferenceservice.entity.UserPreferenceEntity;
import org.cresplanex.api.state.userpreferenceservice.enums.UserPreferenceSortType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserPreferenceRepository extends JpaRepository<UserPreferenceEntity, String>, JpaSpecificationExecutor<UserPreferenceEntity> {

    Optional<UserPreferenceEntity> findByUserId(String userId);

    /**
     * List<UserPreferenceId> の数を取得
     *
     * @param userPreferenceIds ユーザー設定IDリスト
     * @return ユーザー設定IDの数
     */
    Optional<Long> countByUserPreferenceIdIn(List<String> userPreferenceIds);

    @Query("SELECT u FROM UserPreferenceEntity u WHERE u.userId IN :userIds ORDER BY " +
            "CASE WHEN :sortType = 'CREATED_AT_ASC' THEN u.createdAt END ASC, " +
            "CASE WHEN :sortType = 'CREATED_AT_DESC' THEN u.createdAt END DESC")
    List<UserPreferenceEntity> findListByUserIds(List<String> userIds, UserPreferenceSortType sortType);

    @Query("SELECT u FROM UserPreferenceEntity u WHERE u.userPreferenceId IN :userPreference ORDER BY " +
            "CASE WHEN :sortType = 'CREATED_AT_ASC' THEN u.createdAt END ASC, " +
            "CASE WHEN :sortType = 'CREATED_AT_DESC' THEN u.createdAt END DESC")
    List<UserPreferenceEntity> findListByUserPreferenceIds(List<String> userPreferenceIds, UserPreferenceSortType sortType);

    @Query("SELECT u FROM UserPreferenceEntity u ORDER BY " +
            "CASE WHEN :sortType = 'CREATED_AT_ASC' THEN u.createdAt END ASC, " +
            "CASE WHEN :sortType = 'CREATED_AT_DESC' THEN u.createdAt END DESC")
    List<UserPreferenceEntity> findList(Specification<UserPreferenceEntity> specification, UserPreferenceSortType sortType);

    @Query("SELECT u FROM UserPreferenceEntity u ORDER BY " +
            "CASE WHEN :sortType = 'CREATED_AT_ASC' THEN u.createdAt END ASC, " +
            "CASE WHEN :sortType = 'CREATED_AT_DESC' THEN u.createdAt END DESC")
    List<UserPreferenceEntity> findListWithOffsetPagination(Specification<UserPreferenceEntity> specification,
                                                            UserPreferenceSortType sortType, Pageable pageable);

    @Query("SELECT COUNT(u) FROM UserPreferenceEntity u")
    int countList();
}
