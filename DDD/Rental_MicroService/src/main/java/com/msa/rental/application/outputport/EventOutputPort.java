package com.msa.rental.application.outputport;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.msa.rental.domain.model.event.ItemRented;
import com.msa.rental.domain.model.event.ItemReturned;
import com.msa.rental.domain.model.event.OverdueCleared;

public interface EventOutputPort {
    void occurRentalEvent(ItemRented itemRented) throws JsonProcessingException;
    void occurReturnEvent(ItemReturned itemReturned) throws JsonProcessingException;
    void occurOverdueClearedEvent(OverdueCleared overdueCleared) throws JsonProcessingException;
}
