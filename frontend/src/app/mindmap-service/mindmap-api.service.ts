import {inject, Injectable} from '@angular/core';
import {LoggingService} from '../logging.service';
import {HttpClient} from '@angular/common/http';
import {HeaderService} from '../header.service';
import {Observable} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class MindmapApiService {
  BASE_API_URL = 'http://localhost:9087/api/v1';
  log = new LoggingService("MindmapApiService", "mindmap-service");
  http = inject(HttpClient);
  headerService = inject(HeaderService)

  getAllMindmapsByUsername() : Observable<any> {
    const header = this.headerService.createAuthHeader();
    return this.http.get(`${this.BASE_API_URL}/get-all-mindmaps-by-username`, { headers: header });
  }

  getMindmapByModulId(modulId: string) : Observable<any> {
    const header = this.headerService.createAuthHeader();
    return this.http.get<any>(`${this.BASE_API_URL}/get-mindmap-by-modulid`, {
      headers: header,
      params: {modulId: modulId}
    })

  }

  postNewNode(data: any): Observable<any> {
    const header = this.headerService.createAuthHeader();
    return this.http.post<any>(`${this.BASE_API_URL}/create-new-node`, data, { headers: header });

  }
}
