package com.msa.rental.domain.model.vo;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import java.time.LocalDate;

/**
 * 반환 항목 VOC
 */
@Getter
@Access(AccessType.FIELD)
@NoArgsConstructor(force = true, access = AccessLevel.PROTECTED)
@RequiredArgsConstructor
@Embeddable
public class ReturnItem {
    @Embedded
    private final RentalItem rentalItem;
    private final LocalDate returnDate;

    /**
     * ReturnItem 생성 메서드
     * @param rentalItem
     * @return
     */
    public static ReturnItem createReturnItem(RentalItem rentalItem) {
        return new ReturnItem(rentalItem, LocalDate.now());
    }

    /**
     * 테스트용
     * @return
     */
    public static ReturnItem sample() {
        return ReturnItem.createReturnItem(RentalItem.sample());
    }
}
