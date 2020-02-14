import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

@Injectable({
  providedIn: 'root'
})
export class FieldService {
  public API = '//localhost:8080/logcollector';
  constructor(private http: HttpClient) { }

  getAuditfield(): Observable<any> {
    return this.http.get(this.API + '/getAudit');
  }
  getFeRequestLodfield(): Observable<any> {
    return this.http.get(this.API + '/getFerequsetlog');
  }
  getFeApiLodfield(): Observable<any> {
    return this.http.get(this.API + '/getFeApiLog');
  }
}
