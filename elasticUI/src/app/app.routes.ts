import { Routes, RouterModule } from '@angular/router';
import { QueryComponent } from './query/query.component';
import { Query2Component } from './query2/query2.component';
// tslint:disable-next-line:import-spacing
const routes: Routes = [
  { path: '',  redirectTo: '/admin', pathMatch: 'full' },
  {path: 'admin', component : QueryComponent},
  {path: 'user', component : Query2Component},

]
export const routing = RouterModule.forRoot(routes);
