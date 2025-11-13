package com.msa.book.application.inputport;

import com.msa.book.application.outputport.BookOutputPort;
import com.msa.book.application.usecase.InquiryUsecase;
import com.msa.book.domain.model.Book;
import com.msa.book.framework.web.dto.BookOutputDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@RequiredArgsConstructor
@Service
@Transactional
public class InquiryInputPort implements InquiryUsecase {

    private final BookOutputPort bookOutputPort;

    @Override
    public BookOutputDto getBookInfo(long bookNo) {
        Book loadBook = bookOutputPort.loadBook(bookNo);
        return BookOutputDto.mapToDTO(loadBook);
    }
}
