package com.msa.rental.framework.web.dto;

import com.msa.rental.domain.model.RentalCard;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class RentalCardOutputDto {
    private String rentalCardId;
    private String memberId;
    private String memberName;
    private String rentStatus;
    private Long totalLateFee;
    private Long totalRentalCnt;
    private Long totalReturnCnt;
    private Long totalOverduedCnt;

    public static RentalCardOutputDto mapToDto(RentalCard rentalCard) {
        RentalCardOutputDto rentalCardOutputDto = new RentalCardOutputDto();
        rentalCardOutputDto.setRentalCardId(rentalCard.getRentalCardNo().getNo().toString());
        rentalCardOutputDto.setMemberId(rentalCard.getMember().getId().toString());
        rentalCardOutputDto.setMemberName(rentalCard.getMember().getName());
        rentalCardOutputDto.setRentStatus(rentalCard.getRentStatus().toString());
        rentalCardOutputDto.setTotalLateFee(rentalCard.getLateFee().getPoint());
        rentalCardOutputDto.setTotalRentalCnt(rentalCard.getRentalItemList().stream().count());
        rentalCardOutputDto.setTotalReturnCnt(rentalCard.getReturnItemList().stream().count());
        rentalCardOutputDto.setTotalOverduedCnt(
                rentalCard.getRentalItemList().stream()
                        .filter(i -> i.isOverdued())
                        .count()
        );
        return rentalCardOutputDto;
    }
}
