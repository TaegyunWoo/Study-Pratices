package com.msa.rental.application.usecase;

import com.msa.rental.framework.web.dto.RentItemOutputDto;
import com.msa.rental.framework.web.dto.RentalCardOutputDto;
import com.msa.rental.framework.web.dto.ReturnItemOutputDto;
import com.msa.rental.framework.web.dto.UserInputDto;

import java.util.List;
import java.util.Optional;

public interface InquiryUsecase {
    Optional<RentalCardOutputDto> getRentalCard(UserInputDto userInputDTO);
    Optional<List<RentItemOutputDto>> getAllRentItem(UserInputDto userInputDto);
    Optional<List<ReturnItemOutputDto>> getAllReturnItem(UserInputDto userInputDto);
}
