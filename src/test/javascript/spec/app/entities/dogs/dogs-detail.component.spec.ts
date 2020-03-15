import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { DogdbTestModule } from '../../../test.module';
import { DogsDetailComponent } from 'app/entities/dogs/dogs-detail.component';
import { Dogs } from 'app/shared/model/dogs.model';

describe('Component Tests', () => {
  describe('Dogs Management Detail Component', () => {
    let comp: DogsDetailComponent;
    let fixture: ComponentFixture<DogsDetailComponent>;
    const route = ({ data: of({ dogs: new Dogs(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [DogdbTestModule],
        declarations: [DogsDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(DogsDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(DogsDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load dogs on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.dogs).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
