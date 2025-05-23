import { ComponentFixture, TestBed } from '@angular/core/testing';

import { KarteiServiceComponent } from './kartei-service.component';
import {HttpClientTestingModule} from '@angular/common/http/testing';
import {ActivatedRoute} from '@angular/router';
import {of} from 'rxjs';

describe('KarteiServiceComponent', () => {
  let component: KarteiServiceComponent;
  let fixture: ComponentFixture<KarteiServiceComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [KarteiServiceComponent, HttpClientTestingModule],
      providers: [{
        provide: ActivatedRoute,
        useValue: {
          snapshot: {
            paramMap: {
              get: (key: string) => '123',
            },
          },
          params: of({ id: '123' }),
        },
      }]
    })
    .compileComponents();

    fixture = TestBed.createComponent(KarteiServiceComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
