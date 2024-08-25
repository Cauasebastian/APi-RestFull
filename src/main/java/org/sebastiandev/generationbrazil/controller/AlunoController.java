package org.sebastiandev.generationbrazil.controller;

import jakarta.validation.Valid;
import org.sebastiandev.generationbrazil.model.Aluno;
import org.sebastiandev.generationbrazil.service.AlunoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/alunos")
public class AlunoController {

    private static final Logger logger = LoggerFactory.getLogger(AlunoController.class);

    @Autowired
    private AlunoService alunoService;

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<Aluno>>> getAllAlunos() {
        logger.info("Fetching all students");
        List<EntityModel<Aluno>> alunos = alunoService.findAll().stream()
                .map(aluno -> EntityModel.of(aluno,
                        linkTo(methodOn(AlunoController.class).getAlunoById(aluno.getId())).withSelfRel(),
                        linkTo(methodOn(AlunoController.class).getAllAlunos()).withRel("alunos")))
                .collect(Collectors.toList());

        logger.debug("Returning {} students", alunos.size());
        return ResponseEntity.ok(CollectionModel.of(alunos,
                linkTo(methodOn(AlunoController.class).getAllAlunos()).withSelfRel()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Aluno>> getAlunoById(@PathVariable long id) {
        logger.info("Fetching student with ID: {}", id);
        Aluno aluno = alunoService.findById(id);
        EntityModel<Aluno> alunoModel = EntityModel.of(aluno,
                linkTo(methodOn(AlunoController.class).getAlunoById(id)).withSelfRel(),
                linkTo(methodOn(AlunoController.class).getAllAlunos()).withRel("alunos"));
        return ResponseEntity.ok(alunoModel);
    }

    @PostMapping
    public ResponseEntity<EntityModel<Aluno>> saveAluno(@Valid @RequestBody Aluno aluno) {
        logger.info("Saving new student: {}", aluno.getNome());
        Aluno savedAluno = alunoService.save(aluno);
        EntityModel<Aluno> alunoModel = EntityModel.of(savedAluno,
                linkTo(methodOn(AlunoController.class).getAlunoById(savedAluno.getId())).withSelfRel(),
                linkTo(methodOn(AlunoController.class).getAllAlunos()).withRel("alunos"));
        return ResponseEntity.ok(alunoModel);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<Aluno>> updateAluno(@PathVariable long id, @Valid @RequestBody Aluno aluno) {
        logger.info("Updating student with ID: {}", id);
        Aluno updatedAluno = alunoService.update(id, aluno);
        EntityModel<Aluno> alunoModel = EntityModel.of(updatedAluno,
                linkTo(methodOn(AlunoController.class).getAlunoById(updatedAluno.getId())).withSelfRel(),
                linkTo(methodOn(AlunoController.class).getAllAlunos()).withRel("alunos"));
        return ResponseEntity.ok(alunoModel);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAluno(@PathVariable long id) {
        logger.info("Deleting student with ID: {}", id);
        alunoService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<EntityModel<Aluno>> updatePartialAluno(@PathVariable long id, @RequestBody Aluno aluno) {
        logger.info("Partially updating student with ID: {}", id);
        Aluno updatedAluno = alunoService.updatePartial(id, aluno);
        EntityModel<Aluno> alunoModel = EntityModel.of(updatedAluno,
                linkTo(methodOn(AlunoController.class).getAlunoById(updatedAluno.getId())).withSelfRel(),
                linkTo(methodOn(AlunoController.class).getAllAlunos()).withRel("alunos"));
        return ResponseEntity.ok(alunoModel);
    }
}
