import {IsEmail, IsString, IsOptional} from 'class-validator';

export class UpdateUserDto {
  @IsEmail()
  @IsOptional() // 해당 필드가 비어있다면 다른 모든 validation을 무시한다.
  email: string;

  @IsString()
  @IsOptional() // 해당 필드가 비어있다면 다른 모든 validation을 무시한다.
  password: string;
}