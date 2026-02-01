package com.floodapp.flood_impact.service;

import com.floodapp.flood_impact.model.Report;
import com.floodapp.flood_impact.repository.ReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReportService {

    @Autowired
    private ReportRepository reportRepository;

    @Cacheable(value = "reports")
    public List<Report> getAllReports() {
        return reportRepository.findAll();
    }

    @CacheEvict(value = "reports", allEntries = true)
    public Report createReport(Report report) {
        if (report.getStatus() == null || report.getStatus().isEmpty()) {
            report.setStatus("active");
        }
        return reportRepository.save(report);
    }

    @CacheEvict(value = "reports", allEntries = true)
    public Optional<Report> resolveReport(Long id) {
        Optional<Report> reportOpt = reportRepository.findById(id);
        if (reportOpt.isPresent()) {
            Report report = reportOpt.get();
            report.setStatus("resolved");
            reportRepository.save(report);
            return Optional.of(report);
        }
        return Optional.empty();
    }
}
