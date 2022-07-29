package com.camping_rental.server.domain.cs_sms_template.service;

import com.camping_rental.server.domain.cs_sms_template.entity.CsSmsTemplateEntity;
import com.camping_rental.server.domain.cs_sms_template.repository.CsSmsTemplateRepository;
import com.camping_rental.server.domain.enums.DeletedFlagEnums;
import com.camping_rental.server.domain.exception.dto.NotMatchedFormatException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CsSmsTemplateService {
    private final CsSmsTemplateRepository csSmsTemplateRepository;

    public Page<CsSmsTemplateEntity> qSearchPageByRoomId(UUID roomId, Pageable pageable) {
        return csSmsTemplateRepository.qSelectPageByRoomId(roomId, pageable);
    }

    public void saveAndModify(CsSmsTemplateEntity csSmsTemplateEntity) {
        csSmsTemplateRepository.save(csSmsTemplateEntity);
    }

    public CsSmsTemplateEntity searchByIdOrThrow(UUID csSmsTemplateId) {
        return csSmsTemplateRepository.findById(csSmsTemplateId).orElseThrow(() -> new NotMatchedFormatException("해당 데이터를 찾을 수 없습니다."));
    }

    public List<CsSmsTemplateEntity> searchListByRoomId(UUID roomId) {
        return csSmsTemplateRepository.findByRoomId(roomId);
    }

    public void logicalDelete(CsSmsTemplateEntity csSmsTemplateEntity) {
        csSmsTemplateEntity.setDeletedFlag(DeletedFlagEnums.DELETED.getValue());
    }
}
