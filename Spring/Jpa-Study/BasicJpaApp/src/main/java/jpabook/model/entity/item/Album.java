package jpabook.model.entity.item;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("A") // 부모테이블(ITEM)의 DTYPE 칼럼에 A로 저장된다.
public class Album extends Item {

    private String director;

    private String actor;


    //getter, setter

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getActor() {
        return actor;
    }

    public void setActor(String actor) {
        this.actor = actor;
    }
}
