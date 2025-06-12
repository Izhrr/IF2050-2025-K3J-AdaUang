package models;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

public class AgingReportTest {

    @Test
    void testAgingReportGetterSetter() {
        AgingReport report = new AgingReport();
        report.setBranch("Bandung");
        report.setTotalNasabah(10);
        report.setAging1to30(10000);
        report.setAging31to60(20000);
        report.setAging61to90(30000);
        report.setAgingOver90(40000);

        assertEquals("Bandung", report.getBranch());
        assertEquals(10, report.getTotalNasabah());
        assertEquals(10000, report.getAging1to30());
        assertEquals(20000, report.getAging31to60());
        assertEquals(30000, report.getAging61to90());
        assertEquals(40000, report.getAgingOver90());
    }

    @Test
    void testGetAgingReportByBranchReturnsList() {
        List<AgingReport> reports = AgingReport.getAgingReportByBranch();
        assertNotNull(reports, "List hasil query tidak boleh null");
        assertTrue(reports.size() >= 0);
        if (!reports.isEmpty()) {
            AgingReport r = reports.get(0);
            assertNotNull(r.getBranch());
        }
    }

    @Test
    void testGetAgingReportByBranchAndDateReturnsList() {
        java.time.LocalDate now = java.time.LocalDate.now();
        List<AgingReport> reports = AgingReport.getAgingReportByBranchAndDate(now.getMonthValue(), now.getYear());
        assertNotNull(reports, "List hasil query dengan tanggal tidak boleh null");
        assertTrue(reports.size() >= 0);
        if (!reports.isEmpty()) {
            AgingReport r = reports.get(0);
            assertNotNull(r.getBranch());
        }
    }

    @Test
    void testGetAgingReportSummary() {
        AgingReport summary = AgingReport.getAgingReportSummary();
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
        AgingReport summary = AgingReport.getAgingReportSummaryByDate(now.getMonthValue(), now.getYear());
        assertNotNull(summary, "Summary aging report by month/year tidak boleh null");
        assertEquals("TOTAL SEMUA CABANG", summary.getBranch());
        assertTrue(summary.getTotalNasabah() >= 0);
        assertTrue(summary.getAging1to30() >= 0);
        assertTrue(summary.getAging31to60() >= 0);
        assertTrue(summary.getAging61to90() >= 0);
        assertTrue(summary.getAgingOver90() >= 0);
    }

    @Test
    void testDebugAgingDataDoesNotThrow() {
        assertDoesNotThrow(() -> AgingReport.debugAgingData());
    }
}