import {GrupoTransferencia} from "./GrupoTransferencia";


export interface GrupoTransferenciaPage {
    totalPages: number;
    total_elements: number;
    page: number;
    per_page: number;
    has_next: boolean;
    has_prev: boolean;
    content: GrupoTransferencia[];
}
