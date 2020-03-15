import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IDogs } from 'app/shared/model/dogs.model';
import { DogsService } from './dogs.service';

@Component({
  templateUrl: './dogs-delete-dialog.component.html'
})
export class DogsDeleteDialogComponent {
  dogs?: IDogs;

  constructor(protected dogsService: DogsService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.dogsService.delete(id).subscribe(() => {
      this.eventManager.broadcast('dogsListModification');
      this.activeModal.close();
    });
  }
}
