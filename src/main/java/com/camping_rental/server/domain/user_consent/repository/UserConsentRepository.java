package com.camping_rental.server.domain.user_consent.repository;

import com.camping_rental.server.domain.user_consent.entity.UserConsentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserConsentRepository extends JpaRepository<UserConsentEntity, Integer> {
}
