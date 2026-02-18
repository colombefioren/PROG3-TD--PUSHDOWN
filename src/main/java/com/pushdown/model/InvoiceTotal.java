package com.pushdown.model;

import java.util.Objects;

public class InvoiceTotal {
    private Integer id;
    private String customerName;
    private Status status;
    private Double total;

    public InvoiceTotal() {
    }

    public InvoiceTotal(Integer id, String customerName, Status status,Double total) {
        this.id = id;
        this.customerName = customerName;
        this.status = status;
        this.total = total;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        InvoiceTotal that = (InvoiceTotal) o;
        return Objects.equals(id, that.id) && Objects.equals(customerName, that.customerName) && status == that.status && Objects.equals(total, that.total);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, customerName, status, total);
    }

    @Override
    public String toString() {
        return "InvoiceTotal{" +
                "id=" + id +
                ", customerName='" + customerName + '\'' +
                ", status=" + status +
                ", total=" + total +
                '}';
    }
}
