package com.msa.book.domain.model.vo;

import lombok.*;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Embeddable;
import java.time.LocalDate;

@Getter
@Access(AccessType.FIELD)
@NoArgsConstructor(force = true, access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Embeddable
public class BookDesc {
    private final String description;
    private final String author;
    private final String isbn;
    private final LocalDate publicationDate;
    private final Source source;

    public static BookDesc createBookeDesc(String author, String isbn, String description, LocalDate publicationDate, Source source) {
        return new BookDesc(description, author, isbn, publicationDate, source);
    }

    public static BookDesc sample() {
        return createBookeDesc("마틴파울러", "1231323", "설명~!~!", LocalDate.now(), Source.SUPPLY);
    }
}
