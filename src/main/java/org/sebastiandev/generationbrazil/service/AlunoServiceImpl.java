package org.sebastiandev.generationbrazil.service;

import org.sebastiandev.generationbrazil.exception.AlunoBadRequestException;
import org.sebastiandev.generationbrazil.exception.AlunoNotFoundException;
import org.sebastiandev.generationbrazil.model.Aluno;
import org.sebastiandev.generationbrazil.repository.AlunoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AlunoServiceImpl implements AlunoService {

    @Autowired
    private AlunoRepository alunoRepository;

    @Override
    @Cacheable("alunos")
    public List<Aluno> findAll() {
        List<Aluno> alunos = alunoRepository.findAll();
        if (alunos.isEmpty()) {
            throw new AlunoNotFoundException("No students found");
        }
        return alunos;
    }

    @Override
    @Cacheable(value = "aluno", key = "#id")
    public Aluno findById(long id) {
        return alunoRepository.findById(id)
                .orElseThrow(() -> new AlunoNotFoundException(id));
    }

    @Override
    @CacheEvict(value = "alunos", allEntries = true)
    @CachePut(value = "aluno", key = "#result.id")
    public Aluno save(Aluno aluno) {
        if (aluno == null || aluno.getNome() == null || aluno.getNome().isEmpty()) {
            throw new AlunoBadRequestException("Aluno data is invalid");
        }
        return alunoRepository.save(aluno);
    }

    @Override
    @CacheEvict(value = {"aluno", "alunos"}, key = "#id", allEntries = true)
    public Aluno update(long id, Aluno aluno) {
        if (!alunoRepository.existsById(id)) {
            throw new AlunoNotFoundException(id);
        }
        aluno.setId(id);
        return alunoRepository.save(aluno);
    }

    @Override
    @CacheEvict(value = {"aluno", "alunos"}, key = "#id", allEntries = true)
    public void delete(long id) {
        if (!alunoRepository.existsById(id)) {
            throw new AlunoNotFoundException(id);
        }
        alunoRepository.deleteById(id);
    }

    @Override
    @CachePut(value = "aluno", key = "#id")
    @CacheEvict(value = "alunos", allEntries = true)
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
