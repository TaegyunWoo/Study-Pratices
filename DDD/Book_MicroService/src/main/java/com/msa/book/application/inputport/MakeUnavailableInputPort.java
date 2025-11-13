package com.msa.book.application.inputport;

import com.msa.book.application.outputport.BookOutputPort;
import com.msa.book.application.usecase.MakeUnavailableUsecase;
import com.msa.book.domain.model.Book;
import com.msa.book.framework.web.dto.BookOutputDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MakeUnavailableInputPort implements MakeUnavailableUsecase {
    private final BookOutputPort bookOutputPort;

    @Override
    public BookOutputDto unavailable(long bookNo) {
        Book book = bookOutputPort.loadBook(bookNo);
        book.makeUnavailable();
        return BookOutputDto.mapToDTO(book);
    }
}
