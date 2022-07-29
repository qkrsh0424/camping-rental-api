package com.camping_rental.server.domain.cs_sms_template.repository;

import com.camping_rental.server.domain.cs_sms_template.entity.CsSmsTemplateEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CsSmsTemplateRepositoryCustom {
    Page<CsSmsTemplateEntity> qSelectPageByRoomId(UUID roomId, Pageable pageable);
}
