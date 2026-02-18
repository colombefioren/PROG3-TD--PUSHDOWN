package com.pushdown.repository;

import com.pushdown.db.DBConnection;
import com.pushdown.model.InvoiceTotal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

public class DataRetriever implements InvoiceRepository{

    private final DBConnection dbConnection;
    public DataRetriever() {
        this.dbConnection = new DBConnection();
    }


    @Override
    public List<InvoiceTotal> findInvoiceTotals() {
       String sql = """
""";

    Connection conn = null;
    PreparedStatement ps = null;
    ResultSet rs = null;

    try{
        conn = db
    }

    }
}
