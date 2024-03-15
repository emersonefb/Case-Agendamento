import {Conta} from "./Conta";
import {TipoTransferencia} from "./TipoTransferencia";


export interface Transacao {
    idTransacao: number;
    dataAgendamento: string;
    dataProgramada: string;
    valorSolicitado: number;
    valorTransacao: number;
    contaOrigem: Conta;
    contaDestino: Conta;
    tipoTransferencia: TipoTransferencia;
    taxaFixa: number;
    taxaVariavel: number;
    status: string;
}
