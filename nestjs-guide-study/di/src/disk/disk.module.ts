import { Module } from '@nestjs/common';
import { DiskService } from './disk.service';
import { PowerModule } from '../power/power.module';

@Module({
  providers: [DiskService],
  imports: [PowerModule], // DiskModule이 PowerModule을 사용할 수 있게끔 설정 (PowerModule에서 export된 컴포넌트들을 DI 받을 수 있게 된다.)
  exports: [DiskService], // DiskModule을 import한 다른 모듈에서 사용할 수 있게끔 설정 (기본적으로 providers 로 설정한 컴포넌트들은 해당 모듈에서만 사용 가능)
})
export class DiskModule {}
