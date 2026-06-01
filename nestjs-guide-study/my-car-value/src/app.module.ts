import { Module } from '@nestjs/common';
import { TypeOrmModule } from '@nestjs/typeorm';
import { AppController } from './app.controller';
import { AppService } from './app.service';
import { UsersModule } from './users/users.module';
import { ReportsModule } from './reports/reports.module';

@Module({
  imports: [
    TypeOrmModule.forRoot({
      type: 'better-sqlite3', //DB 종류
      database: 'db.sqlite', //DB 명
      entities: [],
      autoLoadEntities: true,
      synchronize: true,
    }),
    UsersModule,
    ReportsModule
  ],
  controllers: [AppController],
  providers: [AppService],
})
export class AppModule {}
