import { NestFactory } from '@nestjs/core';
import { ValidationPipe } from '@nestjs/common';
import { AppModule } from './app.module';

async function bootstrap() {
  const app = await NestFactory.create(AppModule);
  app.useGlobalPipes(
    new ValidationPipe({
      whitelist: true, // 스펙에 없는 필드는 무시처리하는 설정
    })
  );
  await app.listen(process.env.PORT ?? 3001);
}
bootstrap();
