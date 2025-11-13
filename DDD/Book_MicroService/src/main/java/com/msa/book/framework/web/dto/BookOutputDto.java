package com.msa.book.framework.web.dto;

import com.msa.book.domain.model.Book;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BookOutputDto {
    private long bookNo;
    private String bookTitle;
    private String bookStatus;

    public static BookOutputDto mapToDTO(Book book)
    {
        BookOutputDto bookOutPutDTO = new BookOutputDto();
        bookOutPutDTO.setBookNo(book.getNo());
        bookOutPutDTO.setBookTitle(book.getTitle());
        bookOutPutDTO.setBookStatus(book.getBookStatus().toString());
        return bookOutPutDTO;
    }
}
