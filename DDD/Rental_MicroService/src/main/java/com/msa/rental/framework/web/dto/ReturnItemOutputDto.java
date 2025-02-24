package com.msa.rental.framework.web.dto;

import com.msa.rental.domain.model.vo.ReturnItem;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ReturnItemOutputDto {
    private Integer itemNo;
    private String itemtitle;
    private LocalDate returnDate;

    public static ReturnItemOutputDto mapToDTO(ReturnItem returnItem) {
        ReturnItemOutputDto rentItemOutputDTO = new ReturnItemOutputDto();
        rentItemOutputDTO.setItemNo(returnItem.getRentalItem().getItem().getNo());
        rentItemOutputDTO.setItemtitle(returnItem.getRentalItem().getItem().getTitle());
        rentItemOutputDTO.setReturnDate(returnItem.getReturnDate());
        return rentItemOutputDTO;
    }
}
