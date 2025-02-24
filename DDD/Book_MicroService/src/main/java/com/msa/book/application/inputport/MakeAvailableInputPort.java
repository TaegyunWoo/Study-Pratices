package com.msa.book.application.inputport;

import com.msa.book.application.outputport.BookOutputPort;
import com.msa.book.application.usecase.MakeAvailableUsecase;
import com.msa.book.domain.model.Book;
import com.msa.book.framework.web.dto.BookOutputDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class MakeAvailableInputPort implements MakeAvailableUsecase {
    private final BookOutputPort bookOutputPort;

    @Override
    public BookOutputDto available(Long bookNo) {
        Book book = bookOutputPort.loadBook(bookNo);
        book.makeAvailable();
        return BookOutputDto.mapToDTO(book);
    }
}
