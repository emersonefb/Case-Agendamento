import {HttpClient} from '@angular/common/http';
import {inject, Injectable} from '@angular/core';
import {Actions, createEffect, ofType} from '@ngrx/effects';
import {appActionSucesso, appTokenAtualizaAction, IAppStage} from './app.stage';
import {map, of, switchMap, withLatestFrom} from 'rxjs';
import {Store} from '@ngrx/store';
import {GlobalStateService} from '../core/global-state-service.service';

@Injectable({
  providedIn: 'root'
})
export class AppEffectService {

  private global= inject(GlobalStateService);
  constructor(private actions$: Actions,
    private store: Store<{ app: IAppStage}>,
              private http: HttpClient) { }

  carregaTodasActions = createEffect(
    () => this.actions$.pipe(
      ofType(appTokenAtualizaAction),
            withLatestFrom(
        this.store.select('app').pipe(
          map(app => app.tokenJWT )
        )
      ),
      switchMap(([action, payload]) => {
          return of(appActionSucesso());
        }
      )
    )
  )
}
