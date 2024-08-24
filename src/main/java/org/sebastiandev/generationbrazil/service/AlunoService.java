package org.sebastiandev.generationbrazil.service;

import org.sebastiandev.generationbrazil.model.Aluno;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AlunoService {
    List<Aluno> findAll();

    Aluno findById(long id);

    Aluno save(Aluno aluno);

    Aluno update(long id, Aluno aluno);

    void delete(long id);

    Aluno updatePartial(long id, Aluno aluno);
}
