package com.msa.member.domain.model.vo;

import lombok.*;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Embeddable;

@ToString
@Getter
@Access(AccessType.FIELD)
@NoArgsConstructor(force = true, access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Embeddable
public class Point {
    public long pointValue ;

    public Point addPoint(Point point){
        return new Point(this.pointValue + point.pointValue);
    }

    public Point removePoint(Point point) throws Exception {
        if(point.pointValue > this.pointValue) {
            throw new Exception("기존 보유 Point보다 적어 삭제할 수 없습니다.");
        }
        return new Point(this.pointValue - point.pointValue);
    }

    public static Point createPoint()
    {
        return new Point(0L);
    }
    public static Point sample(){
        return new Point(10L);
    }
}

