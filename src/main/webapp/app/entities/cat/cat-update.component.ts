import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { ICat, Cat } from 'app/shared/model/cat.model';
import { CatService } from './cat.service';
import { IUser } from 'app/core/user/user.model';
import { UserService } from 'app/core/user/user.service';

@Component({
  selector: 'jhi-cat-update',
  templateUrl: './cat-update.component.html'
})
export class CatUpdateComponent implements OnInit {
  isSaving = false;
  users: IUser[] = [];

  editForm = this.fb.group({
    id: [],
    name: [],
    user: []
  });

  constructor(
    protected catService: CatService,
    protected userService: UserService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ cat }) => {
      this.updateForm(cat);

      this.userService.query().subscribe((res: HttpResponse<IUser[]>) => (this.users = res.body || []));
    });
  }

  updateForm(cat: ICat): void {
    this.editForm.patchValue({
      id: cat.id,
      name: cat.name,
      user: cat.user
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const cat = this.createFromForm();
    if (cat.id !== undefined) {
      this.subscribeToSaveResponse(this.catService.update(cat));
    } else {
      this.subscribeToSaveResponse(this.catService.create(cat));
    }
  }

  private createFromForm(): ICat {
    return {
      ...new Cat(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      user: this.editForm.get(['user'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICat>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }

  trackById(index: number, item: IUser): any {
    return item.id;
  }
}
