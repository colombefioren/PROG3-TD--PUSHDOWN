package com.pushdown.repository;

import com.pushdown.model.InvoiceTotal;

import java.util.List;

public interface InvoiceRepository {
    List<InvoiceTotal> findInvoiceTotals();

    List<InvoiceTotal> findConfirmedAndPaidInvoiceTotals();

    InvoiceStatusTotals computeStatusTotals();
}
