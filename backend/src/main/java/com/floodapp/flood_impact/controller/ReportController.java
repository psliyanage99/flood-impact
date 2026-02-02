package com.floodapp.flood_impact.controller;

import com.floodapp.flood_impact.model.Report;
import com.floodapp.flood_impact.repository.ReportRepository;
import com.floodapp.flood_impact.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/reports")
@CrossOrigin(origins = {"http://localhost:5173", "https://floodimpact.online"})
public class ReportController {
    @Autowired
    private ReportService reportService;

    // Get All Reports
    @GetMapping
    public List<Report> getAllReports() {
        // This call is now cached!
        return reportService.getAllReports();
    }

    // Create Report
    @PostMapping
    public Report createReport(@RequestBody Report report) {
        return reportService.createReport(report);
    }

    // --- NEW: Resolve Report (Removes from Map, Updates Stats) ---
    @PutMapping("/{id}/resolve")
    public ResponseEntity<Report> resolveReport(@PathVariable Long id) {
        return reportService.resolveReport(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}