package org.sebastiandev.generationbrazil.service;

import jakarta.validation.Valid;
import org.sebastiandev.generationbrazil.exception.AlunoBadRequestException;
import org.sebastiandev.generationbrazil.exception.AlunoNotFoundException;
import org.sebastiandev.generationbrazil.model.Aluno;
import org.sebastiandev.generationbrazil.repository.AlunoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
public class AlunoServiceImpl implements AlunoService {

    @Autowired
    private AlunoRepository alunoRepository;

    @Override
    public List<Aluno> findAll() {
        List<Aluno> alunos = alunoRepository.findAll();
        if (alunos.isEmpty()) {
            throw new AlunoNotFoundException("No students found");
        }
        return alunos;
    }

    @Override
    public Aluno findById(long id) {
        return alunoRepository.findById(id)
                .orElseThrow(() -> new AlunoNotFoundException(id));
    }

    @Override
    public Aluno save(@Valid Aluno aluno) {
        if (aluno == null || aluno.getNome() == null || aluno.getNome().isEmpty()) {
            throw new AlunoBadRequestException("Aluno data is invalid");
        }
        return alunoRepository.save(aluno);
    }

    @Override
    public Aluno update(long id, @Valid Aluno aluno) {
        if (!alunoRepository.existsById(id)) {
            throw new AlunoNotFoundException(id);
        }
        aluno.setId(id);
        return alunoRepository.save(aluno);
    }

    @Override
    public void delete(long id) {
        if (!alunoRepository.existsById(id)) {
            throw new AlunoNotFoundException(id);
        }
        alunoRepository.deleteById(id);
    }

    @Override
    public Aluno updatePartial(long id, Aluno aluno) {
        Aluno alunoAtualizado = alunoRepository.findById(id)
                .orElseThrow(() -> new AlunoNotFoundException(id));

        if (aluno.getNome() != null) {
            alunoAtualizado.setNome(aluno.getNome());
        }
        if (aluno.getNotaSemestre1() != null) {
            alunoAtualizado.setNotaSemestre1(aluno.getNotaSemestre1());
        }
        if (aluno.getNotaSemestre2() != null) {
            alunoAtualizado.setNotaSemestre2(aluno.getNotaSemestre2());
        }
        if (aluno.getNomeProfessor() != null) {
            alunoAtualizado.setNomeProfessor(aluno.getNomeProfessor());
        }
        if (aluno.getNumeroSala() != 0) {
            alunoAtualizado.setNumeroSala(aluno.getNumeroSala());
        }
        return alunoRepository.save(alunoAtualizado);
    }
}
