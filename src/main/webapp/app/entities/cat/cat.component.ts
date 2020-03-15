import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ICat } from 'app/shared/model/cat.model';
import { CatService } from './cat.service';
import { CatDeleteDialogComponent } from './cat-delete-dialog.component';

@Component({
  selector: 'jhi-cat',
  templateUrl: './cat.component.html'
})
export class CatComponent implements OnInit, OnDestroy {
  cats?: ICat[];
  eventSubscriber?: Subscription;

  constructor(protected catService: CatService, protected eventManager: JhiEventManager, protected modalService: NgbModal) {}

  loadAll(): void {
    this.catService.query().subscribe((res: HttpResponse<ICat[]>) => (this.cats = res.body || []));
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInCats();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: ICat): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInCats(): void {
    this.eventSubscriber = this.eventManager.subscribe('catListModification', () => this.loadAll());
  }

  delete(cat: ICat): void {
    const modalRef = this.modalService.open(CatDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.cat = cat;
  }
}
