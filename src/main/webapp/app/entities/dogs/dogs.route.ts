import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IDogs, Dogs } from 'app/shared/model/dogs.model';
import { DogsService } from './dogs.service';
import { DogsComponent } from './dogs.component';
import { DogsDetailComponent } from './dogs-detail.component';
import { DogsUpdateComponent } from './dogs-update.component';

@Injectable({ providedIn: 'root' })
export class DogsResolve implements Resolve<IDogs> {
  constructor(private service: DogsService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IDogs> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((dogs: HttpResponse<Dogs>) => {
          if (dogs.body) {
            return of(dogs.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Dogs());
  }
}

export const dogsRoute: Routes = [
  {
    path: '',
    component: DogsComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Dogs'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: DogsDetailComponent,
    resolve: {
      dogs: DogsResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Dogs'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: DogsUpdateComponent,
    resolve: {
      dogs: DogsResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Dogs'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: DogsUpdateComponent,
    resolve: {
      dogs: DogsResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Dogs'
    },
    canActivate: [UserRouteAccessService]
  }
];
