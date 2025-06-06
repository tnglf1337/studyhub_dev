import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MindmapServiceComponent } from './mindmap-service.component';

describe('MindmapServiceComponent', () => {
  let component: MindmapServiceComponent;
  let fixture: ComponentFixture<MindmapServiceComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MindmapServiceComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(MindmapServiceComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
