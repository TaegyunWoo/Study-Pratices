package com.msa.rental.framework.web.dto;

import com.msa.rental.domain.model.RentalCard;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RentalResultOutputDto {
    public String userId;
    public String userNm;
    public Integer rentedCount;
    public Long totalLateFee;

    public static RentalResultOutputDto mapToDto(RentalCard rentalCard) {
        RentalResultOutputDto rentalResultOutputDto = new RentalResultOutputDto();
        rentalResultOutputDto.setUserId(rentalCard.getMember().getId());
        rentalResultOutputDto.setUserNm(rentalCard.getMember().getName());
        rentalResultOutputDto.setRentedCount(rentalCard.getRentalItemList().size());
        rentalResultOutputDto.setTotalLateFee(rentalCard.getLateFee().getPoint());
        return rentalResultOutputDto;
    }
}
