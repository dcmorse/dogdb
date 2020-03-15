import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IDogs } from 'app/shared/model/dogs.model';

type EntityResponseType = HttpResponse<IDogs>;
type EntityArrayResponseType = HttpResponse<IDogs[]>;

@Injectable({ providedIn: 'root' })
export class DogsService {
  public resourceUrl = SERVER_API_URL + 'api/dogs';

  constructor(protected http: HttpClient) {}

  create(dogs: IDogs): Observable<EntityResponseType> {
    return this.http.post<IDogs>(this.resourceUrl, dogs, { observe: 'response' });
  }

  update(dogs: IDogs): Observable<EntityResponseType> {
    return this.http.put<IDogs>(this.resourceUrl, dogs, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IDogs>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IDogs[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
