import {Component, inject, ViewChild} from '@angular/core';
import {Router} from '@angular/router';
import {GlobalStateService} from '../core/global-state-service.service';
import {MatSnackBar} from '@angular/material/snack-bar';
import {MatPaginator, PageEvent} from '@angular/material/paginator';
import {MatSort} from '@angular/material/sort';
import {SelectionModel} from '@angular/cdk/collections';
import {GrupoTransferencia} from '../model/GrupoTransferencia';
import {MatTableDataSource} from '@angular/material/table';
import {SnackBarComponent} from '../snack-bar/snack-bar.component';

@Component({
  selector: 'app-lista-grupo-transacao',
  templateUrl: './lista-grupo-transacao.component.html',
  styleUrl: './lista-grupo-transacao.component.css'
})
export class ListaGrupoTransacaoComponent {

  private router= inject( Router);
  private global= inject(GlobalStateService);
  private _snackBar= inject(MatSnackBar);

  constructor(){
    this.getDataSource();
  }

  displayedColumns: string[] = ['select','idGrupoTransferencia','nome','descricao','status'];
  @ViewChild(MatPaginator, {static: true}) paginator!: MatPaginator;
  @ViewChild(MatSort, {static: true}) sort!: MatSort;

  selection = new SelectionModel <GrupoTransferencia> ( true , []);
  selectedRow!: GrupoTransferencia;
  entitiesSelect:  GrupoTransferencia[] = [];
  dataSource = new MatTableDataSource<GrupoTransferencia>() ;
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
    this.router.navigate([ 'cadastroGrupoTransferencia/'+ codigo]);
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
    this.global.sendPost('grupo-transferencia/listar', {size: this.pageSize, page: this.pageIndex}).subscribe(resposta => {
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
  checkboxLabel(row?: GrupoTransferencia): string {
    if (!row) {
      return `${this.isAllSelected() ? 'deselect' : 'select'} all`;
    }
    return `${this.selection.isSelected(row) ? 'deselect' : 'select'} row ${row.idGrupoTransferencia + 1}`;
  }
  deletandoLista : number[] = [];

  excluir(){
    this.openSnackBar('Processando',3);
    this.selection.selected.forEach(deletando => {
      this.deletandoLista.push(deletando.idGrupoTransferencia);
    })
    this.global.sendDelete('grupo-transferencia/deletar-lista', this.deletandoLista).subscribe(resposta => {
      this.router.navigateByUrl('/RefreshComponent', { skipLocationChange: true }).then(() => {
        this.router.navigate(['listarGrupoTransferencia']);
      });
    },() => {
      this.openSnackBar('Erro contate o adminstrador', 5);
    });

    this.openSnackBar('Deletados', 3);

  }

  adicionar(){
    this.router.navigate([ 'cadastroGrupoTransferencia/0']);
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
