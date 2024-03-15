import {Component, inject} from '@angular/core';
import {GlobalStateService} from '../core/global-state-service.service';
import {ActivatedRoute, Router} from '@angular/router';
import {NonNullableFormBuilder, Validators} from '@angular/forms';
import {DateAdapter} from '@angular/material/core';
import {DatePipe} from '@angular/common';
import {MatSnackBar} from '@angular/material/snack-bar';
import {SnackBarComponent} from '../snack-bar/snack-bar.component';
import {GrupoTransferencia} from '../model/GrupoTransferencia';
import {TipoTransferencia} from '../model/TipoTransferencia';
import {delay} from 'rxjs';

@Component({
  selector: 'app-cadastro-grupo-transacao',
  templateUrl: './cadastro-grupo-transacao.component.html',
  styleUrl: './cadastro-grupo-transacao.component.css'
})
export class CadastroGrupoTransacaoComponent {

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
    let id = this.routeActive.snapshot.params['id'];

    if(id !== '0' && id !== undefined){
      this.goTohttp(id);
      this.cadastroNovo = false;
    } else {
      this.onReset();
      this.cadastroNovo = true;
    }
  }
  goTohttp(id: number){
    this.global.sendGet('grupo-transferencia/buscar/'+ id).subscribe(resposta => {
      this.popularForm(resposta);
    },() => {
      this.openSnackBar('Erro contate o adminstrador', 5);
    });
  }

  private popularForm(resposta: any) {
    this.form.patchValue({
      idGrupoTransferencia: resposta.idGrupoTransferencia,
      descricao: resposta.descricao,
      nome: resposta.nome,
      status: resposta.status
    });

  }

  public grupoTransferenciaDTO: GrupoTransferencia = {
    idGrupoTransferencia: 0,
    nome: '',
    descricao: '',
    tipoTransferenciaList: [],
    status: true
  };

  form = this.formBuilder.group(
    {
      idGrupoTransferencia: [0],
      nome: ['', Validators.required],
      descricao: ['', Validators.required],
      tipoTransferenciaList: [[]],
      status: [true]
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

    this.goTohttpSave(this.toGrupoTransferencia(this.form.value));
  }
  toGrupoTransferencia(value: Partial<{ idGrupoTransferencia: number; nome: string; descricao: string; TipoTransferenciaList: TipoTransferencia[]; status: boolean; }>): GrupoTransferencia {

    this.grupoTransferenciaDTO.idGrupoTransferencia = value.idGrupoTransferencia as number;
    this.grupoTransferenciaDTO.nome = value.nome as string;
    this.grupoTransferenciaDTO.descricao = value.descricao as string;
    this.grupoTransferenciaDTO.status = value.status as boolean;
    this.grupoTransferenciaDTO.tipoTransferenciaList = value.TipoTransferenciaList;

    return this.grupoTransferenciaDTO
  }

  onReset(): void {

    this.submitted = false;
    this.form.reset();
  }

  goTohttpSave(json: GrupoTransferencia) {
    this.global.sendPost('grupo-transferencia/cadastrar', json).subscribe(resposta => {
      this.openSnackBar('Cadastrado',3);
      delay(1000);
      this.back();
    },() => {
      this.openSnackBar('Erro contate o adminstrador', 5);
    });

  }

  back(){
    this.router.navigate(['listarGrupoTransferencia']);
  }


}
