import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { ICat, Cat } from 'app/shared/model/cat.model';
import { CatService } from './cat.service';
import { CatComponent } from './cat.component';
import { CatDetailComponent } from './cat-detail.component';
import { CatUpdateComponent } from './cat-update.component';

@Injectable({ providedIn: 'root' })
export class CatResolve implements Resolve<ICat> {
  constructor(private service: CatService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICat> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((cat: HttpResponse<Cat>) => {
          if (cat.body) {
            return of(cat.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Cat());
  }
}

export const catRoute: Routes = [
  {
    path: '',
    component: CatComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Cats'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: CatDetailComponent,
    resolve: {
      cat: CatResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Cats'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: CatUpdateComponent,
    resolve: {
      cat: CatResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Cats'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: CatUpdateComponent,
    resolve: {
      cat: CatResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Cats'
    },
    canActivate: [UserRouteAccessService]
  }
];
