package com.msa.rental.application.usecase;

import com.msa.rental.framework.web.dto.RentalResultOutputDto;
import com.msa.rental.framework.web.dto.ClearOverdueInfoDto;

public interface ClearOverdueItemUsecase {
    RentalResultOutputDto clearOverdue(ClearOverdueInfoDto clearOverdueInfoDto) throws Exception;
}
