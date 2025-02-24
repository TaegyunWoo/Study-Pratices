package com.msa.book.application.usecase;

import com.msa.book.framework.web.dto.BookOutputDto;

public interface MakeAvailableUsecase {
    BookOutputDto available(Long bookNo);
}
