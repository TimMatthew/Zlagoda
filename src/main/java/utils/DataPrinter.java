package utils;

import javafx.print.PrinterJob;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;

public class DataPrinter {
    public static void printTableView(TableView<?> tableView) {
        PrinterJob printerJob = PrinterJob.createPrinterJob();
        if (printerJob != null && printerJob.showPrintDialog(tableView.getScene().getWindow())) {
            boolean success = printerJob.printPage(tableView);
            if (success) {
                printerJob.endJob();
            }
        }
    }
}
