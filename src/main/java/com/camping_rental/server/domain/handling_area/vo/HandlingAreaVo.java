package com.camping_rental.server.domain.handling_area.vo;

import com.camping_rental.server.domain.handling_area.entity.HandlingAreaEntity;
import lombok.*;

import javax.persistence.Column;
import java.util.UUID;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HandlingAreaVo {
    private Integer cid;
    private UUID id;
    private String name;

    public static HandlingAreaVo toVo(HandlingAreaEntity entity){
        HandlingAreaVo vo = HandlingAreaVo.builder()
                .id(entity.getId())
                .name(entity.getName())
                .build();
        return vo;
    }
}
