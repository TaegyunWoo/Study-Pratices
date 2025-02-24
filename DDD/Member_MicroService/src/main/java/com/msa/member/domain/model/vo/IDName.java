package com.msa.member.domain.model.vo;

import lombok.*;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Embeddable;

@Getter
@Access(AccessType.FIELD)
@NoArgsConstructor(force = true, access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Embeddable
public class IDName  {
    private final String id;
    private final String name;

    public static IDName sample(){
        return new IDName("scant","jenny");
    }

    public static void main(String[] args) {
        System.out.println(sample().toString());
    }
}
