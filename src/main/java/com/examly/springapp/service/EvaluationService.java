package com.examly.springapp.service;

import com.examly.springapp.Entity.Evaluation;
import com.examly.springapp.repository.EvaluationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class EvaluationService {
    @Autowired
    private EvaluationRepository evaluationRepository;

    public Evaluation createEvaluation(Evaluation evaluation) {
        // Calculate total score with null safety
        double creativity = evaluation.getCreativity() != null ? evaluation.getCreativity() : 0.0;
        double methodology = evaluation.getMethodology() != null ? evaluation.getMethodology() : 0.0;
        double presentation = evaluation.getPresentation() != null ? evaluation.getPresentation() : 0.0;
        double total = creativity + methodology + presentation;
        evaluation.setTotalScore(total);
        return evaluationRepository.save(evaluation);
    }

    public List<Evaluation> getAllEvaluations() {
        return evaluationRepository.findAll();
    }

    public Optional<Evaluation> getEvaluationById(Long id) {
        return evaluationRepository.findById(id);
    }

    public List<Evaluation> getEvaluationsByJudgeId(Long judgeId) {
        return evaluationRepository.findByJudgeId(judgeId);
    }

    public List<Evaluation> getPendingEvaluationsByJudgeId(Long judgeId) {
        return evaluationRepository.findByJudgeIdAndTotalScoreIsNull(judgeId);
    }

    public Evaluation updateEvaluation(Long id, Evaluation update) {
        return evaluationRepository.findById(id).map(evaluation -> {
            evaluation.setCreativity(update.getCreativity());
            evaluation.setMethodology(update.getMethodology());
            evaluation.setPresentation(update.getPresentation());
            evaluation.setTotalScore(update.getCreativity() + update.getMethodology() + update.getPresentation());
            evaluation.setFeedback(update.getFeedback());
            evaluation.setEvaluationDate(update.getEvaluationDate());
            return evaluationRepository.save(evaluation);
        }).orElse(null);
    }

    public void deleteEvaluation(Long id) {
        evaluationRepository.deleteById(id);
    }
}
