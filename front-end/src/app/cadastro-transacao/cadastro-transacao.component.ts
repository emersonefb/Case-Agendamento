import {Component, inject} from '@angular/core';
import {NonNullableFormBuilder, Validators} from '@angular/forms';
import {ActivatedRoute, Router} from '@angular/router';
import {GlobalStateService} from '../core/global-state-service.service';
import {DateAdapter} from '@angular/material/core';
import {DatePipe} from '@angular/common';
import {MatSnackBar} from '@angular/material/snack-bar';
import {SnackBarComponent} from '../snack-bar/snack-bar.component';
import {TransacaoCadastro} from '../model/TransacaoCadastro';

@Component({
  selector: 'app-cadastro-transacao',
  templateUrl: './cadastro-transacao.component.html',
  styleUrl: './cadastro-transacao.component.css'
})
export class CadastroTransacaoComponent {

  cadastroNovo: boolean = true;
  options: string[] = [];
  submitted = false;

  private global= inject(GlobalStateService);
  private router = inject(Router)
  private routeActive = inject(ActivatedRoute)
  private formBuilder= inject(NonNullableFormBuilder);
  private dateAdapter= inject(DateAdapter<Date>);
  private datepipe= inject(DatePipe);
  private _snackBar= inject(MatSnackBar);

  openSnackBar(message: string, time: number) {
    this._snackBar.openFromComponent(SnackBarComponent, {
      data: message,
      duration: time * 1000,
      horizontalPosition: 'center',
      verticalPosition: 'top',
      panelClass: ['warning']
    });
  }

  constructor() {
    this.dateAdapter.setLocale('pr-BR');
    this.onReset();
    this.cadastroNovo = true;
  }

  public transacaoDTO: TransacaoCadastro = {
    contaOrigem: '',
    contaDestino: '',
    dataAgendamento: '',
    dataProgramada: '',
    valorSolicitado: 0
  };

  numberPatern = '^[0-9.,]+$';

  form = this.formBuilder.group(
    {
      contaOrigem: ['', Validators.required],
      contaDestino: ['', Validators.required],
      dataAgendamento: [''],
      dataProgramada: ['', Validators.required],
      valorSolicitado: [0, [Validators.required, Validators.pattern(this.numberPatern)]]
    }
  );

  ngOnInit(): void {
  }

  onSubmit(): void {
    this.submitted = true;
    this.openSnackBar('Processando',3);

    if (this.form.invalid) {
      this.openSnackBar('Verifique os campos obrigatórios',4);
      return;
    }

    this.goTohttpSave(this.toTrasacao(this.form.value));
  }

  toTrasacao(value: Partial<{ contaOrigem: string; contaDestino: string; dataAgendamento: string; dataProgramada: string; valorSolicitado: number; }>): TransacaoCadastro {

    let dataAgendamentoDate = this.datepipe.transform(new Date(), 'yyyy-MM-dd');
    var dataAgendamento = '';
    if(dataAgendamentoDate?.toString !== undefined){
      dataAgendamento = dataAgendamentoDate as string;
    }
    let dataProgramadaDate = this.datepipe.transform(this.form.value.dataProgramada, 'yyyy-MM-dd');
    var dataProgramada = '';
    if(dataProgramadaDate?.toString !== undefined){
      dataProgramada = dataProgramadaDate as string;
    }

    this.transacaoDTO.contaOrigem=value.contaOrigem as string;
    this.transacaoDTO.contaDestino=value.contaDestino as string;
    this.transacaoDTO.valorSolicitado=value.valorSolicitado as number;
    this.transacaoDTO.dataAgendamento = dataAgendamento;
    this.transacaoDTO.dataProgramada = dataProgramada;

    return this.transacaoDTO
  }

  onReset(): void {

    this.submitted = false;
    this.form.reset();
  }

  goTohttpSave(json: TransacaoCadastro) {
    this.global.sendPost('transacao/cadastrar', json).subscribe(resposta => {
      this.openSnackBar('Cadastrado',3);
    },() => {
      this.openSnackBar('Não a regra para a transação solicitada', 10);
    });

    this.onReset();
  }

  back(){
    this.router.navigate(['listarTransacao']);
  }

}
