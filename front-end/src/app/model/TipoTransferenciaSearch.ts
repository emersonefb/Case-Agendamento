
export interface TipoTransferenciaSearch {
    size: number;
    page: number;
    nome: string;
    descricao: string;
    maxDias: number;
    minValor: number;
    maxValor: number;
    taxaFixa: number;
    taxaVariavel: number;
    status: boolean;
}
