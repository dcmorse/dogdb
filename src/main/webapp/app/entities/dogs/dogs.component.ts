import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IDogs } from 'app/shared/model/dogs.model';
import { DogsService } from './dogs.service';
import { DogsDeleteDialogComponent } from './dogs-delete-dialog.component';

@Component({
  selector: 'jhi-dogs',
  templateUrl: './dogs.component.html'
})
export class DogsComponent implements OnInit, OnDestroy {
  dogs?: IDogs[];
  eventSubscriber?: Subscription;

  constructor(protected dogsService: DogsService, protected eventManager: JhiEventManager, protected modalService: NgbModal) {}

  loadAll(): void {
    this.dogsService.query().subscribe((res: HttpResponse<IDogs[]>) => (this.dogs = res.body || []));
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInDogs();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IDogs): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInDogs(): void {
    this.eventSubscriber = this.eventManager.subscribe('dogsListModification', () => this.loadAll());
  }

  delete(dogs: IDogs): void {
    const modalRef = this.modalService.open(DogsDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.dogs = dogs;
  }
}
