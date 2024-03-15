import {Component, inject} from '@angular/core';
import {GlobalStateService} from '../core/global-state-service.service';
import {ActivatedRoute, Router} from '@angular/router';
import {FormControl, NonNullableFormBuilder, Validators} from '@angular/forms';
import {DateAdapter} from '@angular/material/core';
import {DatePipe} from '@angular/common';
import {MatSnackBar} from '@angular/material/snack-bar';
import {SnackBarComponent} from '../snack-bar/snack-bar.component';
import {TipoTransferencia} from '../model/TipoTransferencia';
import {delay} from 'rxjs';
import {GrupoTransferencia} from '../model/GrupoTransferencia';

@Component({
  selector: 'app-detalhe-tipos-transferencia',
  templateUrl: './detalhe-tipos-transferencia.component.html',
  styleUrl: './detalhe-tipos-transferencia.component.css'
})
export class DetalheTiposTransferenciaComponent {
  cadastroNovo: boolean = true;
  atualId: number = 0;

  private global= inject(GlobalStateService);
  private router = inject(Router)
  private routeActive = inject(ActivatedRoute)
  private formBuilder= inject(NonNullableFormBuilder);
  private dateAdapter= inject(DateAdapter<Date>);
  private datepipe= inject(DatePipe);
  private _snackBar= inject(MatSnackBar);


  openSnackBar(message: string , time: number) {
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
      this.atualId = id as number;
      this.cadastroNovo = false;
    } else {
      this.onReset();
      this.cadastroNovo = true;
    }

  }

  grupoCombo: GrupoTransferencia[] = [
    {
      idGrupoTransferencia: 0,
      nome: '',
      descricao: '',
      status: true
    }
  ];

  idGrupo: number = 0;
  grupoId = new FormControl(this.idGrupo);

  public grupo : GrupoTransferencia = {
    idGrupoTransferencia: this.idGrupo,
    nome: '',
    descricao: '',
    status: true
  }

  public tipoTransferenciaDTO: TipoTransferencia = {
    idTipoTransferencia: 0,
    minDias: 0,
    maxDias: 0,
    minValor: 0,
    maxValor: 0,
    taxaFixa: 0,
    taxaVariavel: 0,
    status: true,
    grupoTransferencia: {
      idGrupoTransferencia: 0,
      nome: '',
      descricao: '',
      status: true
    }
  };



  form = this.formBuilder.group(
    {
      idTipoTransferencia: 0,
      nome: ['',Validators.required],
      descricao: [''],
      status: [true],
      minDias: [0],
      maxDias: [0],
      minValor: [0],
      maxValor: [0],
      taxaFixa: [0],
      taxaVariavel: [0],
      idGrupoTransferencia: 0,
    }
  );

  submitted = false;
  ngOnInit(): void {
    this.goTohttpCombo();
  }

  goTohttpCombo() {
    this.global.sendPost('grupo-transferencia/listar', {size: 100, page: 0}).subscribe(resposta => {
      this.grupoCombo = resposta.content;
    },() => {
      this.openSnackBar('Erro contate o adminstrador', 5);
    });
  }


  onSubmit(): void {
    this.submitted = true;
    this.openSnackBar('Processando' ,3 );

    if (this.form.invalid) {
      this.openSnackBar('Verifique os campos obrigatórios', 3);
      return;
    }
    this.goTohttpSave(this.toTipoTransferencias(this.form.value,this.grupoId));
  }
  toTipoTransferencias(value: Partial<{ idTipoTransferencia: number; nome: string; descricao: string; status: boolean; minDias: number; maxDias: number; minValor: number; maxValor: number; taxaFixa: number; taxaVariavel: number; idGrupoTransferencia: number; }>, grupoId: FormControl<number | null>): TipoTransferencia {
    this.tipoTransferenciaDTO.idTipoTransferencia=value.idTipoTransferencia as number;
    this.tipoTransferenciaDTO.nome=value.nome;
    this.tipoTransferenciaDTO.descricao=value.descricao;
    this.tipoTransferenciaDTO.minDias=value.minDias as number;
    this.tipoTransferenciaDTO.maxDias=value.maxDias as number;
    this.tipoTransferenciaDTO.minDias=value.minDias as number;
    this.tipoTransferenciaDTO.minValor=value.minValor as number;
    this.tipoTransferenciaDTO.maxValor=value.maxValor as number;
    this.tipoTransferenciaDTO.taxaFixa=value.taxaFixa as number;
    this.tipoTransferenciaDTO.taxaVariavel=value.taxaVariavel as number;
    this.tipoTransferenciaDTO.status=value.status as boolean;

    this.grupo.idGrupoTransferencia=value.idGrupoTransferencia as number;
    this.tipoTransferenciaDTO.grupoTransferencia = this.grupo;

    return this.tipoTransferenciaDTO
  }

  onReset(): void {
    this.submitted = false;
    this.form.reset();
  }

  goTohttpSave(json: TipoTransferencia) {
    if(json.idTipoTransferencia == 0 || json.idTipoTransferencia == undefined){
      this.global.sendPost('tipo-transferencia/cadastrar', json).subscribe(resposta => {
        this.popularForm(resposta);
        this.openSnackBar('Cadastrado', 3);
        delay(1000);
      },() => {
        this.openSnackBar('Erro contate o adminstrador', 5);
      });
    } else {
      let rep =  this.global.sendPut('tipo-transferencia/update', json).subscribe(resposta => {
        this.popularForm(resposta);
        this.openSnackBar('Atualizado', 3);
        delay(1000);
        this.back();
      },() => {
        this.openSnackBar('Erro contate o adminstrador', 5);
      });
    }

  }

  back(){
    this.router.navigate(['listarTipoTransferencia']);
  }

  goTohttp(id: number){
    this.global.sendGet('tipo-transferencia/buscar/'+ id).subscribe(resposta => {
      this.popularForm(resposta);
    },() => {
      this.openSnackBar('Erro contate o adminstrador', 5);
    });
  }

  private popularForm(resposta: any) {

    this.form.patchValue({
      idTipoTransferencia: resposta.idTipoTransferencia,
      nome: resposta.nome,
      descricao: resposta.descricao,
      minDias: resposta.minDias,
      maxDias: resposta.maxDias,
      minValor: resposta.minValor,
      maxValor: resposta.maxValor,
      taxaFixa: resposta.taxaFixa,
      taxaVariavel: resposta.taxaVariavel,
      status: resposta.status,
      idGrupoTransferencia : resposta.grupoTransferencia.idGrupoTransferencia,

    });

  }

  adicionar(){
    this.router.navigate([ 'editarTiposTransferencia/:0']);
  }


}
