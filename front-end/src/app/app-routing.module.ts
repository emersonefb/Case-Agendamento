import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {HomeComponent} from './home/home.component';
import {ListaTranscaoComponent} from './lista-transcao/lista-transcao.component';
import {ListaContasComponent} from './lista-contas/lista-contas.component';
import {DetalheContaComponent} from './detalhe-conta/detalhe-conta.component';
import {CadastroTransacaoComponent} from './cadastro-transacao/cadastro-transacao.component';
import {CadastroGrupoTransacaoComponent} from './cadastro-grupo-transacao/cadastro-grupo-transacao.component';
import {ListaGrupoTransacaoComponent} from './lista-grupo-transacao/lista-grupo-transacao.component';
import {ListaTiposTransferenciaComponent} from './lista-tipos-transferencia/lista-tipos-transferencia.component';
import {DetalheTiposTransferenciaComponent} from './detalhe-tipos-transferencia/detalhe-tipos-transferencia.component';

const routes: Routes = [
  {
    path: '', component: HomeComponent,
    data: {
      title: 'Home'
    }
  },
  {
    path: 'listarContas', component: ListaContasComponent,
    data: {
      title: 'listarContas'
    }
  },
  {
    path: 'editarConta/:id', component: DetalheContaComponent,
    data: {
      title: 'editarConta'
    }
  },
  {
    path: 'cadastroGrupoTransferencia/:id', component: CadastroGrupoTransacaoComponent,
    data: {
      title: 'cadastroGrupoTransferencia'
    }
  },
  {
    path: 'listarGrupoTransferencia', component: ListaGrupoTransacaoComponent,
    data: {
      title: 'listarGrupoTransferencia'
    }
  },
  {
    path: 'listarTipoTransferencia', component: ListaTiposTransferenciaComponent,
    data: {
      title: 'listarTipoTransferencia'
    }
  },
  {
    path: 'editarTiposTransferencia/:id', component: DetalheTiposTransferenciaComponent,
    data: {
      title: 'editarTiposTransferencia'
    }
  },
  {
    path: 'cadastroTransacao', component: CadastroTransacaoComponent,
    data: {
      title: 'cadastroTransacao'
    }
  },
  {
    path: 'listarTransacao', component: ListaTranscaoComponent,
    data: {
      title: 'listarTransacao'
    }
  },
  {
    path: '**',  redirectTo: '/'
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
