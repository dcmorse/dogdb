export interface IDogs {
  id?: number;
  name?: string;
}

export class Dogs implements IDogs {
  constructor(public id?: number, public name?: string) {}
}
