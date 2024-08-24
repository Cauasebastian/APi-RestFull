package org.sebastiandev.generationbrazil.controller;

import org.sebastiandev.generationbrazil.model.Aluno;
import org.sebastiandev.generationbrazil.service.AlunoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/alunos")
public class AlunoController {

    @Autowired
    private AlunoService alunoService;

    @GetMapping
    public ResponseEntity<List<Aluno>> getAllProducts(){
        if(alunoService.findAll().isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(alunoService.findAll());
    }
    @GetMapping("/{id}")
    public ResponseEntity<Aluno> getProductById(@PathVariable long id){
        Aluno aluno = alunoService.findById(id);
        if(aluno == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(aluno);
    }
    @PostMapping
    public ResponseEntity<Aluno> saveProduct(@RequestBody Aluno aluno){
        if(aluno == null){
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(alunoService.save(aluno));
    }
    @PutMapping("/{id}")
    public ResponseEntity<Aluno> updateProduct(@PathVariable long id, @RequestBody Aluno aluno){
        if(aluno == null){
            return ResponseEntity.badRequest().build();
        }
        Aluno updatedAluno = alunoService.update(id, aluno);
        if(updatedAluno == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedAluno);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable long id){
        alunoService.delete(id);
        if(alunoService.findById(id) == null){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
    @PatchMapping("/{id}")
    public ResponseEntity<Aluno> updatePartialProduct(@PathVariable long id, @RequestBody Aluno aluno){
        if(aluno == null){
            return ResponseEntity.badRequest().build();
        }
        Aluno updatedAluno = alunoService.updatePartial(id, aluno);
        if(updatedAluno == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedAluno);
    }
}
