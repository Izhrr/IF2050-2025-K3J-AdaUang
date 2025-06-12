package controllers;

import models.AgingReport;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

public class AgingReportControllerTest {

    AgingReportController controller;

    @BeforeEach
    void setUp() {
        controller = new AgingReportController();
    }

    @Test
    void testGetAgingReportReturnsList() {
        List<AgingReport> reports = controller.getAgingReport();
        assertNotNull(reports, "List aging report tidak boleh null");
        assertTrue(reports.size() >= 0);
        if (!reports.isEmpty()) {
            AgingReport report = reports.get(0);
            assertNotNull(report.getBranch(), "Branch tidak boleh null di report");
        }
    }

    @Test
    void testGetAgingReportByMonthYearReturnsList() {
        java.time.LocalDate now = java.time.LocalDate.now();
        List<AgingReport> reports = controller.getAgingReportByMonthYear(now.getMonthValue(), now.getYear());
        assertNotNull(reports, "List aging report by month/year tidak boleh null");
        assertTrue(reports.size() >= 0);
        if (!reports.isEmpty()) {
            AgingReport report = reports.get(0);
            assertNotNull(report.getBranch(), "Branch tidak boleh null di report");
        }
    }
}