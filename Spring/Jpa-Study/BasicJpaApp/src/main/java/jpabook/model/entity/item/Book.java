package jpabook.model.entity.item;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("B") // 부모테이블(ITEM)의 DTYPE 칼럼에 B로 저장된다.
public class Book extends Item {

    private String author;

    private String isbn;

    // getter, setter

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }
}
