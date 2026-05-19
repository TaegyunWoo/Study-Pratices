import {Controller, Module, Get} from '@nestjs/common';
import { NestFactory } from "@nestjs/core";

@Controller()
class AppController {
    @Get()
    getRootRoute() {
        return 'Hello World!';
    }
}

/**
 * NestJS가 실행될때, Module 데코레이터가 붙은 클래스를 보고, 속성으로 전달된 모든 컨트롤러를 인스턴스로 생성한다.
 */
@Module({
    controllers: [AppController]
})
class AppModule {

}

async function bootstrap() {
    const app = await NestFactory.create(AppModule); //AppModule을 생성해라.

    await app.listen(3001); // 3001번 포트로 요청을 받는다.
}

bootstrap();