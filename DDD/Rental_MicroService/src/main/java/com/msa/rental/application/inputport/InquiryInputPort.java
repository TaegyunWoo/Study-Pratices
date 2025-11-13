package com.msa.rental.application.inputport;

import com.msa.rental.application.outputport.RentalCardOutputPort;
import com.msa.rental.application.usecase.InquiryUsecase;
import com.msa.rental.framework.web.dto.RentItemOutputDto;
import com.msa.rental.framework.web.dto.RentalCardOutputDto;
import com.msa.rental.framework.web.dto.ReturnItemOutputDto;
import com.msa.rental.framework.web.dto.UserInputDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class InquiryInputPort implements InquiryUsecase {
    private final RentalCardOutputPort rentalCardOutputPort;

    @Override
    public Optional<RentalCardOutputDto> getRentalCard(UserInputDto userInputDTO) {
        return rentalCardOutputPort.loadRentalCard(userInputDTO.getUserId())
                .map(RentalCardOutputDto::mapToDto);
    }

    @Override
    public Optional<List<RentItemOutputDto>> getAllRentItem(UserInputDto userInputDto) {
        return rentalCardOutputPort.loadRentalCard(userInputDto.getUserId())
                .map(loadCard -> loadCard.getRentalItemList()
                        .stream()
                        .map(RentItemOutputDto::mapToDTO)
                        .collect(Collectors.toList()));
    }

    @Override
    public Optional<List<ReturnItemOutputDto>> getAllReturnItem(UserInputDto userInputDto) {
        return rentalCardOutputPort.loadRentalCard(userInputDto.getUserId())
                .map(loadCard -> loadCard.getReturnItemList()
                        .stream()
                        .map(ReturnItemOutputDto::mapToDTO)
                        .collect(Collectors.toList()));
    }
}
