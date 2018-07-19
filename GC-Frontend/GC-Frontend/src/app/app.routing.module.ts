import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {ProductListComponent} from './product-list/product-list.component';
import {PageNotFoundComponent} from './not-found.component';
import {AddInvoiceComponent} from './add-invoice/add-invoice.component';
import {DeadlinesComponent} from './deadlines/deadlines.component';
import {WorkersComponent} from './workers/workers.component';
import {SquadComponent} from './squad/squad.component';
import {VehiclesComponent} from './vehicles/vehicles.component';
import {ProfitsComponent} from './profits/profits.component';
import {BuildingsComponent} from './buildings/buildings.component';

const appRoutes: Routes = [
  {path: 'cantieri', component: BuildingsComponent},
  {path: 'aggiungi-fattura', component: AddInvoiceComponent},
  {path: 'lista-prodotti', component: ProductListComponent},
  {path: 'scadenze', component: DeadlinesComponent},
  {path: 'operai', component: WorkersComponent},
  {path: 'squadre', component: SquadComponent},
  {path: 'mezzi-attrezzature', component: VehiclesComponent},
  {path: 'utili', component: ProfitsComponent},
  {path: '', redirectTo: '/cantieri', pathMatch: 'full'},
  {path: '**', component: PageNotFoundComponent}
];

@NgModule({
  imports: [
    RouterModule.forRoot(
      appRoutes,
      {enableTracing: true} // <-- debugging purposes only
    )
  ],
  exports: [
    RouterModule
  ]
})
export class AppRoutingModule {
}
