import { Injectable, NotFoundException } from '@nestjs/common';
import { Repository } from 'typeorm';
import { InjectRepository } from '@nestjs/typeorm';
import { User } from './user.entity';

@Injectable()
export class UsersService {
  constructor(
    @InjectRepository(User)
    private readonly usersRepository: Repository<User>,
  ) {}

  create(email: string, password: string) {
    const user = this.usersRepository.create({ email, password }); // 영속화되지 않은 엔티티 객체 생성
    return this.usersRepository.save(user); //엔티티 영속화 및 DB 저장
  }

  findOne(id: number) {
    return this.usersRepository.findOneBy({ id });
  }

  find(email: string) {
    return this.usersRepository.find({ where: { email } });
  }

  /**
   * Partial<User> : User 엔티티의 모든 필드를 선택적으로 업데이트할 수 있도록 함
   * Partial<T> ? : TS 자체 타입으로, T 타입의 일부 혹은 모든 필드가 없는 객체라도 T 타입으로 인정하는 타입
   * @param id
   * @param attrs
   */
  async update(id: number, attrs: Partial<User>) {
    const user = await this.findOne(id);
    if (!user) {
      throw new NotFoundException('user not found');
    }
    Object.assign(user, attrs); //attrs 객체의 필드들을 가져와 user 객체의 필드에 직접 붙여넣는다.
    return this.usersRepository.save(user);
  }

  async remove(id: number) {
    const user = await this.findOne(id);
    if (!user) {
      throw new NotFoundException('user not found');
    }
    return this.usersRepository.remove(user);
  }
}
