import {NgModule} from '@angular/core';
import {BrowserModule, provideClientHydration} from '@angular/platform-browser';
import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {provideAnimationsAsync} from '@angular/platform-browser/animations/async';
import {MatSlideToggleModule} from '@angular/material/slide-toggle';
import {HttpClientModule} from '@angular/common/http';
import {MatSidenavModule} from '@angular/material/sidenav';
import {MatButtonModule} from '@angular/material/button';
import {MatToolbarModule} from '@angular/material/toolbar'
import {MatIconModule} from '@angular/material/icon';
import {MatDividerModule} from '@angular/material/divider';
import {MatListModule} from '@angular/material/list';
import {MatMenuModule} from '@angular/material/menu';
import {MatCardModule} from '@angular/material/card';
import {MatTabsModule} from '@angular/material/tabs';
import {MatCheckboxModule} from '@angular/material/checkbox';
import {MatSelectModule} from '@angular/material/select';
import {MatAutocompleteModule} from '@angular/material/autocomplete';
import {MatTableModule} from '@angular/material/table';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {MatFormFieldModule} from '@angular/material/form-field';
import {MatInputModule} from '@angular/material/input';
import {GlobalStateService} from './core/global-state-service.service';
import {NgxMaskDirective, provideNgxMask} from 'ngx-mask';
import {MatPaginatorModule} from '@angular/material/paginator';
import {MatDatepickerModule} from '@angular/material/datepicker';
import {MAT_DATE_LOCALE, provideNativeDateAdapter} from '@angular/material/core';
import {DatePipe} from '@angular/common';
import {MAT_SNACK_BAR_DATA, MatSnackBarModule} from '@angular/material/snack-bar';
import {SnackBarComponent} from './snack-bar/snack-bar.component';
import {MatProgressBarModule} from '@angular/material/progress-bar';
import {ProgressBarComponent} from './progress-bar/progress-bar.component';
import {MatDialogModule} from '@angular/material/dialog';
import {ListaTranscaoComponent} from './lista-transcao/lista-transcao.component';
import {ListaContasComponent} from './lista-contas/lista-contas.component';
import {DetalheContaComponent} from './detalhe-conta/detalhe-conta.component';
import {CadastroTransacaoComponent} from './cadastro-transacao/cadastro-transacao.component';
import {CadastroGrupoTransacaoComponent} from './cadastro-grupo-transacao/cadastro-grupo-transacao.component';
import {ListaGrupoTransacaoComponent} from './lista-grupo-transacao/lista-grupo-transacao.component';
import {ListaTiposTransferenciaComponent} from './lista-tipos-transferencia/lista-tipos-transferencia.component';
import {DetalheTiposTransferenciaComponent} from './detalhe-tipos-transferencia/detalhe-tipos-transferencia.component';
import {MatTooltipModule} from '@angular/material/tooltip';
import {StoreModule} from '@ngrx/store';
import {appReducer} from "./store/appReducer";
import {StoreDevtoolsModule} from '@ngrx/store-devtools';
import {EffectsModule} from '@ngrx/effects';
import {AppEffectService} from './store/app.effect.service';

@NgModule({
  declarations: [
    AppComponent,
    SnackBarComponent,
    ProgressBarComponent,
    ListaTranscaoComponent,
    ListaContasComponent,
    DetalheContaComponent,
    CadastroTransacaoComponent,
    CadastroGrupoTransacaoComponent,
    ListaGrupoTransacaoComponent,
    ListaTiposTransferenciaComponent,
    DetalheTiposTransferenciaComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    MatSlideToggleModule,
    HttpClientModule,
    MatSidenavModule,
    MatButtonModule,
    MatToolbarModule,
    MatIconModule,
    MatListModule,
    MatDividerModule,
    MatMenuModule,
    FormsModule,
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatCardModule,
    MatTabsModule,
    MatCheckboxModule,
    MatSelectModule,
    MatAutocompleteModule,
    NgxMaskDirective,
    MatTableModule,
    MatPaginatorModule,
    MatDatepickerModule,
    MatSnackBarModule,
    MatProgressBarModule,
    MatDialogModule,
    MatTooltipModule,
    StoreModule.forRoot({app: appReducer}, {}),
    StoreDevtoolsModule.instrument({
      maxAge: 25,
    }),
    EffectsModule.forRoot([AppEffectService])
  ],
  providers: [
    provideClientHydration(),
    provideAnimationsAsync(),
    GlobalStateService,
    provideNgxMask({ /* opções de cfg */ }),
    provideNativeDateAdapter(),
    { provide: MAT_DATE_LOCALE, useValue: 'en-GB' },
    { provide: MAT_SNACK_BAR_DATA, useValue: {} },
    DatePipe
  ],
  bootstrap: [AppComponent],
  schemas: [
    // CUSTOM_ELEMENTS_SCHEMA
  ],
})
export class AppModule { }
