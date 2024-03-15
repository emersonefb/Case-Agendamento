import {ComponentFixture, TestBed} from '@angular/core/testing';

import {ListaTranscaoComponent} from './lista-transcao.component';

describe('ListaTranscaoComponent', () => {
  let component: ListaTranscaoComponent;
  let fixture: ComponentFixture<ListaTranscaoComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ListaTranscaoComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ListaTranscaoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
