import {GrupoTransferencia} from "./GrupoTransferencia";


export interface TipoTransferencia {
    idTipoTransferencia: number;
    nome?: string;
    descricao?: string;
    minDias: number;
    maxDias: number;
    minValor: number;
    maxValor: number;
    taxaFixa: number;
    taxaVariavel: number;
    status: boolean;
    grupoTransferencia: GrupoTransferencia;
}
