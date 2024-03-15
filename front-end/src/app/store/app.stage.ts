import {createAction, props} from "@ngrx/store";

export interface IAppStage {
 tokenJWT: string;
 erro: number,
}

export const appInitialState : IAppStage = {
    tokenJWT: '1',
    erro: 0
}

export const appTokenActionInicial = createAction(  // cria uma ação
    'appToken nome', // text com nome
)

export const appTokenAction = createAction(  // cria outra ação
    'appToken nome'
)

export const appActionTodos = createAction(  // cria outra ação
    'appToken nome todos'
)

export const appActionSucesso = createAction(  // cria outra ação
'appToken atualizar Sucesso' // payload da ação
)

export const appTokenAtualizaAction = createAction(  // cria outra ação
    'appToken atualizar',
    props<{payload: any, erro: any}>() // payload da ação
)

export const appTokenErroAction = createAction(  // cria outra ação
    'appToken erro',
    props<{payload: any, erro: any}>() // payload da ação
)

