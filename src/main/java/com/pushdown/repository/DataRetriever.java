package com.pushdown.repository;

import com.pushdown.db.DBConnection;
import com.pushdown.model.InvoiceStatusTotals;
import com.pushdown.model.InvoiceTaxSummary;
import com.pushdown.model.InvoiceTotal;
import com.pushdown.model.Status;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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
select i.id as inv_id, i.customer_name as inv_customer_name, i.status as inv_status, sum(il.unit_price * il.quantity) as inv_total
from invoice i
join invoice_line il on i.id = il.invoice_id
group by i.id
order by i.id
""";

    Connection conn = null;
    PreparedStatement ps = null;
    ResultSet rs = null;

    try{
        conn = dbConnection.getDBConnection();
        ps = conn.prepareStatement(sql);
        rs = ps.executeQuery();
        List<InvoiceTotal> invoiceTotals = new ArrayList<InvoiceTotal>();
        while(rs.next()){
            invoiceTotals.add(mapResultSetToInvoiceTotal(rs));
        }

        return invoiceTotals;
    } catch (SQLException e) {
        throw new RuntimeException("Failed to get all invoice totals ",e);
    }finally{
        dbConnection.attemptCloseDBConnection(rs,ps,conn);
    }
    }

    @Override
    public List<InvoiceTotal> findConfirmedAndPaidInvoiceTotals() {
        String sql =

                """
                select i.id as inv_id, i.customer_name as inv_customer_name, i.status as inv_status, sum(il.unit_price * il.quantity) as inv_total
                from invoice i
                join invoice_line il on i.id = il.invoice_id
                where i.status = 'CONFIRMED' or i.status = 'PAID'
                group by i.id
                order by i.id
                """;

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try{
            conn = dbConnection.getDBConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            List<InvoiceTotal> invoiceTotals = new ArrayList<InvoiceTotal>();
            while(rs.next()){
                invoiceTotals.add(mapResultSetToInvoiceTotal(rs));
            }

            return invoiceTotals;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to get all invoice totals ",e);
        }finally{
            dbConnection.attemptCloseDBConnection(rs,ps,conn);
        }
    }

    @Override
    public InvoiceStatusTotals computeStatusTotals() {
        String sql =

                """
                select i.status as inv_status, sum(il.unit_price * il.quantity) as inv_total
                from invoice i
                join invoice_line il on i.id = il.invoice_id
                group by i.status
                order by inv_total desc
                """;

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try{
            conn = dbConnection.getDBConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            return mapResultSetToInvoiceStatusTotal(rs);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to get all invoice totals ",e);
        }finally{
            dbConnection.attemptCloseDBConnection(rs,ps,conn);
        }
    }

    @Override
    public Double computeWeightedTurnover(){
        String sql =

                """
                select sum(
                    case
                        when i.status = 'PAID' then il.unit_price * il.quantity
                        when i.status = 'CONFIRMED' then il.unit_price * il.quantity * 0.5
                    end
                     ) as total
                from invoice i
                join invoice_line il on i.id = il.invoice_id
                where i.status = 'CONFIRMED' or i.status = 'PAID'
                """;

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try{
            conn = dbConnection.getDBConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            if(!rs.next()){
                throw new RuntimeException("No invoice found");
            }
            return rs.getObject("total") == null ? null : rs.getDouble("total");
        } catch (SQLException e) {
            throw new RuntimeException("Failed to get all invoice totals ",e);
        }finally{
            dbConnection.attemptCloseDBConnection(rs,ps,conn);
        }
    }

    @Override
    public List<InvoiceTaxSummary> findInvoiceTaxSummaries(){
     String sql = """
    select i.id as inv_id, sum(il.unit_price * il.quantity) as ht_total, sum(il.unit_price * il.quantity * 0.2) as ht_tax, sum(il.unit_price * il.quantity * 1.2) as ttc_total
    from invoice i
    join invoice_line il on i.id = il.invoice_id
    group by i.id
    order by i.id
""";

     Connection conn = null;
     PreparedStatement ps = null;
     ResultSet rs = null;

     try{
         conn = dbConnection.getDBConnection();
         ps = conn.prepareStatement(sql);
         rs = ps.executeQuery();
         List<InvoiceTaxSummary> invoiceTaxSummaries = new ArrayList<>();
         while(rs.next()){
             invoiceTaxSummaries.add(mapResultSetToInvoiceTaxSummary(rs));
         }
         return invoiceTaxSummaries;
     } catch (SQLException e) {
         throw new RuntimeException(e);
     }finally{
         dbConnection.attemptCloseDBConnection(rs,ps,conn);
     }
    }

    @Override
    public BigDecimal computeWeightedTurnedoverTtc(){
        String sql =

                """
                select sum(
                    case
                        when i.status = 'PAID' then il.unit_price * il.quantity * 1.2
                        when i.status = 'CONFIRMED' then il.unit_price * il.quantity * 0.5 * 1.2
                    end
                     ) as total
                from invoice i
                join invoice_line il on i.id = il.invoice_id
                where i.status = 'CONFIRMED' or i.status = 'PAID'
                """;

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try{
            conn = dbConnection.getDBConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            if(!rs.next()){
                throw new RuntimeException("No invoice found");
            }

            return rs.getObject("total") == null ? null : rs.getBigDecimal("total");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }finally{
            dbConnection.attemptCloseDBConnection(rs,ps,conn);
        }

    }


    private InvoiceTotal mapResultSetToInvoiceTotal(ResultSet rs) throws SQLException {
        InvoiceTotal invoiceTotal = new InvoiceTotal();
        invoiceTotal.setId(rs.getInt("inv_id"));
        invoiceTotal.setCustomerName(rs.getString("inv_customer_name"));
        invoiceTotal.setStatus(Status.valueOf(rs.getString("inv_status")));
        invoiceTotal.setTotal(rs.getDouble("inv_total"));
        return invoiceTotal;
    }

    private InvoiceStatusTotals mapResultSetToInvoiceStatusTotal(ResultSet rs) throws SQLException {
        InvoiceStatusTotals invoiceStatusTotals = new InvoiceStatusTotals();
     while(rs.next()){
         if(Status.valueOf(rs.getString("inv_status")).equals(Status.CONFIRMED)){
             invoiceStatusTotals.setConfirmedTotal(rs.getDouble("inv_total"));
         }
         else if(Status.valueOf(rs.getString("inv_status")).equals(Status.PAID)){
             invoiceStatusTotals.setPaidTotal(rs.getDouble("inv_total"));
         }else if (Status.valueOf(rs.getString("inv_status")).equals(Status.DRAFT)){
             invoiceStatusTotals.setDraftTotal(rs.getDouble("inv_total"));
         }

     }
     return invoiceStatusTotals;
    }

    private InvoiceTaxSummary mapResultSetToInvoiceTaxSummary(ResultSet rs) throws SQLException {
        InvoiceTaxSummary invoiceTaxSummary = new InvoiceTaxSummary();
        invoiceTaxSummary.setInvoiceId(rs.getInt("inv_id"));
        invoiceTaxSummary.setTaxValue(rs.getDouble("ht_tax"));
        invoiceTaxSummary.setHtValue(rs.getDouble("ht_total"));
        invoiceTaxSummary.setTtcValue(rs.getDouble("ttc_total"));
        return invoiceTaxSummary;
    }
}
