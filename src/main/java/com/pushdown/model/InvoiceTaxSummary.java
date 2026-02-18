package com.pushdown.model;

import java.util.Objects;

public class InvoiceTaxSummary {
    private Integer invoiceId;
    private Double htValue;
    private Double taxValue;
    private Double ttcValue;

    public InvoiceTaxSummary() {
    }

    public InvoiceTaxSummary(Integer invoiceId, Double htValue, Double taxValue, Double ttcValue) {
        this.invoiceId = invoiceId;
        this.htValue = htValue;
        this.taxValue = taxValue;
        this.ttcValue = ttcValue;
    }

    public Integer getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(Integer invoiceId) {
        this.invoiceId = invoiceId;
    }

    public Double getHtValue() {
        return htValue;
    }

    public void setHtValue(Double htValue) {
        this.htValue = htValue;
    }

    public Double getTaxValue() {
        return taxValue;
    }

    public void setTaxValue(Double taxValue) {
        this.taxValue = taxValue;
    }

    public Double getTtcValue() {
        return ttcValue;
    }

    public void setTtcValue(Double ttcValue) {
        this.ttcValue = ttcValue;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        InvoiceTaxSummary that = (InvoiceTaxSummary) o;
        return Objects.equals(invoiceId, that.invoiceId) && Objects.equals(htValue, that.htValue) && Objects.equals(taxValue, that.taxValue) && Objects.equals(ttcValue, that.ttcValue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(invoiceId, htValue, taxValue, ttcValue);
    }

    @Override
    public String toString() {
        return "InvoiceTaxSummary{" +
                "invoiceId=" + invoiceId +
                ", htValue=" + htValue +
                ", taxValue=" + taxValue +
                ", ttcValue=" + ttcValue +
                '}';
    }
}
