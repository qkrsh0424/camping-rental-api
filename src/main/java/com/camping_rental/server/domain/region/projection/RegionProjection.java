package com.camping_rental.server.domain.region.projection;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

public class RegionProjection {
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SidoAndSigungus{
        private String sido;
        private Set<String> sigungus;
    }
}
