import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { DogdbTestModule } from '../../../test.module';
import { DogsComponent } from 'app/entities/dogs/dogs.component';
import { DogsService } from 'app/entities/dogs/dogs.service';
import { Dogs } from 'app/shared/model/dogs.model';

describe('Component Tests', () => {
  describe('Dogs Management Component', () => {
    let comp: DogsComponent;
    let fixture: ComponentFixture<DogsComponent>;
    let service: DogsService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [DogdbTestModule],
        declarations: [DogsComponent]
      })
        .overrideTemplate(DogsComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(DogsComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(DogsService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new Dogs(123)],
            headers
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.dogs && comp.dogs[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
