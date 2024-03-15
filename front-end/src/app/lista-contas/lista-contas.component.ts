import {Component, inject, ViewChild} from '@angular/core';
import {SnackBarComponent} from '../snack-bar/snack-bar.component';
import {Router} from '@angular/router';
import {GlobalStateService} from '../core/global-state-service.service';
import {MatSnackBar} from '@angular/material/snack-bar';
import {MatPaginator, PageEvent} from '@angular/material/paginator';
import {MatSort} from '@angular/material/sort';
import {SelectionModel} from '@angular/cdk/collections';
import {MatTableDataSource} from '@angular/material/table';
import {Conta} from '../model/Conta';
import {DateAdapter} from '@angular/material/core';
import {DatePipe} from '@angular/common';

@Component({
  selector: 'app-lista-contas',
  templateUrl: './lista-contas.component.html',
  styleUrl: './lista-contas.component.css'
})
export class ListaContasComponent {


  private router= inject( Router);
  private global= inject(GlobalStateService);
  private _snackBar= inject(MatSnackBar);
  private dateAdapter= inject(DateAdapter<Date>);
  private datepipe= inject(DatePipe);

  constructor(){
    this.dateAdapter.setLocale('pr-BR');
    this.getDataSource();
  }

  displayedColumns: string[] = ['select','idConta','contaCorrente','nome','dataCadastro','status'];
  @ViewChild(MatPaginator, {static: true}) paginator!: MatPaginator;
  @ViewChild(MatSort, {static: true}) sort!: MatSort;

  selection = new SelectionModel <Conta> ( true , []);
  selectedRow!: Conta;
  entitiesSelect:  Conta[] = [];
  dataSource = new MatTableDataSource<Conta>() ;
  length = 50;
  pageSize = 8;
  pageIndex = 0;
  pageSizeOptions = [4, 8, 10, 20];
  hidePageSize = false;
  showPageSizeOptions = true;
  showFirstLastButtons = true;
  disabled = false;
  pageEvent: PageEvent = new PageEvent;

  ngOnInit() {
    this.getDataSource();
  }

  rowClicked(codigo: number) {

    this.openSnackBar('Processando',3);
    this.router.navigate([ 'editarConta/'+ codigo]);
  }

  handlePageEvent(e: PageEvent) {
    this.pageEvent = e;
    this.length = e.length;
    this.pageSize = e.pageSize;
    this.pageIndex = e.pageIndex;
    this.getDataSource();
  }

  setPageSizeOptions(setPageSizeOptionsInput: string) {
    if (setPageSizeOptionsInput) {
      this.pageSizeOptions = setPageSizeOptionsInput.split(',').map(str => +str);
    }
  }
  getDataSource(){
    this.global.sendPost('conta/listar', {size: this.pageSize, page: this.pageIndex}).subscribe(resposta => {
      this.dataSource.data = resposta.content;
      this.length = resposta.total_elements;
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
  checkboxLabel(row?: Conta): string {
    if (!row) {
      return `${this.isAllSelected() ? 'deselect' : 'select'} all`;
    }
    return `${this.selection.isSelected(row) ? 'deselect' : 'select'} row ${row.idConta + 1}`;
  }
  deletandoLista : number[] = [];

  excluir(){
    this.openSnackBar('Processando',3);
    this.selection.selected.forEach(deletando => {
      this.deletandoLista.push(deletando.idConta);
    })
    this.global.sendPost('conta/deletarLista', this.deletandoLista).subscribe(resposta => {
      this.router.navigateByUrl('/RefreshComponent', { skipLocationChange: true }).then(() => {
        this.router.navigate(['listarContas']);
      });
    },() => {
      this.openSnackBar('Erro contate o adminstrador', 5);
    });

    this.openSnackBar('Deletados', 3);

  }

  adicionar(){
    this.router.navigate([ 'editarConta/0']);
  }

  openSnackBar(message: string, time: number) {
    this._snackBar.openFromComponent(SnackBarComponent, {
      data: message,
      duration: time * 1000,
      horizontalPosition: 'center',
      verticalPosition: 'top',
      panelClass: ['warning']
    });
  }
}
