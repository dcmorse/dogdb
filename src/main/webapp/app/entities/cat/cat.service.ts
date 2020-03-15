import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { ICat } from 'app/shared/model/cat.model';

type EntityResponseType = HttpResponse<ICat>;
type EntityArrayResponseType = HttpResponse<ICat[]>;

@Injectable({ providedIn: 'root' })
export class CatService {
  public resourceUrl = SERVER_API_URL + 'api/cats';

  constructor(protected http: HttpClient) {}

  create(cat: ICat): Observable<EntityResponseType> {
    return this.http.post<ICat>(this.resourceUrl, cat, { observe: 'response' });
  }

  update(cat: ICat): Observable<EntityResponseType> {
    return this.http.put<ICat>(this.resourceUrl, cat, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ICat>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICat[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
