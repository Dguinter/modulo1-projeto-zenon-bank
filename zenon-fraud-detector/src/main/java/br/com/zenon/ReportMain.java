package br.com.zenon;

public class ReportMain {
    void main() {
        String bigFileName = "data/PS_20174392719_1491204439457_log.csv";

        var transactionReport = new TransactionReport();

        IO.println("Iniciando o relatorio original");
        IO.print("Arquivo: " + bigFileName);
        IO.println("-----------------------------------------------------------------------------");
        transactionReport.printSummary(bigFileName);

        IO.println("------------------------------------------------------------------------------");
        IO.print("Relatorio Finalizado com sucesso: ");

    }
}
