import { Controller, Get, Post, Body, Param } from '@nestjs/common';
import { CreateMessageDto } from './dtos/create-message.dto';

@Controller('messages')
export class MessagesController {
  @Get()
  listMessages() {}

  /**
   * [MARK]
   * TS는 컴파일된 후 모든 데코레이터와 타입정보가 소거된 JS로 변환된다. ( createMessage(@Body() body: CreateMessageDto) -> createMessage(body) )
   * 따라서 런타임 시점에 class-transformer 가 특정 DTO 타입을 어떻게 알아차리고 DTO 객체로 변환하는지에 대한 질문에 대한 답은 아래와 같다.
   *
   * tsconfig.json 에 `emitDecoratorMetadata` 와 `experimentalDecorators` 를 설정하면 js로 변환시 메타데이터가 함께 변환된다.
   * 실제로 js로 컴파일된 결과의 일부를 보면 아래와 같다.
   *
   * ```js
   * __decorate([
   *     (0, common_1.Post)(), // @Post 데코레이터 정보
   *     __param(0, (0, common_1.Body)()), //@Body 데코레이터 정보
   *     __metadata("design:type", Function),
   *     __metadata("design:paramtypes", [create_message_dto_1.CreateMessageDto]), //요청 DTO 타입 정보
   *     __metadata("design:returntype", void 0)
   * ], MessagesController.prototype, "createMessage", null);
   * ```
   */
  @Post()
  createMessage(@Body() body: CreateMessageDto) {
    console.log(body);
  }

  @Get('/:id')
  getMessage(@Param('id') id: string) {
    console.log(id);
  }
}
