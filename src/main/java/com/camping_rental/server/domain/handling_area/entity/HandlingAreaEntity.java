package com.camping_rental.server.domain.handling_area.entity;

import lombok.*;

import javax.persistence.*;
import java.util.UUID;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "handling_area")
public class HandlingAreaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cid")
    private Integer cid;
    @Column(name = "id")
    private UUID id;
    @Column(name = "name")
    private String name;
}
