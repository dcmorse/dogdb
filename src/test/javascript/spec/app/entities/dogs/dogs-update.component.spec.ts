import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { DogdbTestModule } from '../../../test.module';
import { DogsUpdateComponent } from 'app/entities/dogs/dogs-update.component';
import { DogsService } from 'app/entities/dogs/dogs.service';
import { Dogs } from 'app/shared/model/dogs.model';

describe('Component Tests', () => {
  describe('Dogs Management Update Component', () => {
    let comp: DogsUpdateComponent;
    let fixture: ComponentFixture<DogsUpdateComponent>;
    let service: DogsService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [DogdbTestModule],
        declarations: [DogsUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(DogsUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(DogsUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(DogsService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Dogs(123);
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new Dogs();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});
