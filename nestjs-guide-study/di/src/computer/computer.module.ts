import { Module } from '@nestjs/common';
import { ComputerController } from './computer.controller';
import { CpuModule } from '../cpu/cpu.module';
import { DiskModule } from '../disk/disk.module';

@Module({
  controllers: [ComputerController],
  imports: [CpuModule, DiskModule], // ComputerModule이 CpuModule와 DiskModule을 사용할 수 있게끔 설정 (각 모듈에서 export된 컴포넌트들을 DI 받을 수 있게 된다.)
})
export class ComputerModule {}
