import {ComponentFixture, TestBed} from '@angular/core/testing';

import {DetalheTiposTransferenciaComponent} from './detalhe-tipos-transferencia.component';

describe('DetalheTiposTransferenciaComponent', () => {
  let component: DetalheTiposTransferenciaComponent;
  let fixture: ComponentFixture<DetalheTiposTransferenciaComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [DetalheTiposTransferenciaComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DetalheTiposTransferenciaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
