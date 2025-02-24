package com.msa.book.framework.web;

import com.msa.book.application.usecase.AddBookUsecase;
import com.msa.book.application.usecase.InquiryUsecase;
import com.msa.book.framework.web.dto.BookInfoDto;
import com.msa.book.framework.web.dto.BookOutputDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class BookController {
    private final AddBookUsecase addBookUsecase;
    private final InquiryUsecase inquiryUsecase;

    @PostMapping("/book")
    public ResponseEntity<BookOutputDto> createBook(@RequestBody BookInfoDto bookInfoDto) {
        BookOutputDto bookOutputDto = addBookUsecase.addBook(bookInfoDto);
        return ResponseEntity.ok(bookOutputDto);
    }

    @GetMapping("/book/{bookNo}")
    public ResponseEntity<BookOutputDto> getBookInfo(@PathVariable long bookNo) {
        BookOutputDto bookOutputDto = inquiryUsecase.getBookInfo(bookNo);
        return bookOutputDto != null ? ResponseEntity.ok(bookOutputDto) : ResponseEntity.notFound().build();
    }
}
