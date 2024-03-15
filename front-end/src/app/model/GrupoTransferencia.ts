import {TipoTransferencia} from "./TipoTransferencia";


export interface GrupoTransferencia {
    idGrupoTransferencia: number;
    nome: string;
    descricao: string;
    tipoTransferenciaList?: TipoTransferencia[];
    status: boolean;
}
