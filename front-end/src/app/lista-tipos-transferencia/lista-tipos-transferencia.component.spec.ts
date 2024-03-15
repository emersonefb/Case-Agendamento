import {ComponentFixture, TestBed} from '@angular/core/testing';

import {ListaTiposTransferenciaComponent} from './lista-tipos-transferencia.component';

describe('ListaTiposTransferenciaComponent', () => {
  let component: ListaTiposTransferenciaComponent;
  let fixture: ComponentFixture<ListaTiposTransferenciaComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ListaTiposTransferenciaComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ListaTiposTransferenciaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
