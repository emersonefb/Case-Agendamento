import {ComponentFixture, TestBed} from '@angular/core/testing';

import {ListaGrupoTransacaoComponent} from './lista-grupo-transacao.component';

describe('ListaGrupoTransacaoComponent', () => {
  let component: ListaGrupoTransacaoComponent;
  let fixture: ComponentFixture<ListaGrupoTransacaoComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ListaGrupoTransacaoComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ListaGrupoTransacaoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
