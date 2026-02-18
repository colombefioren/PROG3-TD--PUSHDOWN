package com.pushdown.repository;

import com.pushdown.db.DBConnection;
import com.pushdown.model.InvoiceTotal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class DataRetriever implements InvoiceRepository{

    private final DBConnection dbConnection;
    public DataRetriever() {
        this.dbConnection = new DBConnection();
    }


    @Override
    public List<InvoiceTotal> findInvoiceTotals() {
       String sql =

"""
select i.id, i.customer_name, i.status, sum(il.unit_price * il.quantity) as total

""";

    Connection conn = null;
    PreparedStatement ps = null;
    ResultSet rs = null;

    try{
        conn = dbConnection.getDBConnection();
    } catch (SQLException e) {
        throw new RuntimeException("Failed to get all invoice totals ",e);
    }finally{
        dbConnection.attemptCloseDBConnection(rs,ps,conn);
    }

    }
}
