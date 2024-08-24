package org.sebastiandev.generationbrazil.repository;

import org.sebastiandev.generationbrazil.model.Aluno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlunoRepository extends JpaRepository<Aluno, Long> {

}
