package com.msa.book.framework.jpaadapter;

import com.msa.book.application.outputport.BookOutputPort;
import com.msa.book.domain.model.Book;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class BookAdapter implements BookOutputPort {
    private final BookJpaRepository bookJpaRepository;

    @Override
    public Book loadBook(long bookNo) {
        return bookJpaRepository.findById(bookNo).orElse(null);
    }

    @Override
    public Book save(Book book) {
        return bookJpaRepository.save(book);
    }
}
