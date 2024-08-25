package org.sebastiandev.generationbrazil.service;

import org.sebastiandev.generationbrazil.exception.AlunoBadRequestException;
import org.sebastiandev.generationbrazil.exception.AlunoNotFoundException;
import org.sebastiandev.generationbrazil.model.Aluno;
import org.sebastiandev.generationbrazil.repository.AlunoRepository;
import org.sebastiandev.generationbrazil.service.AlunoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AlunoServiceImpl implements AlunoService {

    private static final Logger logger = LoggerFactory.getLogger(AlunoServiceImpl.class);

    @Autowired
    private AlunoRepository alunoRepository;

    @Override
    @Cacheable("alunos")
    public List<Aluno> findAll() {
        logger.info("Fetching all students from the database");
        List<Aluno> alunos = alunoRepository.findAll();
        if (alunos.isEmpty()) {
            logger.warn("No students found");
            throw new AlunoNotFoundException("No students found");
        }
        return alunos;
    }

    @Override
    @Cacheable(value = "aluno", key = "#id")
    public Aluno findById(long id) {
        logger.info("Fetching student with ID: {}", id);
        return alunoRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Student with ID: {} not found", id);
                    return new AlunoNotFoundException(id);
                });
    }

    @Override
    @CacheEvict(value = "alunos", allEntries = true)
    @CachePut(value = "aluno", key = "#result.id")
    public Aluno save(Aluno aluno) {
        if (aluno == null || aluno.getNome() == null || aluno.getNome().isEmpty()) {
            logger.error("Invalid student data: {}", aluno);
            throw new AlunoBadRequestException("Aluno data is invalid");
        }
        logger.info("Saving new student: {}", aluno.getNome());
        return alunoRepository.save(aluno);
    }

    @Override
    @CacheEvict(value = {"aluno", "alunos"}, key = "#id", allEntries = true)
    public Aluno update(long id, Aluno aluno) {
        if (!alunoRepository.existsById(id)) {
            logger.warn("Attempt to update non-existent student with ID: {}", id);
            throw new AlunoNotFoundException(id);
        }
        aluno.setId(id);
        logger.info("Updating student with ID: {}", id);
        return alunoRepository.save(aluno);
    }

    @Override
    @CacheEvict(value = {"aluno", "alunos"}, key = "#id", allEntries = true)
    public void delete(long id) {
        if (!alunoRepository.existsById(id)) {
            logger.warn("Attempt to delete non-existent student with ID: {}", id);
            throw new AlunoNotFoundException(id);
        }
        logger.info("Deleting student with ID: {}", id);
        alunoRepository.deleteById(id);
    }

    @Override
    @CachePut(value = "aluno", key = "#id")
    @CacheEvict(value = "alunos", allEntries = true)
    public Aluno updatePartial(long id, Aluno aluno) {
        logger.info("Partially updating student with ID: {}", id);
        Aluno alunoAtualizado = alunoRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Student with ID: {} not found for partial update", id);
                    return new AlunoNotFoundException(id);
                });

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
