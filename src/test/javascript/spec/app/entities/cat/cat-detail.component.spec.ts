import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { DogdbTestModule } from '../../../test.module';
import { CatDetailComponent } from 'app/entities/cat/cat-detail.component';
import { Cat } from 'app/shared/model/cat.model';

describe('Component Tests', () => {
  describe('Cat Management Detail Component', () => {
    let comp: CatDetailComponent;
    let fixture: ComponentFixture<CatDetailComponent>;
    const route = ({ data: of({ cat: new Cat(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [DogdbTestModule],
        declarations: [CatDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(CatDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(CatDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load cat on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.cat).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
