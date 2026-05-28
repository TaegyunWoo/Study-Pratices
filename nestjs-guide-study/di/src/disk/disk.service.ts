import { Injectable } from '@nestjs/common';
import { PowerService } from '../power/power.service';

@Injectable()
export class DiskService {
  constructor(private readonly powerService: PowerService) {}

  getData() {
    console.log('Getting data from disk');
    this.powerService.supplyPower(10);
    return 'Data from disk';
  }
}
