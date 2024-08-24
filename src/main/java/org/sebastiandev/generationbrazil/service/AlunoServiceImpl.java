package org.sebastiandev.generationbrazil.service;

import org.sebastiandev.generationbrazil.model.Aluno;
import org.sebastiandev.generationbrazil.repository.AlunoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AlunoServiceImpl implements AlunoService {

    @Autowired
    private AlunoRepository alunoRepository;

    @Override
    public List<Aluno> findAll() {
        Optional<List<Aluno>> alunos = Optional.ofNullable(alunoRepository.findAll());
        return alunos.orElse(null);
    }

    @Override
    public Aluno findById(long id) {
        Optional<Aluno> aluno = alunoRepository.findById(id);
        return aluno.orElse(null);
    }

    @Override
    public Aluno save(Aluno aluno) {
        return alunoRepository.save(aluno);
    }

    @Override
    public Aluno update(long id, Aluno aluno) {
        if(alunoRepository.existsById(id)) {
            aluno.setId(id);
            return alunoRepository.save(aluno);
        }
        return null;
    }

    @Override
    public void delete(long id) {
        alunoRepository.deleteById(id);
    }

    @Override
    public Aluno updatePartial(long id, Aluno aluno) {
        Optional<Aluno> alunoOptional = alunoRepository.findById(id);
        if(alunoOptional.isPresent()){
            Aluno alunoAtualizado = alunoOptional.get();
            if(aluno.getNome() != null){
                alunoAtualizado.setNome(aluno.getNome());
            }
            if(aluno.getNotaSemestre1() != null){
                alunoAtualizado.setNotaSemestre1(aluno.getNotaSemestre1());
            }
            if(aluno.getNotaSemestre2() != null){
                alunoAtualizado.setNotaSemestre2(aluno.getNotaSemestre2());
            }
            if(aluno.getNomeProfessor() != null){
                alunoAtualizado.setNomeProfessor(aluno.getNomeProfessor());
            }
            if(aluno.getNumeroSala() != 0){
                alunoAtualizado.setNumeroSala(aluno.getNumeroSala());
            }
            return alunoRepository.save(alunoAtualizado);
        }
        return null;
    }
}