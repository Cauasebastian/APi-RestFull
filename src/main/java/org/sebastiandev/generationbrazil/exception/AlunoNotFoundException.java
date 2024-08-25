package org.sebastiandev.generationbrazil.exception;


public class AlunoNotFoundException extends RuntimeException {
    public AlunoNotFoundException(Long id) {
        super("Aluno not found with id: " + id);
    }
    // Construtor que aceita uma String (para mensagens personalizadas)
    public AlunoNotFoundException(String message) {
        super(message);
    }
}