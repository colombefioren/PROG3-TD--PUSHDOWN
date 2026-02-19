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
        String sql1 =

                """
                select i.status as inv_status, sum(il.unit_price * il.quantity) as inv_total
                from invoice i
                join invoice_line il on i.id = il.invoice_id
                group by i.status
                order by inv_total desc
                """;

        String sql = """
        select
        coalesce(sum(case when i.status = 'PAID' then il.unit_price * il.quantity end),0) as paid_total,
        coalesce(sum(case when i.status = 'CONFIRMED' then il.unit_price * il.quantity end),0) as confirmed_total,
        coalesce(sum(case when i.status = 'DRAFT' then il.unit_price * il.quantity end),0) as draft_total
        from invoice i
        join invoice_line il on i.id = il.invoice_id
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
    select i.id as inv_id, sum(il.unit_price * il.quantity) as ht_total, sum(il.unit_price * il.quantity * (tc.rate / 100)) as ht_tax, sum(il.unit_price * il.quantity * (1 + (tc.rate /100))) as ttc_total
    from invoice i
    join invoice_line il on i.id = il.invoice_id
    join tax_config tc on tc.id = i.tax_config_id
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
                        when i.status = 'PAID' then il.unit_price * il.quantity * (1 + (tc.rate / 100))
                        when i.status = 'CONFIRMED' then il.unit_price * il.quantity * 0.5 * (1 + (tc.rate / 100))
                    end
                     ) as total
                from invoice i
                join invoice_line il on i.id = il.invoice_id
                join tax_config tc on tc.id = i.tax_config_id
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
             invoiceStatusTotals.setConfirmedTotal(rs.getDouble("confirmed_total"));
             invoiceStatusTotals.setPaidTotal(rs.getDouble("paid_total"));
             invoiceStatusTotals.setDraftTotal(rs.getDouble("draft_total"));
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
