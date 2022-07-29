package com.camping_rental.server.domain.cs_sms_template.repository;

import com.camping_rental.server.domain.cs_sms_template.entity.CsSmsTemplateEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CsSmsTemplateRepository extends JpaRepository<CsSmsTemplateEntity, Long>, CsSmsTemplateRepositoryCustom {
    Optional<CsSmsTemplateEntity> findById(UUID csSmsTemplateId);

    List<CsSmsTemplateEntity> findByRoomId(UUID roomId);
}
