import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { DogdbSharedModule } from 'app/shared/shared.module';
import { DogsComponent } from './dogs.component';
import { DogsDetailComponent } from './dogs-detail.component';
import { DogsUpdateComponent } from './dogs-update.component';
import { DogsDeleteDialogComponent } from './dogs-delete-dialog.component';
import { dogsRoute } from './dogs.route';

@NgModule({
  imports: [DogdbSharedModule, RouterModule.forChild(dogsRoute)],
  declarations: [DogsComponent, DogsDetailComponent, DogsUpdateComponent, DogsDeleteDialogComponent],
  entryComponents: [DogsDeleteDialogComponent]
})
export class DogdbDogsModule {}
