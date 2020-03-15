import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IDogs, Dogs } from 'app/shared/model/dogs.model';
import { DogsService } from './dogs.service';

@Component({
  selector: 'jhi-dogs-update',
  templateUrl: './dogs-update.component.html'
})
export class DogsUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required, Validators.minLength(2), Validators.maxLength(64)]]
  });

  constructor(protected dogsService: DogsService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ dogs }) => {
      this.updateForm(dogs);
    });
  }

  updateForm(dogs: IDogs): void {
    this.editForm.patchValue({
      id: dogs.id,
      name: dogs.name
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const dogs = this.createFromForm();
    if (dogs.id !== undefined) {
      this.subscribeToSaveResponse(this.dogsService.update(dogs));
    } else {
      this.subscribeToSaveResponse(this.dogsService.create(dogs));
    }
  }

  private createFromForm(): IDogs {
    return {
      ...new Dogs(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDogs>>): void {
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
}
