import { MessagesRepository } from './messages.repository';

export class MessagesService {
  messagesRepo: MessagesRepository;

  constructor() {
    /**
     * TODO - Refactoring
     * 의존성 주입을 사용하지 않고 명시적으로 생성. 실제론 NestJS에서 제공되는 의존성 주입 기능을 사용한다. 즉 아래 코드는 임시 코드이다.
     */
    this.messagesRepo = new MessagesRepository();
  }

  findOne(id: string) {
    return this.messagesRepo.findOne(id);
  }

  findAll() {
    return this.messagesRepo.findAll();
  }

  create(content: string) {
    return this.messagesRepo.create(content);
  }
}