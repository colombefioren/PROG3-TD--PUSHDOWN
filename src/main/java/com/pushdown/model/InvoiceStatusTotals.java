package com.pushdown.model;

import java.util.Objects;

public class InvoiceStatusTotals {
    private Double PaidTotal;
    private Double ConfirmedTotal;
    private Double DraftTotal;

    public InvoiceStatusTotals() {
    }

    public InvoiceStatusTotals(Double paidTotal, Double confirmedTotal, Double draftTotal) {
        PaidTotal = paidTotal;
        ConfirmedTotal = confirmedTotal;
        DraftTotal = draftTotal;
    }

    public Double getPaidTotal() {
        return PaidTotal;
    }

    public void setPaidTotal(Double paidTotal) {
        PaidTotal = paidTotal;
    }

    public Double getConfirmedTotal() {
        return ConfirmedTotal;
    }

    public void setConfirmedTotal(Double confirmedTotal) {
        ConfirmedTotal = confirmedTotal;
    }

    public Double getDraftTotal() {
        return DraftTotal;
    }

    public void setDraftTotal(Double draftTotal) {
        DraftTotal = draftTotal;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        InvoiceStatusTotals that = (InvoiceStatusTotals) o;
        return Objects.equals(PaidTotal, that.PaidTotal) && Objects.equals(ConfirmedTotal, that.ConfirmedTotal) && Objects.equals(DraftTotal, that.DraftTotal);
    }

    @Override
    public int hashCode() {
        return Objects.hash(PaidTotal, ConfirmedTotal, DraftTotal);
    }

    @Override
    public String toString() {
        return "InvoiceStatusTotals{" +
                "PaidTotal=" + PaidTotal +
                ", ConfirmedTotal=" + ConfirmedTotal +
                ", DraftTotal=" + DraftTotal +
                '}';
    }
}
