
package com.examly.springapp.controller;

import com.examly.springapp.Entity.Evaluation;
import com.examly.springapp.repository.UserRepository;
import com.examly.springapp.service.EvaluationService;
import com.examly.springapp.Entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/evaluations")
public class EvaluationController {
    @Autowired
    private EvaluationService evaluationService;
    @Autowired
    private UserRepository userRepository;

    // JUDGE: Create new evaluation
    @PostMapping
    @PreAuthorize("hasAnyRole('JUDGE','ADMIN')")
    public ResponseEntity<?> createEvaluation(@RequestBody Evaluation evaluation, Principal principal) {
        System.out.println("Received evaluation: " + evaluation);
        System.out.println("Creativity: " + evaluation.getCreativity());
        System.out.println("Methodology: " + evaluation.getMethodology());
        System.out.println("Presentation: " + evaluation.getPresentation());
        
        User judge = userRepository.findByUsername(principal.getName()).orElse(null);
        if (judge == null) return ResponseEntity.status(401).build();
        evaluation.setJudgeId(judge.getId());
        return ResponseEntity.ok(evaluationService.createEvaluation(evaluation));
    }

    // JUDGE/ADMIN/TEACHER/COORDINATOR: List all evaluations
    @GetMapping
    @PreAuthorize("hasAnyRole('JUDGE','ADMIN','TEACHER','FAIR_COORDINATOR','COORDINATOR')")
    public List<Evaluation> getAllEvaluations() {
        return evaluationService.getAllEvaluations();
    }

    // JUDGE/ADMIN/TEACHER/COORDINATOR: Get evaluation by id
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('JUDGE','ADMIN','TEACHER','FAIR_COORDINATOR','COORDINATOR')")
    public ResponseEntity<Evaluation> getEvaluation(@PathVariable Long id) {
        return evaluationService.getEvaluationById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    // JUDGE: Update evaluation
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('JUDGE','ADMIN')")
    public ResponseEntity<?> updateEvaluation(@PathVariable Long id, @RequestBody Evaluation update, Principal principal) {
        User judge = userRepository.findByUsername(principal.getName()).orElse(null);
        if (judge == null) return ResponseEntity.status(401).build();
        Evaluation evaluation = evaluationService.getEvaluationById(id).orElse(null);
        if (evaluation == null) return ResponseEntity.notFound().build();
        if (!judge.getId().equals(evaluation.getJudgeId()) && !judge.getRole().name().equals("ADMIN"))
            return ResponseEntity.status(403).build();
        return ResponseEntity.ok(evaluationService.updateEvaluation(id, update));
    }

    // JUDGE: Get pending evaluations for judge
    @GetMapping("/pending")
    @PreAuthorize("hasAnyRole('JUDGE','ADMIN')")
    public List<Evaluation> getPendingEvaluations(Principal principal) {
        User judge = userRepository.findByUsername(principal.getName()).orElse(null);
        if (judge == null) return List.of();
        return evaluationService.getPendingEvaluationsByJudgeId(judge.getId());
    }

    // ADMIN: Delete evaluation
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteEvaluation(@PathVariable Long id) {
        evaluationService.deleteEvaluation(id);
        return ResponseEntity.ok().build();
    }
}
