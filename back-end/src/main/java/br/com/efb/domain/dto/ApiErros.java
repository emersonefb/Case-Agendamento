package br.com.efb.domain.dto;

import lombok.Data;

import java.util.List;

@Data
public class ApiErros {

    private List<String> message;

    public ApiErros(List<String> message) {
        this.message = message;
    }
}
