package com.msa.book.application.usecase;

import com.msa.book.framework.web.dto.BookOutputDto;

public interface MakeUnavailableUsecase {
    BookOutputDto unavailable(long bookNo);
}
