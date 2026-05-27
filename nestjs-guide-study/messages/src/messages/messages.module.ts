import { Module } from '@nestjs/common';
import { MessagesController } from './messages.controller';
import { MessagesService } from './messages.service';
import { MessagesRepository } from './messages.repository';

@Module({
  controllers: [MessagesController],
  providers: [MessagesService, MessagesRepository], // 다른 컴포넌트에서 사용할 수 있는 컴포넌트들을 등록 (싱글톤으로 관리됨)
})
export class MessagesModule {}
