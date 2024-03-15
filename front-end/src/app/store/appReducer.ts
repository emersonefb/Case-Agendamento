import {createReducer, on} from "@ngrx/store";
import {
  appInitialState,
  appTokenAction,
  appTokenActionInicial,
  appTokenAtualizaAction,
  appTokenErroAction
} from "./app.stage";


export const appReducer = createReducer(
    appInitialState,
    on(appTokenActionInicial, (stage) => {

        stage = {
            ...stage, // recebendo atual
            tokenJWT: stage.tokenJWT  +'12'//novo valor
        }; // recebendo um novo objeto
        return stage; // retorna o estado
    }),
    on(appTokenAction, (stage) => {

        stage = {
            ...stage, // recebendo atual
            tokenJWT: 'Limpo'//novo valor
        }; // recebendo um novo objeto
        return stage; // retorna o estado
    }),

    on(appTokenErroAction, (stage, {payload, erro}) => {

        stage = {
            ...stage, // recebendo atual
            tokenJWT: payload,
            erro: erro//novo valor
        }; // recebendo um novo objeto
        return stage; // retorna o estado
    }),
    on(appTokenAtualizaAction, (stage, {payload}) => {

        stage = {
            ...stage, // recebendo atual
            tokenJWT: payload//novo valor
        }; // recebendo um novo objeto
        return stage; // retorna o estado
    })
);
