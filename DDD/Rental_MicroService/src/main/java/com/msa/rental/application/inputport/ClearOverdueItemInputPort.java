package com.msa.rental.application.inputport;

import com.msa.rental.application.outputport.EventOutputPort;
import com.msa.rental.application.outputport.RentalCardOutputPort;
import com.msa.rental.application.usecase.ClearOverdueItemUsecase;
import com.msa.rental.domain.model.RentalCard;
import com.msa.rental.domain.model.event.OverdueCleared;
import com.msa.rental.framework.web.dto.ClearOverdueInfoDto;
import com.msa.rental.framework.web.dto.RentalResultOutputDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ClearOverdueItemInputPort implements ClearOverdueItemUsecase {
    private final RentalCardOutputPort rentalCardOutputPort;
    private final EventOutputPort eventOutputPort;

    @Override
    public RentalResultOutputDto clearOverdue(ClearOverdueInfoDto clearOverdueInfoDto) throws Exception {
        RentalCard rentalCard = rentalCardOutputPort.loadRentalCard(clearOverdueInfoDto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("해당 카드가 존재하지 않습니다."));
        rentalCard.makeAvailableRental(clearOverdueInfoDto.getPoint());
        rentalCardOutputPort.save(rentalCard);

        //연체 해제 이벤트 생성 및 발행
        OverdueCleared overdueClearedEvent = RentalCard.createOverdueClearedEvent(rentalCard.getMember(), clearOverdueInfoDto.getPoint());
        eventOutputPort.occurOverdueClearedEvent(overdueClearedEvent);

        return RentalResultOutputDto.mapToDto(rentalCard);
    }
}
