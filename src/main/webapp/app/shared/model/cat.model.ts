import { IUser } from 'app/core/user/user.model';

export interface ICat {
  id?: number;
  name?: string;
  user?: IUser;
}

export class Cat implements ICat {
  constructor(public id?: number, public name?: string, public user?: IUser) {}
}
