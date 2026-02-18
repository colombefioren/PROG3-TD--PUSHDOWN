package com.pushdown.repository;

import com.pushdown.model.InvoiceTotal;

import java.util.List;

public class DataRetriever implements InvoiceRepository{

    @Override
    public List<InvoiceTotal> findInvoiceTotals() {
        return List.of();
    }
}
