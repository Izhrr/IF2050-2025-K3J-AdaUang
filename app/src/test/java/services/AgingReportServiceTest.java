package services;

import models.AgingReport;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

public class AgingReportServiceTest {

    AgingReportService service;

    @BeforeEach
    void setUp() {
        service = new AgingReportService();
    }

    @Test
    void testGetAgingReportByBranchReturnsList() {
        List<AgingReport> reports = service.getAgingReportByBranch();
        assertNotNull(reports, "List aging report tidak boleh null");
        assertTrue(reports.size() >= 0);
        if (!reports.isEmpty()) {
            AgingReport report = reports.get(0);
            assertNotNull(report.getBranch(), "Branch tidak boleh null di report");
        }
    }

    @Test
    void testGetAgingReportByBranchAndDateReturnsList() {
        java.time.LocalDate now = java.time.LocalDate.now();
        List<AgingReport> reports = service.getAgingReportByBranchAndDate(now.getMonthValue(), now.getYear());
        assertNotNull(reports, "List aging report by month/year tidak boleh null");
        assertTrue(reports.size() >= 0);
        if (!reports.isEmpty()) {
            AgingReport report = reports.get(0);
            assertNotNull(report.getBranch(), "Branch tidak boleh null di report");
        }
    }
}