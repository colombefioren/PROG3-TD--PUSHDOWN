package com.pushdown.model;

import java.util.Objects;

public class InvoiceStatusTotals {
    private Status status;
    private Double total;

    public InvoiceStatusTotals() {
    }

    public InvoiceStatusTotals(Status status, Double total) {
        this.status = status;
        this.total = total;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    @Override
    public boolean equals(Object o) {

        if (o == null || getClass() != o.getClass()) return false;
        InvoiceStatusTotals that = (InvoiceStatusTotals) o;
        return status == that.status && Objects.equals(total, that.total);
    }

    @Override
    public int hashCode() {
        return Objects.hash(status, total);
    }

    @Override
    public String toString() {
        return "InvoiceStatusTotals{" +
                "status=" + status +
                ", total=" + total +
                '}';
    }
}
