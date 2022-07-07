package com.camping_rental.server.domain.product.service;

import com.camping_rental.server.domain.exception.dto.NotMatchedFormatException;
import com.camping_rental.server.domain.product.entity.ProductEntity;
import com.camping_rental.server.domain.product.enums.ProductDeletedFlagEnum;
import com.camping_rental.server.domain.product.projection.ProductProjection;
import com.camping_rental.server.domain.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    public void saveAndModify(ProductEntity entity){
        productRepository.save(entity);
    }

    public ProductProjection.FullJoin qSearchOneFullJoin(UUID productId) {
        return productRepository.qSelectOneFullJoin(productId).orElseThrow(()->new NotMatchedFormatException("요청 데이터를 찾을 수 없습니다."));
    }

    public List<ProductProjection.JoinRoomAndRegions> qSearchListJoinRoomAndRegions(UUID roomId, Map<String, Object> params){
        return productRepository.qSelectListJoinRoomAndRegions(roomId, params);
    }

    public Page<ProductProjection.JoinRoomAndRegions> qSearchPageJoinRoomAndRegions(Map<String, Object> params, Pageable pageable) {
        return productRepository.qSelectPageJoinRoomAndRegions(params, pageable);
    }

    public ProductProjection.JoinRoomAndRegions qSearchOneByIdJoinRoomAndRegionOrThrow(UUID productId) {
        return productRepository.qSelectOneByIdJoinRoomAndRegion(productId).orElseThrow(() -> new NotMatchedFormatException("요청 데이터를 찾을 수 없습니다."));
    }

    public List<ProductProjection.JoinRoomAndRegions> qSearchListByIdsJoinRoomAndRegions(List<UUID> productIds) {
        return productRepository.qSelectListByIdsJoinRoomAndRegions(productIds);
    }

    public void logicalDelete(ProductEntity entity) {
        entity.setDeletedFlag(ProductDeletedFlagEnum.DELETED.getValue());
    }

    public ProductEntity searchOneById(UUID productId) {
        return productRepository.findById(productId).orElseThrow(() -> new NotMatchedFormatException("요청 데이터를 찾을 수 없습니다."));
    }
}
