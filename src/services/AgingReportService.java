package services;

import models.AgingReport;
import java.util.List;

public class AgingReportService {
    
    public List<AgingReport> getAgingReportByBranch() {
        return AgingReport.getAgingReportByBranch();
    }

    public List<AgingReport> getAgingReportByBranchAndDate(int month, int year) {
        return AgingReport.getAgingReportByBranchAndDate(month, year);
    }
}