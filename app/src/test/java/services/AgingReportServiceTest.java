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

    @Test
    void testGetAgingReportSummary() {
        AgingReport summary = service.getAgingReportSummary();
        assertNotNull(summary, "Summary aging report tidak boleh null");
        assertEquals("TOTAL SEMUA CABANG", summary.getBranch());
        assertTrue(summary.getTotalNasabah() >= 0);
        assertTrue(summary.getAging1to30() >= 0);
        assertTrue(summary.getAging31to60() >= 0);
        assertTrue(summary.getAging61to90() >= 0);
        assertTrue(summary.getAgingOver90() >= 0);
    }

    @Test
    void testGetAgingReportSummaryByDate() {
        java.time.LocalDate now = java.time.LocalDate.now();
        AgingReport summary = service.getAgingReportSummaryByDate(now.getMonthValue(), now.getYear());
        assertNotNull(summary, "Summary aging report by month/year tidak boleh null");
        assertEquals("TOTAL SEMUA CABANG", summary.getBranch());
        assertTrue(summary.getTotalNasabah() >= 0);
        assertTrue(summary.getAging1to30() >= 0);
        assertTrue(summary.getAging31to60() >= 0);
        assertTrue(summary.getAging61to90() >= 0);
        assertTrue(summary.getAgingOver90() >= 0);
    }
}