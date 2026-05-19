import {Module} from "@nestjs/common";
import {AppController} from "./app.controller";


/**
 * NestJS가 실행될때, Module 데코레이터가 붙은 클래스를 보고, 속성으로 전달된 모든 컨트롤러를 인스턴스로 생성한다.
 */
@Module({
    controllers: [AppController]
})
export class AppModule { }