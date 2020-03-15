import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { DogdbTestModule } from '../../../test.module';
import { CatComponent } from 'app/entities/cat/cat.component';
import { CatService } from 'app/entities/cat/cat.service';
import { Cat } from 'app/shared/model/cat.model';

describe('Component Tests', () => {
  describe('Cat Management Component', () => {
    let comp: CatComponent;
    let fixture: ComponentFixture<CatComponent>;
    let service: CatService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [DogdbTestModule],
        declarations: [CatComponent]
      })
        .overrideTemplate(CatComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(CatComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(CatService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new Cat(123)],
            headers
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.cats && comp.cats[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
