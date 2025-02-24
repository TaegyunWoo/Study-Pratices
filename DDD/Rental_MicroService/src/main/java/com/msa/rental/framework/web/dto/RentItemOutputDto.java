package com.msa.rental.framework.web.dto;

import com.msa.rental.domain.model.vo.RentalItem;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class RentItemOutputDto {
    private Integer itemNo;
    private String itemtitle;
    private LocalDate rentDate; private boolean overdued; //반납 예정일
    private LocalDate overdueDate;

    public static RentItemOutputDto mapToDTO(RentalItem rentItem) {
        RentItemOutputDto rentItemOutputDTO = new RentItemOutputDto();
        rentItemOutputDTO.setItemNo(rentItem.getItem().getNo());
        rentItemOutputDTO.setItemtitle(rentItem.getItem().getTitle());
        rentItemOutputDTO.setRentDate(rentItem.getRentDate());
        rentItemOutputDTO.setOverdued(rentItem.isOverdued());
        rentItemOutputDTO.setOverdueDate(rentItem.getOverdueDate());
        return rentItemOutputDTO;
    }
}
