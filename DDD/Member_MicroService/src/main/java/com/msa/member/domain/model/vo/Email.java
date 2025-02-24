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
public class Email {
    public final String address;

    public static Email sample(){
        return new Email("scant10@gmail.com");
    }
}
