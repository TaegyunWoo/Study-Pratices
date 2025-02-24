package com.msa.rental.domain.model;

import com.msa.rental.domain.model.event.ItemRented;
import com.msa.rental.domain.model.event.ItemReturned;
import com.msa.rental.domain.model.event.OverdueCleared;
import com.msa.rental.domain.model.vo.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

/**
 * 대여 마이크로서비스의 Aggregate Entity (Root Entity)
 */
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class RentalCard {
    @EmbeddedId
    private RentalCardNo rentalCardNo; //렌탈카드 번호 (식별자)
    @Embedded
    private IDName member; //대여자
    private RentStatus rentStatus; //대여 상태
    @Embedded
    private LateFee lateFee; //연체료
    @ElementCollection
    private List<RentalItem> rentalItemList = new ArrayList<>(); //대여 항목
    @ElementCollection
    private List<ReturnItem> returnItemList = new ArrayList<>(); //반납 항목

    /**
     * 비즈니스 로직 - 대여카드 생성
     * @param creator 생성자
     * @return
     */
    public static RentalCard createRentalCard(IDName creator) {
        RentalCard rentalCard = new RentalCard();
        rentalCard.setRentalCardNo(RentalCardNo.createRentalCardNo());
        rentalCard.setMember(creator);
        rentalCard.setRentStatus(RentStatus.RENT_AVAILABLE);
        rentalCard.setLateFee(LateFee.createLateFee());
        return rentalCard;
    }

    /**
     * 대여 이벤트 객체 생성 (이벤트 생성도 루트 엔티티에서 책임진다.)
     */
    public static ItemRented createItemRentedEvent(IDName idName, Item item, long point) {
        return new ItemRented(idName, item, point);
    }

    /**
     * 반납 이벤트 객체 생성 (이벤트 생성도 루트 엔티티에서 책임진다.)
     */
    public static ItemReturned createItemReturnedEvent(IDName idName, Item item, long point) {
        return new ItemReturned(idName, item, point);
    }

    /**
     * 연체 해제 이벤트 객체 생성 (이벤트 생성도 루트 엔티티에서 책임진다.)
     */
    public static OverdueCleared createOverdueClearedEvent(IDName idName, long point) {
        return new OverdueCleared(idName, point);
    }

    /**
     * 비즈니스 로직 - 대여처리
     * @param item 대여할 도서 항목
     * @return
     */
    public RentalCard rentItem(Item item) {
        checkRentalAvailable();
        this.addRentalItem(RentalItem.createRentalItem(item));
        return this;
    }

    /**
     * 비즈니스 로직 - 반납처리
     * @param item
     * @param returnDate
     * @return
     */
    public RentalCard returnItem(Item item, LocalDate returnDate) {
        RentalItem rentalItem = this.rentalItemList.stream().filter(
                        i -> i.getItem().getNo().equals(item.getNo())
                ).findFirst()
                .get();
        this.calculateLateFee(rentalItem, returnDate);
        this.addReturnItem(ReturnItem.createReturnItem(rentalItem));
        this.removeRentalItem(rentalItem);
        return this;
    }

    /**
     * 비즈니스 로직 - 연체처리
     * @param item
     * @return
     */
    public RentalCard overdueItem(Item item) {
        RentalItem rentalItem = this.rentalItemList.stream().filter(
                        i -> i.getItem().getNo().equals(item.getNo())
                ).findFirst()
                .get();
        RentalItem overduedRentalItem = RentalItem.createOverduedRentalItem(rentalItem); //연체된 렌탈아이템 생성
        this.rentalItemList.remove(rentalItem); //기존 렌탈아이템 제거
        this.rentalItemList.add(overduedRentalItem); //연체된 렌탈아이템 추가

        this.setRentStatus(RentStatus.RENT_UNAVAILABLE);
        return this;
    }

    /**
     * 비즈니스 로직 - 연체 해제
     * @param point
     * @return
     */
    public long makeAvailableRental(long point) throws Exception {
        if (this.rentalItemList.size() != 0)
            throw new IllegalArgumentException("모든 도서가 반납되어야 정지를 해제할 수 있습니다.");
        if (this.getLateFee().getPoint() != point)
            throw new IllegalArgumentException("해당 포인트로 연체를 해제할 수 없습니다.");

        this.setLateFee(lateFee.removePoint(point));
        if (this.getLateFee().getPoint() == 0) {
            this.rentStatus = RentStatus.RENT_AVAILABLE;
        }
        return this.getLateFee().getPoint();
    }

    private void calculateLateFee(RentalItem rentalItem, LocalDate returnDate) {
        if (returnDate.compareTo(rentalItem.getOverdueDate()) > 0) {
            int point = Period.between(rentalItem.getOverdueDate(), returnDate).getDays() * 10;
            this.setLateFee(this.lateFee.addPoint(point)); // lateFee.addPoint(point) 는 포인트가 변경된 새로운 객체를 반환한다.
        }
    }

    private void checkRentalAvailable() {
        if (this.rentStatus == RentStatus.RENT_UNAVAILABLE)
            throw new IllegalArgumentException("대여불가상태입니다.");
        if (this.rentalItemList.size() > 5) throw new IllegalArgumentException("이미 5권을 대여했습니다.");
    }

    private void addRentalItem(RentalItem rentalItem) {
        this.rentalItemList.add(rentalItem);
    }

    private void removeRentalItem(RentalItem rentalItem) {
        this.rentalItemList.remove(rentalItem);
    }

    private void addReturnItem(ReturnItem returnItem) {
        this.returnItemList.add(returnItem);
    }

    /**
     * 테스트용
     * @return
     */
    public static RentalCard sample() {
        RentalCard rentalCard = new RentalCard();
        rentalCard.setRentalCardNo(RentalCardNo.createRentalCardNo());
        rentalCard.setMember(IDName.sample());
        rentalCard.setRentStatus(RentStatus.RENT_AVAILABLE);
        rentalCard.setLateFee(LateFee.sample());
        return rentalCard;
    }
}
