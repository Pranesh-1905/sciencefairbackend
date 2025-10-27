package com.examly.springapp.service;

import com.examly.springapp.Entity.Submission;
import com.examly.springapp.repository.SubmissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class SubmissionService {
    @Autowired
    private SubmissionRepository submissionRepository;

    public Submission createSubmission(Submission submission) {
        return submissionRepository.save(submission);
    }

    public List<Submission> getAllSubmissions() {
        return submissionRepository.findAll();
    }

    public Optional<Submission> getSubmissionById(Long id) {
        return submissionRepository.findById(id);
    }

    public List<Submission> getSubmissionsByProjectId(Long projectId) {
        return submissionRepository.findByProjectId(projectId);
    }

    public Submission updateSubmission(Long id, Submission update) {
        return submissionRepository.findById(id).map(submission -> {
            submission.setFileType(update.getFileType());
            submission.setFilePath(update.getFilePath());
            submission.setFileSize(update.getFileSize());
            submission.setStatus(update.getStatus());
            return submissionRepository.save(submission);
        }).orElse(null);
    }

    public void deleteSubmission(Long id) {
        submissionRepository.deleteById(id);
    }
}
