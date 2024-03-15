import {HttpClient, HttpErrorResponse, HttpHeaders} from '@angular/common/http';
import {inject, Injectable} from '@angular/core';
import {catchError, map, Observable, throwError} from 'rxjs';
import {environment} from '../../environments/environment';
import {MatSnackBar} from '@angular/material/snack-bar';
import {Store} from '@ngrx/store';
import {appTokenAtualizaAction, appTokenErroAction, IAppStage} from '../store/app.stage';

@Injectable({
  providedIn: 'root'
})
export class JsonApiService {


  constructor(private stage :Store<{ app: IAppStage}>){

  }

  ttp= ''
  headers = new HttpHeaders()
                            .append('Content-Type', 'application/json; charset=utf-8')
                            .append('Access-Control-Allow-Origin', ' *')
                            .append('Access-Control-Allow-Methods', ' GET, POST, PATCH, PUT, DELETE, OPTIONS')
                            .append("Access-Control-Allow-Headers", "Authorization, X-PINGOTHER, X-Auth-Token', Origin, X-Requested-With, Content-Type, Accept, X-Custom-header")
                            .append('tokenJWT', '123X-Auth-Token');

  private http = inject(HttpClient);
  private _snackBar= inject(MatSnackBar);

  tokenJWT$ = this.stage.select('app').pipe(
    map(app => this.ttp = app.tokenJWT)
  )

  public addHeader(headerName: string, headerValue: string) {
    this.headers.delete(headerName);
    this.headers.append(headerName, headerValue);
  }

  public fetchFromServiceWithGet(url: string): Observable<any> {
    this.headers = new HttpHeaders();
    return this.http.get(environment.API_URL_SERVICES + url,
                        { headers : this.headers,observe: "response" }).pipe(
      map((res) => this.extractData( res )),
      catchError(this.handleError), );
  }

  public fetchFromServiceWithPost(url: string, postData: any): Observable<any> {
    this.headers = new HttpHeaders();
    this.headers.append('Content-Type', 'application/json');

    return this.http.post(environment.API_URL_SERVICES + url, postData,
                         { headers : this.headers,observe: "response" }).pipe(
      map((res) => this.extractData( res )),
      catchError(this.handleError),
       );
  }

  public fetchFromServiceWithDelete(url: string, postData: any): Observable<any> {
    this.headers = new HttpHeaders();
    this.headers.append('Content-Type', 'application/json');;

    return this.http.delete(environment.API_URL_SERVICES + url, postData).pipe(
      map((res) => this.extractData( res )),
      catchError(this.handleError),
      );
  }

  public fetchFromServiceWithPut(url: string, postData: any): Observable<any> {
    this.headers = new HttpHeaders();
    this.headers.append('Content-Type', 'application/json');;
    return this.http.put(environment.API_URL_SERVICES + url, postData,
                          { headers : this.headers,observe: "response" }).pipe(
      map((res) => {this.extractData( res )}),
      catchError(this.handleError),
      );
  }

  private handleError(error: HttpErrorResponse) {
    this.stage.dispatch(appTokenErroAction({payload: error.status, erro: error.message}));
    if (error.error instanceof HttpErrorResponse) {
      console.error('error:', error.error.message);
      this.stage.dispatch(appTokenErroAction({payload: error.status, erro: error.message}));
    } else {
      console.error(
        `Código retornado de back-end ${error.status}`);
        this.stage.dispatch(appTokenAtualizaAction({payload: error.status, erro: error.message}));
    }
    this.stage.dispatch(appTokenAtualizaAction({payload: error.status, erro: error.message}));
    return throwError(
      'Algo aconteceu; por favor, tente novamente mais tarde.');
  }

  private extractData(res: any) {
    // Processando os headers para incluir ou trocar o token de autorizacao
    if (res.headers) {
      if (res.headers.has('tokenJWT')) {
        this.addHeader('tokenJWT', res.headers.get('tokenJWT'));
        this.stage.dispatch(appTokenAtualizaAction({payload: res.headers.get('tokenJWT'), erro: 0}));
      }
    }

    if (res.body) {
      const body = res.body;
      if (body) {
        return body.data || body
      } else {
        return {}
      }
    } else {
      return {};
    }
  }
}
