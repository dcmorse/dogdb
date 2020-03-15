import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { DogdbSharedModule } from 'app/shared/shared.module';
import { CatComponent } from './cat.component';
import { CatDetailComponent } from './cat-detail.component';
import { CatUpdateComponent } from './cat-update.component';
import { CatDeleteDialogComponent } from './cat-delete-dialog.component';
import { catRoute } from './cat.route';

@NgModule({
  imports: [DogdbSharedModule, RouterModule.forChild(catRoute)],
  declarations: [CatComponent, CatDetailComponent, CatUpdateComponent, CatDeleteDialogComponent],
  entryComponents: [CatDeleteDialogComponent]
})
export class DogdbCatModule {}
