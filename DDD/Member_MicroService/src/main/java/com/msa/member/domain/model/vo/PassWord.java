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
public class PassWord {
    public final String presentPWD;
    public final String pastPWD;

    public static PassWord sample(){
        return new PassWord("12345","abcde");
    }
}
