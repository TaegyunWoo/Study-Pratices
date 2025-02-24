package com.msa.rental.application.inputport;

import com.msa.rental.application.outputport.EventOutputPort;
import com.msa.rental.application.outputport.RentalCardOutputPort;
import com.msa.rental.application.usecase.RentItemUsecase;
import com.msa.rental.domain.model.RentalCard;
import com.msa.rental.domain.model.event.ItemRented;
import com.msa.rental.domain.model.vo.IDName;
import com.msa.rental.domain.model.vo.Item;
import com.msa.rental.framework.web.dto.RentalCardOutputDto;
import com.msa.rental.framework.web.dto.UserItemInputDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class RentItemInputPort implements RentItemUsecase {

    private final RentalCardOutputPort rentalCardOutputPort;
    private final EventOutputPort eventOutputPort;

    @Override
    public RentalCardOutputDto rentItem(UserItemInputDto rental) throws Exception {
        RentalCard rentalCard = rentalCardOutputPort.loadRentalCard(rental.getUserId())
                .orElseGet(
                        () -> RentalCard.createRentalCard(new IDName(rental.getUserId(), rental.getUserNm()))
                );
        Item newItem = new Item(rental.getItemId(), rental.getItemTitle());
        rentalCard.rentItem(newItem);
        rentalCardOutputPort.save(rentalCard);

        //대여 이벤트 생성 및 발행
        ItemRented itemRentedEvent = RentalCard.createItemRentedEvent(rentalCard.getMember(), newItem, 10L);
        eventOutputPort.occurRentalEvent(itemRentedEvent);

        return RentalCardOutputDto.mapToDto(rentalCard);
    }
}
