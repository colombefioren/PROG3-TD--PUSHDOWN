package com.pushdown;

import com.pushdown.model.InvoiceStatusTotals;
import com.pushdown.model.InvoiceTaxSummary;
import com.pushdown.model.InvoiceTotal;
import com.pushdown.repository.DataRetriever;

import java.math.BigDecimal;
import java.util.List;

public class Main {
    public static void main(String[] args){
        DataRetriever dataRetriever = new DataRetriever();

    System.out.println("\n===> findInvoiceTotals <===");

    List<InvoiceTotal> invoiceTotals = dataRetriever.findInvoiceTotals();

    System.out.println(invoiceTotals);

    System.out.println("\n===> findConfirmedAndPaidInvoicesTotals <===");
    List<InvoiceTotal> confirmedAndPaidInvoiceTotals = dataRetriever.findConfirmedAndPaidInvoiceTotals();

    System.out.println(confirmedAndPaidInvoiceTotals);

    System.out.println("\n===> computeStatusTotal <===");

        InvoiceStatusTotals invoiceStatusTotals = dataRetriever.computeStatusTotals();

        double totalPaid = invoiceStatusTotals.getPaidTotal();
        double totalConfirmed = invoiceStatusTotals.getConfirmedTotal();
        double totalDraft = invoiceStatusTotals.getDraftTotal();

        System.out.println("total_paid = " + totalPaid);
        System.out.println("total_confirmed = " + totalConfirmed);
        System.out.println("total_draft = " + totalDraft);

        System.out.println("\n===> findInvoiceTaxSummaries <===");

        List<InvoiceTaxSummary> invoiceTaxSummaries = dataRetriever.findInvoiceTaxSummaries();

        for (InvoiceTaxSummary invoiceTaxSummary : invoiceTaxSummaries) {
            System.out.println(invoiceTaxSummary.getInvoiceId() + " | HT " + invoiceTaxSummary.getHtValue() + " | TVA " + invoiceTaxSummary.getTaxValue() + " | TTC " + invoiceTaxSummary.getTtcValue() );
        }

    System.out.println("\n===> computeWeightedTurnedoverTtc <===");

        BigDecimal turnoverTtc = dataRetriever.computeWeightedTurnedoverTtc();

        System.out.println("turnoverTtc = " + turnoverTtc);
  }
}
