package com.msa.rental.application.usecase;

import com.msa.rental.framework.web.dto.RentalCardOutputDto;
import com.msa.rental.framework.web.dto.UserInputDto;

public interface CreateRentalCardUsecase {
    RentalCardOutputDto createRentalCard(UserInputDto userInputDto);
}
