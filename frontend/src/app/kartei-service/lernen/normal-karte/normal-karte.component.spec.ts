import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NormalKarteComponent } from './normal-karte.component';

describe('NormalKarteComponent', () => {
  let component: NormalKarteComponent;
  let fixture: ComponentFixture<NormalKarteComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [NormalKarteComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(NormalKarteComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
