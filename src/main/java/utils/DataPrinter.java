package utils;

import javafx.print.PrinterJob;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;

import javax.swing.*;
import java.io.InputStream;
import java.sql.Connection;
import java.util.Map;

public class DataPrinter {
    private static JasperViewer jasperViewer;
    private static JasperReport jasperReport;
    private static JasperPrint jasperPrint;

    public static void createReport(Connection connect, Map<String, Object> map, InputStream by) {
        try {
            jasperReport = (JasperReport) JRLoader.loadObject(by);
            jasperPrint = JasperFillManager.fillReport(jasperReport, map, connect);

        } catch (JRException e) {
            throw new RuntimeException(e);
        }
    }

    public static void showReport() {
        jasperViewer = new JasperViewer(jasperPrint, false);
        jasperViewer.setVisible(true);
    }
}
