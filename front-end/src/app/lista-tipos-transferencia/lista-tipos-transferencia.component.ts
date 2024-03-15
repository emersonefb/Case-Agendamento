import {Component, inject, ViewChild} from '@angular/core';
import {SnackBarComponent} from '../snack-bar/snack-bar.component';
import {Router} from '@angular/router';
import {GlobalStateService} from '../core/global-state-service.service';
import {MatSnackBar} from '@angular/material/snack-bar';
import {MatPaginator, PageEvent} from '@angular/material/paginator';
import {MatSort} from '@angular/material/sort';
import {SelectionModel} from '@angular/cdk/collections';
import {TipoTransferencia} from '../model/TipoTransferencia';
import {MatTableDataSource} from '@angular/material/table';

@Component({
  selector: 'app-lista-tipos-transferencia',
  templateUrl: './lista-tipos-transferencia.component.html',
  styleUrl: './lista-tipos-transferencia.component.css'
})
export class ListaTiposTransferenciaComponent {

  private router= inject( Router);
  private global= inject(GlobalStateService);
  private _snackBar= inject(MatSnackBar);

  constructor(){
    this.getDataSource();
  }

  displayedColumns: string[] = ['select','idTipoTransferencia','grupo','nome','descricao','minDias','maxDias','minValor','maxValor','taxaFixa','taxaVariavel','status'];
  @ViewChild(MatPaginator, {static: true}) paginator!: MatPaginator;
  @ViewChild(MatSort, {static: true}) sort!: MatSort;

  selection = new SelectionModel <TipoTransferencia> ( true , []);
  selectedRow!: TipoTransferencia;
  entitiesSelect:  TipoTransferencia[] = [];
  dataSource = new MatTableDataSource<TipoTransferencia>() ;
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
    this.router.navigate([ 'editarTiposTransferencia/'+ codigo]);

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
    this.global.sendPost('tipo-transferencia/listar', {size: this.pageSize, page: this.pageIndex}).subscribe(resposta => {
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
  checkboxLabel(row?: TipoTransferencia): string {
    if (!row) {
      return `${this.isAllSelected() ? 'deselect' : 'select'} all`;
    }
    return `${this.selection.isSelected(row) ? 'deselect' : 'select'} row ${row.idTipoTransferencia + 1}`;
  }
  deletandoLista : number[] = [];

  excluir(){
    this.openSnackBar('Processando',3);
    this.selection.selected.forEach(deletando => {
      this.deletandoLista.push(deletando.idTipoTransferencia);
    })

    this.global.sendPost('tipo-transferencia/deletar-lista', this.deletandoLista).subscribe(resposta => {
      this.router.navigateByUrl('/RefreshComponent', { skipLocationChange: true }).then(() => {
        this.router.navigate(['listarTipoTransferencia']);
      });
    },() => {
            this.openSnackBar('Erro contate o adminstrador', 5);
    });

    this.openSnackBar('Deletados', 3);

  }

  adicionar(){
    this.router.navigate([ 'editarTiposTransferencia/0']);
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
