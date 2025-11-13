package com.msa.book.domain.model.vo;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Embeddable;

/**
 * 도서 항목 VO
 */
@Getter
@Access(AccessType.FIELD)
@NoArgsConstructor(force = true, access = AccessLevel.PROTECTED)
@RequiredArgsConstructor
@Embeddable
public class Item {
    private final Long no; //도서번호
    private final String title; //도서제목

    /**
     * 테스트용
     * @return
     */
    public static Item sample() {
        return new Item(10L, "노인과 바다");
    }
}
