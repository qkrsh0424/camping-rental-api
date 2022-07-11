package com.camping_rental.server.domain.rental_order_product.enums;

/**
 * 신규 주문 NEW_ORDER
 * 주문 확인 CONFIRM_ORDER
 * 예약 확정 CONFIRM_RESERVATION
 * 픽업 완료 PICKED_UP
 * 반납 완료 RETURNED
 * 종료됨 COMPLETED
 * 취소됨 CANCELLED
 */
public enum RentalOrderProductStatusEnum {
    NEW_ORDER("newOrder"),
    CONFIRM_ORDER("confirmOrder"),
    CONFIRM_RESERVATION("confirmReservation"),
    PICKED_UP("pickedUp"),
    RETURNED("returned"),
    COMPLETED("completed"),
    CANCELLED("cancelled");

    private String value;

    RentalOrderProductStatusEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
