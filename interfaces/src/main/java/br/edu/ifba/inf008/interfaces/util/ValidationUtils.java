package br.edu.ifba.inf008.interfaces.util;

/**
 * Utilitários comuns para validação de dados
 */
public class ValidationUtils {
    
    /**
     * Valida se uma string não é nula nem vazia
     */
    public static boolean isValidString(String value) {
        return value != null && !value.trim().isEmpty();
    }
    
    /**
     * Valida se um ID é válido (maior que 0)
     */
    public static boolean isValidId(Integer id) {
        return id != null && id > 0;
    }
    
    /**
     * Sanitiza uma string removendo espaços extras
     */
    public static String sanitizeString(String value) {
        return value != null ? value.trim() : "";
    }
}
