package com.msa.rental.domain.model.vo;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Embeddable;

/**
 * 연체료 VO
 */
@Getter
@Access(AccessType.FIELD)
@NoArgsConstructor(force = true, access = AccessLevel.PROTECTED)
@RequiredArgsConstructor
@Embeddable
public class LateFee {
    private final long point;

    public LateFee addPoint(long point) {
        return new LateFee(this.point + point); // REMARK : LateFee는 VO이기에, 포인트 변경시 새로운 객체를 만들고 해당 객체에 반영한다.
    }

    public LateFee removePoint(long point) throws Exception {
        if (point > this.point) {
            throw new Exception("보유한 포인트보다 커서 삭제할 수 없습니다.");
        }

        return new LateFee(this.point - point); // REMARK : LateFee는 VO이기에, 포인트 변경시 새로운 객체를 만들고 해당 객체에 반영한다.
    }

    /**
     * LateFee 생성 메서드
     * @return
     */
    public static LateFee createLateFee() {
        return new LateFee(0);
    }

    /**
     * 테스트용
     * @return
     */
    public static LateFee sample() {
        return new LateFee(100);
    }
}
