import {Component, inject, ViewChild} from '@angular/core';
import {NonNullableFormBuilder, Validators,} from '@angular/forms';
import {ActivatedRoute, Router} from '@angular/router';
import {GlobalStateService} from '../core/global-state-service.service';
import {DateAdapter} from '@angular/material/core';
import {DatePipe} from '@angular/common';
import {MatSnackBar} from '@angular/material/snack-bar';
import {SnackBarComponent} from '../snack-bar/snack-bar.component';
import {Conta} from '../model/Conta';
import {delay} from 'rxjs';
import {Transacao} from '../model/Transacao';
import {MatPaginator, PageEvent} from '@angular/material/paginator';
import {MatSort} from '@angular/material/sort';
import {SelectionModel} from '@angular/cdk/collections';
import {MatTableDataSource} from '@angular/material/table';

@Component({
  selector: 'app-detalhe-conta',
  templateUrl: './detalhe-conta.component.html',
  styleUrl: './detalhe-conta.component.css'
})
export class DetalheContaComponent {

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
  displayedColumnsOrigem: string[] = ['idTransacao','contaOrigem', 'contaDestino','dataAgendamento','dataProgramada','valorSolicitado','tipoTransferencia','grupoTransferencia','taxaFixa','taxaVariavel','valorTransacao','status'];
  displayedColumns: string[] = ['idTransacao','contaOrigem', 'contaDestino','dataAgendamento','dataProgramada','valorSolicitado','tipoTransferencia','grupoTransferencia','taxaFixa','taxaVariavel','valorTransacao','status'];
  @ViewChild(MatPaginator, {static: true}) paginator!: MatPaginator;
  @ViewChild(MatSort, {static: true}) sort!: MatSort;

  selection = new SelectionModel <Transacao> ( true , []);
  selectedRow!: Transacao;
  entitiesSelect:  Transacao[] = [];
  dataSource = new MatTableDataSource<Transacao>() ;
  length = 50;
  pageSize = 8;
  pageIndex = 0;
  pageSizeOptions = [4, 8, 10, 20];
  hidePageSize = false;
  showPageSizeOptions = true;
  showFirstLastButtons = true;
  disabled = false;
  pageEvent: PageEvent = new PageEvent;

  selectionOrigem = new SelectionModel <Transacao> ( true , []);
  selectedRowOrigem!: Transacao;
  entitiesSelectOrigem:  Transacao[] = [];
  dataSourceOrigem = new MatTableDataSource<Transacao>() ;
  lengthOrigem = 50;
  pageSizeOrigem = 8;
  pageIndexOrigem = 0;
  pageSizeOptionsOrigem = [4, 8, 10, 20];
  hidePageSizeOrigem = false;
  showPageSizeOptionsOrigem = true;
  showFirstLastButtonsOrigem = true;
  disabledOrigem = false;
  pageEventOrigem: PageEvent = new PageEvent;

  public contaDTO: Conta = {
    idConta: 0,
    contaCorrente: '',
    nome: '',
    dataCadastro: '',
    status: false
  };

  form = this.formBuilder.group(
    {
      idConta: [''],
      contaCorrente: ['', Validators.compose([Validators.required, Validators.minLength(6)])],
      nome: ['',[Validators.required],],
      dataCadastro: [''],
      status: [true],
    }
  );

  submitted = false;
  ngOnInit(): void {
  }


  onSubmit(): void {
    this.submitted = true;
    this.openSnackBar('Processando' ,3 );

    if (this.form.invalid) {
      this.openSnackBar('Verifique os campos obrigatórios', 3);
      return;
    }
    this.goTohttpSave(this.toConta(this.form.value));
  }
  toConta(value: Partial<{ idConta: string; contaCorrente: string | null; nome: string; dataCadastro: string; status: boolean; }>): Conta {
    let cod = 0;
    if(value.idConta !== '0'){
      cod = parseInt(value.idConta as string);
    }
    let dataCadastroDate = this.datepipe.transform(this.form.value.dataCadastro, 'yyyy-MM-dd');
    var dataCadastro = '';
    if(dataCadastroDate?.toString !== undefined){
      dataCadastro = dataCadastroDate as string;
    }
    this.contaDTO.nome=value.nome;
    this.contaDTO.idConta = cod;
    this.contaDTO.contaCorrente = value.contaCorrente as string;
    this.contaDTO.dataCadastro = dataCadastro;
    this.contaDTO.status=value.status
    return this.contaDTO
  }

  onReset(): void {

    this.submitted = false;
    this.form.reset();
  }

  goTohttpSave(json: Conta) {
    if(json.idConta !== 0 ||json.idConta == undefined){
      this.global.sendPost('conta/cadastrar', json).subscribe(resposta => {
        this.popularForm(resposta);
        this.openSnackBar('Cadastrado', 3);
        delay(1000);
      },() => {
        this.openSnackBar('Erro contate o adminstrador', 5);
      });
    } else {
      let rep =  this.global.sendPut('conta/update', json).subscribe(resposta => {
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
    this.router.navigate(['listarContas']);
  }

  goTohttp(id: number){
    this.global.sendGet('conta/buscar/'+ id).subscribe(resposta => {
      this.popularForm(resposta);
    },() => {
      this.openSnackBar('Erro contate o adminstrador', 5);
    });
  }

  private popularForm(resposta: any) {
    this.form.patchValue({
      idConta: resposta.idConta,
      contaCorrente: resposta.contaCorrente,
      nome: resposta.nome,
      dataCadastro: resposta.dataCadastro,
      status: resposta.status
    });
    this.getDataSourceContaDestino();
    this.getDataSourceContaOrigem();
  }


  handlePageEvent(e: PageEvent) {
    this.pageEvent = e;
    this.length = e.length;
    this.pageSize = e.pageSize;
    this.pageIndex = e.pageIndex;
    this.getDataSourceContaDestino();
  }



  handlePageEventOrigem(e: PageEvent) {
    this.pageEvent = e;
    this.length = e.length;
    this.pageSize = e.pageSize;
    this.pageIndex = e.pageIndex;
    this.getDataSourceContaOrigem();
  }

  setPageSizeOptions(setPageSizeOptionsInput: string) {
    if (setPageSizeOptionsInput) {
      this.pageSizeOptions = setPageSizeOptionsInput.split(',').map(str => +str);
    }
  }

  getDataSourceContaDestino(){
    this.global.sendPost('transacao/listar', {size: this.pageSize, page: this.pageIndex , "contaDestino": {
      "contaCorrente" : this.form.controls.contaCorrente.value
    }}).subscribe(resposta => {
      this.dataSource.data = resposta.content;
      this.length = resposta.total_elements;
    },() => {
      this.openSnackBar('Erro contate o adminstrador', 5);
    });
  }


  getDataSourceContaOrigem(){
    this.global.sendPost('transacao/listar', {size: this.pageSize, page: this.pageIndex , "contaOrigem": {
      "contaCorrente" : this.form.controls.contaCorrente.value
    }}).subscribe(resposta => {
      this.dataSourceOrigem.data = resposta.content;
      this.lengthOrigem = resposta.total_elements;
    },() => {
      this.openSnackBar('Erro contate o adminstrador', 5);
    });
  }

  isAllSelected() {
    const numSelected = this.selection.selected.length;
    const numRows = this.dataSource.data.length;
    return numSelected === numRows;
  }

  toggleAllRows() {
    if (this.isAllSelected()) {
      this.selection.clear();
      return;
    }
    this.selection.select(...this.dataSource.data);
  }

  /** The label for the checkbox on the passed row */
  checkboxLabel(row?: Transacao): string {
    if (!row) {
      return `${this.isAllSelected() ? 'deselect' : 'select'} all`;
    }
    return `${this.selection.isSelected(row) ? 'deselect' : 'select'} row ${row.idTransacao + 1}`;
  }
  deletandoLista : number[] = [];

  excluir(){
    this.openSnackBar('Processando', 3);
    this.selection.selected.forEach(deletandos => {
      this.deletandoLista.push(deletandos.idTransacao);
    })
    this.global.sendPost('/transacao/deletar-lista', this.deletandoLista).subscribe(resposta => {
      this.router.navigateByUrl('/RefreshComponent', { skipLocationChange: true }).then(() => {
        this.router.navigate(['editarConta/:'+this.atualId]);
      });
    },() => {
      this.openSnackBar('Erro contate o adminstrador', 5);
    });

    this.openSnackBar('Deletados', 3);

  }

  adicionar(){
    this.router.navigate([ 'editarConta/:0']);
  }

  isAllSelectedOrigem() {
    const numSelected = this.selectionOrigem.selected.length;
    const numRows = this.dataSourceOrigem.data.length;
    return numSelected === numRows;
  }

  toggleAllRowsOrigem() {
    if (this.isAllSelectedOrigem()) {
      this.selectionOrigem.clear();
      return;
    }
    this.selectionOrigem.select(...this.dataSourceOrigem.data);
  }

  /** The label for the checkbox on the passed row */
  checkboxLabelOrigem(row?: Transacao): string {
    if (!row) {
      return `${this.isAllSelectedOrigem() ? 'deselect' : 'select'} all`;
    }
    return `${this.selectionOrigem.isSelected(row) ? 'deselect' : 'select'} row ${row.idTransacao + 1}`;
  }
  deletandoListaOrigem : number[] = [];

  excluirOrigem(){
    this.openSnackBar('Processando', 3);
    this.selectionOrigem.selected.forEach(deletandos => {
      this.deletandoListaOrigem.push(deletandos.idTransacao);
    })
    this.global.sendPost('/transacao/deletar-lista', this.deletandoListaOrigem).subscribe(resposta => {
      this.router.navigateByUrl('/RefreshComponent', { skipLocationChange: true }).then(() => {
        this.router.navigate(['editarConta/:'+this.atualId]);
      });
    },() => {
      this.openSnackBar('Erro contate o adminstrador', 5);
    });

    this.openSnackBar('Deletados', 3);

  }

}
