import {TipoTransferencia} from "./TipoTransferencia";


export interface GrupoTransferenciaSearch {
    size: number;
    page: number;
    nome: string;
    descricao: string;
    TipoTransferencia: TipoTransferencia;
    status: boolean;
}
