package com.examly.springapp.repository;

import com.examly.springapp.Entity.Evaluation;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface EvaluationRepository extends JpaRepository<Evaluation, Long> {
    List<Evaluation> findByJudgeId(Long judgeId);
    List<Evaluation> findByProjectId(Long projectId);
    List<Evaluation> findByJudgeIdAndTotalScoreIsNull(Long judgeId); // pending
}
