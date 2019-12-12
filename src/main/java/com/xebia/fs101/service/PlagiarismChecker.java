package com.xebia.fs101.service;

import net.ricecode.similarity.JaroWinklerStrategy;
import net.ricecode.similarity.SimilarityStrategy;
import net.ricecode.similarity.StringSimilarityService;
import net.ricecode.similarity.StringSimilarityServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class PlagiarismChecker {
    @Value("${spring.service.requiredPlagScore}")
    double requiredScore;
    private SimilarityStrategy strategy;
    private StringSimilarityService service;
    @Autowired
    public PlagiarismChecker() {
        this.strategy = new JaroWinklerStrategy();
        this.service = new StringSimilarityServiceImpl(this.strategy);
    }
    public void checkPlagiarism(String source, String target) {
        double score = service.score(source, target);
        if (score > this.requiredScore)
            throw new IllegalArgumentException("Same Content already exists");
    }
}
