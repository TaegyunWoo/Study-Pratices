package com.msa.book.domain.model.vo;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Embeddable;

/**
 * 대여자 VO
 */
@Getter
@Access(AccessType.FIELD)
@NoArgsConstructor(force = true, access = AccessLevel.PROTECTED)
@RequiredArgsConstructor
@Embeddable
public class IDName {
    private final String id;
    private final String name;

    /**
     * 테스트용
     * @return
     */
    public static IDName sample() {
        return new IDName("scant", "han");
    }

    /**
     * 테스트용
     * @param args
     */
    public static void main(String[] args) {
        System.out.println(sample());
    }
}
