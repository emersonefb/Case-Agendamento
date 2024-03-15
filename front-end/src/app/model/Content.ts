import {EnderecoList} from "./EnderecoList";


export interface Content {
  codigo: number;
  codigoExterno: string;
  nome: string;
  email: string;
  cpf: string;
  sexo: string;
  estadoCivil: string;
  conjuge: string;
  documentoIdentificacao: string;
  obsSemImpact: string;
  condicaoPessoal: any;
  telefone: string;
  celular: string;
  validateFichaCadastral: number[];
  dataNascimento: number[];
  habilitaEnvioToken: boolean;
  status: boolean;
  ppe: any;
  assinaDigital: boolean;
  enderecoList: EnderecoList[];
}
