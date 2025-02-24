package com.msa.rental.application.usecase;

import com.msa.rental.framework.web.dto.RentalCardOutputDto;
import com.msa.rental.framework.web.dto.UserItemInputDto;

public interface RentItemUsecase {
    RentalCardOutputDto rentItem(UserItemInputDto userItemInputDto) throws Exception;
}
