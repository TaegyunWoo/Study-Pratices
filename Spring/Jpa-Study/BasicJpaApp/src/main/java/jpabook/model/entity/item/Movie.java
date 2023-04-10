package jpabook.model.entity.item;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("M") // 부모테이블(ITEM)의 DTYPE 칼럼에 M로 저장된다.
public class Movie extends Item {

    private String artist;

    private String etc;

    //getter, setter

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getEtc() {
        return etc;
    }

    public void setEtc(String etc) {
        this.etc = etc;
    }
}
