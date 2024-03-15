import { Component, ViewChild, inject } from '@angular/core';
import { SnackBarComponent } from '../snack-bar/snack-bar.component';
import { Router } from '@angular/router';
import { GlobalStateService } from '../core/global-state-service.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MatPaginator, PageEvent } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { SelectionModel } from '@angular/cdk/collections';
import { Transacao } from '../model/Transacao';
import { MatTableDataSource } from '@angular/material/table';
import { delay } from 'rxjs';

@Component({
  selector: 'app-lista-transcao',
  templateUrl: './lista-transcao.component.html',
  styleUrl: './lista-transcao.component.css'
})
export class ListaTranscaoComponent {

  private router= inject( Router);
  private global= inject(GlobalStateService);
  private _snackBar= inject(MatSnackBar);
  
  displayedColumns: string[] = ['select','idTransacao','contaOrigem', 'contaDestino','dataAgendamento','dataProgramada','valorSolicitado','tipoTransferencia','grupoTransferencia','taxaFixa','taxaVariavel','valorTransacao','status'];
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

  constructor(){
    this.getDataSource();
  }
  
  ngOnInit() {
    this.getDataSource();
  }

  rowClicked(codigo: number) {
    this.openSnackBar('Processando',3);
    this.router.navigate([ 'cadastroTransacao']);
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
    this.global.sendPost('transacao/listar', {size: this.pageSize, page: this.pageIndex}).subscribe(resposta => {
      this.dataSource.data = resposta.content;
      this.length = resposta.total_elements;
      delay(100);
      
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
    this.openSnackBar('Processando',3);
    this.selection.selected.forEach(deletando => {
      this.deletandoLista.push(deletando.idTransacao);
    })
    this.global.sendPost('transacao/deletar-lista', this.deletandoLista).subscribe(resposta => {
      this.router.navigateByUrl('/RefreshComponent', { skipLocationChange: true }).then(() => {
        this.router.navigate(['listarTransacao']);
      }); 
    });
    
    this.openSnackBar('Deletados',4);
    
  }

  adicionar(){
    this.router.navigate([ 'cadastroTransacao']);
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
