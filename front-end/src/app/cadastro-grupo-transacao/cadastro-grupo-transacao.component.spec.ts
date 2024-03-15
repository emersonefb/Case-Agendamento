import {ComponentFixture, TestBed} from '@angular/core/testing';

import {CadastroGrupoTransacaoComponent} from './cadastro-grupo-transacao.component';

describe('CadastroGrupoTransacaoComponent', () => {
  let component: CadastroGrupoTransacaoComponent;
  let fixture: ComponentFixture<CadastroGrupoTransacaoComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [CadastroGrupoTransacaoComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CadastroGrupoTransacaoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
