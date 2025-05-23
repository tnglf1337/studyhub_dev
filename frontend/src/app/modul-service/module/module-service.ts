import {inject, Injectable} from '@angular/core';
import { Modul} from './modul';
import { HttpClient } from '@angular/common/http';
import {Observable } from 'rxjs';
import {HeaderService} from '../../header.service';

// TODO: need to handle errors when backend htorws exception for almost every method here
@Injectable({
  providedIn: 'root' // macht den Service global verfügbar
})
export class ModuleService {
  private MODUL_BASE_API = 'http://localhost:9080/api';
  private headerService = inject(HeaderService)
  private http = inject(HttpClient)

  resetTimer(fachId: string): Observable<any> {
    const headers = this.headerService.createAuthHeader()
    return this.http.put(this.MODUL_BASE_API + '/reset', fachId, {headers});
  }

  getActiveModuleByUsername(): Observable<Modul[]> {
    const headers = this.headerService.createAuthHeader()
    return this.http.get<Modul[]>(this.MODUL_BASE_API + '/get-active-modules', {headers});
  }

  getAllModulesByUsername(): Observable<Modul[]> {
    const headers = this.headerService.createAuthHeader()
    return this.http.get<Modul[]>(this.MODUL_BASE_API + '/get-all-by-username', { headers });
  }

  getSeconds(fachId : string) : Observable<number> {
    const headers = this.headerService.createAuthHeader()
    return this.http.post<number>(this.MODUL_BASE_API + '/get-seconds', fachId,  {headers})
  }

  postNewSeconds(fachId: string, sessionSecondsLearned: number): Observable<any> {
    const element = document.getElementById(fachId);
    if (!element || !element.dataset['value']) return new Observable<any>();

    const seconds = parseInt(element.dataset['value'], 10);
    if (isNaN(seconds)) return new Observable<any>();;

    const headers = this.headerService.createAuthHeader()

    const payload = {
      fachId: fachId,
      secondsLearned: seconds,
      secondsLearnedThisSession: sessionSecondsLearned
    };

    return this.http.post<any>(this.MODUL_BASE_API + '/update', payload, {headers})
  }

  postFormData(formData : any) : Observable<any> {
    const headers = this.headerService.createAuthHeader()
    return this.http.post(this.MODUL_BASE_API + '/new-modul', formData, {headers})
  }

  deleteModul(fachId: string) : Observable<void> {
    const headers = this.headerService.createAuthHeader()
    return this.http.delete<void>(this.MODUL_BASE_API + '/delete?fachId=' + fachId, {headers})
  }

  putAktivStatus(fachId: string) : Observable<void> {
    const headers = this.headerService.createAuthHeader()
    return this.http.put<void>(this.MODUL_BASE_API + '/change-active', fachId, {headers})
  }

  sendAddTimeData(data: any) {
    const headers = this.headerService.createAuthHeader()
    this.http.post(this.MODUL_BASE_API + '/add-time', data, {headers}).subscribe()
  }

  sendKlausurDateData(data: any) {
    const headers = this.headerService.createAuthHeader()
    this.http.post(this.MODUL_BASE_API + '/add-klausur-date', data ,{headers}).subscribe()
  }
}
