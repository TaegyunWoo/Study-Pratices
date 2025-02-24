package com.msa.rental.application.outputport;

import com.msa.rental.domain.model.RentalCard;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 해당 인터페이스를 framework 패키지(외부 헥사곤)에서 구현
 */
@Repository
public interface RentalCardOutputPort {
    Optional<RentalCard> loadRentalCard(String userid);

    RentalCard save(RentalCard rentalCard);
}
