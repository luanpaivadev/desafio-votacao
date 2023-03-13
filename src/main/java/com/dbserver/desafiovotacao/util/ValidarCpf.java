package com.dbserver.desafiovotacao.util;

public class ValidarCpf {

    public static boolean validar(String cpf) {

        cpf = cpf.replaceAll("\\D", "");

        if (cpf.length() != 11) {
            return false;
        }

        boolean todosDigitosIguais = true;
        for (int i = 1; i < 11; i++) {
            if (cpf.charAt(i) != cpf.charAt(0)) {
                todosDigitosIguais = false;
                break;
            }
        }
        if (todosDigitosIguais) {
            return false;
        }

        int soma = 0;
        for (int i = 0; i < 9; i++) {
            int digito = Character.getNumericValue(cpf.charAt(i));
            soma += digito * (10 - i);
        }
        int resto = soma % 11;
        int primeiroDigitoVerificador = 0;
        if (resto >= 2) {
            primeiroDigitoVerificador = 11 - resto;
        }

        if (primeiroDigitoVerificador != Character.getNumericValue(cpf.charAt(9))) {
            return false;
        }

        soma = 0;
        for (int i = 0; i < 10; i++) {
            int digito = Character.getNumericValue(cpf.charAt(i));
            soma += digito * (11 - i);
        }
        resto = soma % 11;
        int segundoDigitoVerificador = 0;
        if (resto >= 2) {
            segundoDigitoVerificador = 11 - resto;
        }

        return segundoDigitoVerificador == Character.getNumericValue(cpf.charAt(10));
    }
}
