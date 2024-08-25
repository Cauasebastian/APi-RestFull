package org.sebastiandev.generationbrazil.controller;

import jakarta.validation.Valid;
import org.sebastiandev.generationbrazil.model.Aluno;
import org.sebastiandev.generationbrazil.service.AlunoService;
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

    @Autowired
    private AlunoService alunoService;

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<Aluno>>> getAllAlunos() {
        List<EntityModel<Aluno>> alunos = alunoService.findAll().stream()
                .map(aluno -> EntityModel.of(aluno,
                        linkTo(methodOn(AlunoController.class).getAlunoById(aluno.getId())).withSelfRel(),
                        linkTo(methodOn(AlunoController.class).getAllAlunos()).withRel("alunos")))
                .collect(Collectors.toList());

        return ResponseEntity.ok(CollectionModel.of(alunos,
                linkTo(methodOn(AlunoController.class).getAllAlunos()).withSelfRel()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Aluno>> getAlunoById(@PathVariable long id) {
        Aluno aluno = alunoService.findById(id);
        EntityModel<Aluno> alunoModel = EntityModel.of(aluno,
                linkTo(methodOn(AlunoController.class).getAlunoById(id)).withSelfRel(),
                linkTo(methodOn(AlunoController.class).getAllAlunos()).withRel("alunos"));
        return ResponseEntity.ok(alunoModel);
    }

    @PostMapping
    public ResponseEntity<EntityModel<Aluno>> saveAluno(@Valid @RequestBody Aluno aluno) {
        Aluno savedAluno = alunoService.save(aluno);
        EntityModel<Aluno> alunoModel = EntityModel.of(savedAluno,
                linkTo(methodOn(AlunoController.class).getAlunoById(savedAluno.getId())).withSelfRel(),
                linkTo(methodOn(AlunoController.class).getAllAlunos()).withRel("alunos"));
        return ResponseEntity.ok(alunoModel);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<Aluno>> updateAluno(@PathVariable long id, @Valid @RequestBody Aluno aluno) {
        Aluno updatedAluno = alunoService.update(id, aluno);
        EntityModel<Aluno> alunoModel = EntityModel.of(updatedAluno,
                linkTo(methodOn(AlunoController.class).getAlunoById(updatedAluno.getId())).withSelfRel(),
                linkTo(methodOn(AlunoController.class).getAllAlunos()).withRel("alunos"));
        return ResponseEntity.ok(alunoModel);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAluno(@PathVariable long id) {
        alunoService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<EntityModel<Aluno>> updatePartialAluno(@PathVariable long id, @RequestBody Aluno aluno) {
        Aluno updatedAluno = alunoService.updatePartial(id, aluno);
        EntityModel<Aluno> alunoModel = EntityModel.of(updatedAluno,
                linkTo(methodOn(AlunoController.class).getAlunoById(updatedAluno.getId())).withSelfRel(),
                linkTo(methodOn(AlunoController.class).getAllAlunos()).withRel("alunos"));
        return ResponseEntity.ok(alunoModel);
    }
}
