package br.com.efb.domain.excepition;

public class ErroException extends RuntimeException {

    public ErroException(String exception){
        super(exception);
    }
}
