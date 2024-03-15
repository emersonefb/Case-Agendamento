import {Conta} from "./Conta";
import {GrupoTransferencia} from "./GrupoTransferencia";


export interface TransacaoSearch {
    size: number;
    page: number;
    dataAgendamento: string;
    dataProgramada: string;
    valorSolicitado: number;
    valorTransacao: number;
    contaOrigem: Conta;
    contaDestino: Conta;
    grupoTransferencia: GrupoTransferencia;
    status: string;
}
