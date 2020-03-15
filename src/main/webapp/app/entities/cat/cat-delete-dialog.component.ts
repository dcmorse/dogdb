import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ICat } from 'app/shared/model/cat.model';
import { CatService } from './cat.service';

@Component({
  templateUrl: './cat-delete-dialog.component.html'
})
export class CatDeleteDialogComponent {
  cat?: ICat;

  constructor(protected catService: CatService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.catService.delete(id).subscribe(() => {
      this.eventManager.broadcast('catListModification');
      this.activeModal.close();
    });
  }
}
