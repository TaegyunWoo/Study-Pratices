package com.msa.rental.domain.model.vo;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

/**
 * VO 클래스
 * RentalCard 의 번호(식별자)
 */
@Getter
@Access(AccessType.FIELD)
@NoArgsConstructor(force = true, access = AccessLevel.PROTECTED)
@RequiredArgsConstructor
@Embeddable
public class RentalCardNo implements Serializable {
    private final String no;

    /**
     * RentalCardNo 생성 메서드
     * @return 생성된 RentalCardNo 객체
     */
    public static RentalCardNo createRentalCardNo() {
        UUID uuid = UUID.randomUUID();
        String year = String.valueOf(LocalDate.now().getYear());
        return new RentalCardNo(year + "-" + uuid);
    }

    /**
     * 테스트용
     * @return
     */
    public static RentalCardNo sample() {
        return RentalCardNo.createRentalCardNo();
    }

    /**
     * 테스트용
     * @param args
     */
    public static void main(String[] args) {
        System.out.println(RentalCardNo.sample());
    }
}
