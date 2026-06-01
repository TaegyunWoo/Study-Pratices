import { Module } from '@nestjs/common';
import { TypeOrmModule } from '@nestjs/typeorm';
import { UsersController } from './users.controller';
import { UsersService } from './users.service';
import { User } from './user.entity';

@Module({
  imports: [TypeOrmModule.forFeature([User])], // 해당 설정으로 인해 UserRepository 가 생성된다.
  controllers: [UsersController],
  providers: [UsersService]
})
export class UsersModule {}
