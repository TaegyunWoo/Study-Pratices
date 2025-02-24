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
 * 대여 항목 서브 엔티티
 */
@Getter
@Access(AccessType.FIELD)
@NoArgsConstructor(force = true, access = AccessLevel.PROTECTED)
@RequiredArgsConstructor
@Embeddable
public class RentalItem {
    @Embedded
    private final Item item; //항목
    private final LocalDate rentDate; //대여일자
    private final boolean overdued; //연체 여부
    private final LocalDate overdueDate; //반납 예정일

    /**
     * RentalItem 생성 메서드
     * @param item
     * @return
     */
    public static RentalItem createRentalItem(Item item) {
        return new RentalItem(item, LocalDate.now(), false, LocalDate.now().plusDays(14));
    }

    /**
     * 연체된 RentalItem 생성 메서드
     * @param rentalItem
     * @return
     */
    public static RentalItem createOverduedRentalItem(RentalItem rentalItem) {
        LocalDate overdueDate = LocalDate.now().minusDays(1); // 연체 억지로 만들기 - 실제로는 필요없는 코드
        return new RentalItem(rentalItem.getItem(), rentalItem.getRentDate(), true, overdueDate);
    }

    /**
     * 테스트용
     * @return
     */
    public static RentalItem sample() {
        return RentalItem.createRentalItem(Item.sample());
    }
}
