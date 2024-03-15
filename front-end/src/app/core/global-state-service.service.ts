import {inject, Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {JsonApiService} from '../shared/json-api.service';

@Injectable({
  providedIn: 'root'
})
export class GlobalStateService {

private apiService = inject(JsonApiService)

token$ = new Observable<any>();
sendPost(url: string, postData: any): Observable<any>
{
  this.token$ = this.apiService.fetchFromServiceWithPost(url, postData);
  return this.token$;
}

sendDelete(url: string, postData: any): Observable<any>
{
  this.token$ = this.apiService.fetchFromServiceWithDelete(url, postData);
  return this.token$;
}

sendPut(url: string, postData: any): Observable<any>
{
  this.token$ = this.apiService.fetchFromServiceWithPut(url, postData);
  return this.token$;
}

sendGet(url: string) : Observable<any>
{
  this.token$ = this.apiService.fetchFromServiceWithGet(url);
  return this.token$;
}

}

