import { Module } from '@nestjs/common';
import { PowerService } from './power.service';

@Module({
  providers: [PowerService],
  exports: [PowerService], // 본 모듈을 import한 다른 모듈에서 사용할 수 있는 컴포넌트로 설정 (기본적으로 providers 로 설정한 컴포넌트들은 해당 모듈에서만 사용 가능)
})
export class PowerModule {}
