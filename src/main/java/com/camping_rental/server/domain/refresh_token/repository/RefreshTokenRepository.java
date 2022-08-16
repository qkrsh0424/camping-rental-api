package com.camping_rental.server.domain.refresh_token.repository;

import com.camping_rental.server.domain.refresh_token.entity.RefreshTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshTokenEntity, Integer> {

    Optional<RefreshTokenEntity> findById(UUID id);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM refresh_token WHERE user_id=:userId AND cid NOT IN (SELECT tmp.* FROM (SELECT rt2.cid FROM refresh_token AS rt2 WHERE rt2.user_id=:userId ORDER BY rt2.updated_at DESC LIMIT :allowedAccessCount) AS tmp)", nativeQuery = true)
    void deleteOldRefreshTokenForUser(String userId, Integer allowedAccessCount);

    @Modifying
    @Query(value = "DELETE FROM refresh_token WHERE user_id=:userId", nativeQuery = true)
    void deleteAllByUserId(String userId);
}
