import { NestFactory } from "@nestjs/core";
import {AppModule} from "./app.module";

async function bootstrap() {
    const app = await NestFactory.create(AppModule); //AppModule을 생성해라.

    await app.listen(3001); // 3001번 포트로 요청을 받는다.
}

bootstrap();