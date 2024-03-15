import {ComponentFixture, TestBed} from '@angular/core/testing';

import {DetalheContaComponent} from './detalhe-conta.component';

describe('DetalheContaComponent', () => {
  let component: DetalheContaComponent;
  let fixture: ComponentFixture<DetalheContaComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [DetalheContaComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DetalheContaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
