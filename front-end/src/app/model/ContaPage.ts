import {Conta} from "./Conta";


export interface ContaPage {
    totalPages: number;
    total_elements: number;
    page: number;
    per_page: number;
    has_next: boolean;
    has_prev: boolean;
    content: Conta[];
}
