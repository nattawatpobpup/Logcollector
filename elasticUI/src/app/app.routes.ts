import { Routes, RouterModule } from '@angular/router';
import { QueryComponent } from './query/query.component';
// tslint:disable-next-line:import-spacing
const routes: Routes = [
  { path: '',  redirectTo: '/query', pathMatch: 'full' },
  {path: 'query', component : QueryComponent}
  ]
export const routing = RouterModule.forRoot(routes);
