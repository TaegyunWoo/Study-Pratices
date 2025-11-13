package com.msa.book.application.inputport;

import com.msa.book.application.outputport.BookOutputPort;
import com.msa.book.application.usecase.AddBookUsecase;
import com.msa.book.domain.model.Book;
import com.msa.book.domain.model.vo.Classfication;
import com.msa.book.domain.model.vo.Location;
import com.msa.book.domain.model.vo.Source;
import com.msa.book.framework.web.dto.BookInfoDto;
import com.msa.book.framework.web.dto.BookOutputDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AddBookInputPort implements AddBookUsecase {
    private final BookOutputPort bookOutputPort;

    @Override
    public BookOutputDto addBook(BookInfoDto bookInfoDto) {
        Book book = Book.enterBook(
                bookInfoDto.getTitle(),
                bookInfoDto.getAuthor(),
                bookInfoDto.getIsbn(),
                bookInfoDto.getDescription(),
                bookInfoDto.getPublicationDate(),
                Source.valueOf(bookInfoDto.getSource()),
                Classfication.valueOf(bookInfoDto.getClassfication()),
                Location.valueOf(bookInfoDto.getLocation())
        );
        Book save = bookOutputPort.save(book);
        return BookOutputDto.mapToDTO(save);
    }
}
