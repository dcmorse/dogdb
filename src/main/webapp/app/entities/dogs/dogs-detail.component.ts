import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IDogs } from 'app/shared/model/dogs.model';

@Component({
  selector: 'jhi-dogs-detail',
  templateUrl: './dogs-detail.component.html'
})
export class DogsDetailComponent implements OnInit {
  dogs: IDogs | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ dogs }) => (this.dogs = dogs));
  }

  previousState(): void {
    window.history.back();
  }
}
