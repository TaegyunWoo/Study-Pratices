package com.msa.rental.framework.web.controller;

import com.msa.rental.application.usecase.*;
import com.msa.rental.framework.web.dto.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Api(tags = {"대여 Controller"})
public class RentalController {
    private final RentItemUsecase rentItemUsecase;
    private final ReturnItemUsecase returnItemUsecase;
    private final OverdueItemUsecase overdueItemUsecase;
    private final CreateRentalCardUsecase createRentalCardUsecase;
    private final InquiryUsecase inquiryUsecase;
    private final ClearOverdueItemUsecase clearOverdueItemUsecase;

    @ApiOperation(value = "도서카드 생성",notes = "사용자정보 -> 도서카드정보")
    @PostMapping("/RentalCard/")
    public ResponseEntity<RentalCardOutputDto> createRentalCard(@RequestBody UserInputDto userInputDto) {
        RentalCardOutputDto createdRentalCard = createRentalCardUsecase.createRentalCard(userInputDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdRentalCard);
    }

    @ApiOperation(value = "도서카드 조회",notes = "사용자정보(id) -> 도서카드정보")
    @GetMapping("/RentalCard/{id}")
    public ResponseEntity<RentalCardOutputDto> getRentalCard(@PathVariable String id) {
        Optional<RentalCardOutputDto> rentalCard = inquiryUsecase.getRentalCard(new UserInputDto(id, ""));
        return ResponseEntity.ok(rentalCard.get());
    }

    @ApiOperation(value = "대여도서목록 조회",notes = "사용자정보(id) -> 대여도서목록 조회")
    @GetMapping("/RentalCard/{id}/rentbook")
    public ResponseEntity<List<RentItemOutputDto>> getAllRentItem(@PathVariable String id) {
        Optional<List<RentItemOutputDto>> allRentItem = inquiryUsecase.getAllRentItem(new UserInputDto(id, ""));
        return ResponseEntity.ok(allRentItem.get());
    }

    @ApiOperation(value = "반납도서목록 조회",notes = "사용자정보(id) -> 반납도서목록 조회")
    @GetMapping("/RentalCard/{id}/returnbook")
    public ResponseEntity<List<ReturnItemOutputDto>> getAllReturnItem(@PathVariable String id) {
        Optional<List<ReturnItemOutputDto>> allReturnItem = inquiryUsecase.getAllReturnItem(new UserInputDto(id, ""));
        return ResponseEntity.ok(allReturnItem.get());
    }

    @ApiOperation(value = "대여기능",notes = "사용자정보,아이템정보1 -> 도서카드정보 ")
    @PostMapping("/RentalCard/rent")
    public ResponseEntity<RentalCardOutputDto> rentItem(@RequestBody UserItemInputDto userItemInputDTO) throws Exception {
        RentalCardOutputDto rentalCardOutputDto = rentItemUsecase.rentItem(userItemInputDTO);
        return ResponseEntity.ok(rentalCardOutputDto);
    }

    @ApiOperation(value = "반납기능",notes = "사용자정보,아이템정보1 -> 도서카드정보 ")
    @PostMapping("/RentalCard/return")
    public ResponseEntity<RentalCardOutputDto> returnItem(@RequestBody UserItemInputDto userItemInputDTO) throws Exception {
        RentalCardOutputDto rentalCardOutputDto = returnItemUsecase.returnItem(userItemInputDTO);
        return ResponseEntity.ok(rentalCardOutputDto);
    }

    @ApiOperation(value = "연체기능",notes = "사용자정보,아이템정보1 -> 도서카드정보 ")
    @PostMapping("/RentalCard/overdue")public ResponseEntity<RentalCardOutputDto> overdueItem(@RequestBody UserItemInputDto userItemInputDTO) throws Exception {
        RentalCardOutputDto rentalCardOutputDto = overdueItemUsecase.overdueItem(userItemInputDTO);
        return ResponseEntity.ok(rentalCardOutputDto);
    }

    @ApiOperation(value = "연체해제기능",notes = "사용자정보,포인트 -> 도서카드정보 ")
    @PostMapping("/RentalCard/clearoverdue")
    public ResponseEntity<RentalResultOutputDto> clearOverdueItem(@RequestBody ClearOverdueInfoDto clearOverdueInfoDTO) throws Exception {
        RentalResultOutputDto rentalResultOutputDto = clearOverdueItemUsecase.clearOverdue(clearOverdueInfoDTO);
        return ResponseEntity.ok(rentalResultOutputDto);
    }
}
