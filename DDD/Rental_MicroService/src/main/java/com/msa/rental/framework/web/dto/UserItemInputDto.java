package com.msa.rental.framework.web.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserItemInputDto {
    public String userId;
    public String userNm;
    public Integer itemId;
    public String itemTitle;
}
