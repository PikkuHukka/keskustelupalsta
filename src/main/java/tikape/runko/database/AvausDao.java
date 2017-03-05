/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tikape.runko.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import tikape.runko.domain.Alue;
import tikape.runko.domain.Avaus;
import tikape.runko.domain.Opiskelija;

/**
 *
 * @author oolli
 */
public class AvausDao implements Dao<Avaus, Integer> {

    private Database database;

    public AvausDao(Database database) {
        this.database = database;
    }

    /**
     *
     * @param key
     * @return
     * @throws SQLException
     */
    @Override
    public Avaus findOne(Integer key) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Avaus WHERE avaus_id = ?");
        stmt.setObject(1, key);

        ResultSet rs = stmt.executeQuery();
        boolean hasOne = rs.next();
        if (!hasOne) {
            return null;
        }

        Integer avaus_id = rs.getInt("avaus_id");
        Integer alueviittaus = rs.getInt("alueviittaus");
        String otsikko = rs.getString("otsikko");
        String sisalto = rs.getString("sisalto");
        String nimimerkki = rs.getString("nimimerkki");
       Timestamp aikaleima = rs.getTimestamp("aikaleima");

//(int avaus_id, int alueviittaus, String otsikko, String sisalto, String nimimerkki, Calendar aikaleima
        Avaus o = new Avaus(avaus_id, alueviittaus, otsikko, sisalto, nimimerkki, aikaleima);

        rs.close();
        stmt.close();
        connection.close();
        return o;
    }

    @Override

    public List<Avaus> findAll() throws SQLException {

        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Avaus ORDER BY otsikko");

        ResultSet rs = stmt.executeQuery();
        List<Avaus> avaukset = new ArrayList<>();
        while (rs.next()) {

            Integer avaus_id = rs.getInt("avaus_id");
            Integer alueviittaus = rs.getInt("alueviittaus");
            String otsikko = rs.getString("otsikko");
            String sisalto = rs.getString("sisalto");
            String nimimerkki = rs.getString("nimimerkki");
            Timestamp aikaleima = rs.getTimestamp("aikaleima");

            avaukset.add(new Avaus(avaus_id, alueviittaus, otsikko, sisalto, nimimerkki, aikaleima));
        }

        rs.close();
        stmt.close();
        connection.close();

        return avaukset;
    }
    
    public List<Avaus> findAlue(int alue_id) throws SQLException {

        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Avaus WHERE alueviittaus = ? ORDER BY otsikko");
        stmt.setObject(1, alue_id);
        ResultSet rs = stmt.executeQuery();
        List<Avaus> avaukset = new ArrayList<>();
        while (rs.next()) {

            Integer avaus_id = rs.getInt("avaus_id");
            Integer alueviittaus = rs.getInt("alueviittaus");
            String otsikko = rs.getString("otsikko");
            String sisalto = rs.getString("sisalto");
            String nimimerkki = rs.getString("nimimerkki");
            Timestamp aikaleima = rs.getTimestamp("aikaleima");

            avaukset.add(new Avaus(avaus_id, alueviittaus, otsikko, sisalto, nimimerkki, aikaleima));
        }

        rs.close();
        stmt.close();
        connection.close();

        return avaukset;
    }

    public void lisaaAvaus(Avaus avaus) throws SQLException {

        String sql = "INSERT INTO Avaus(avaus_id, otsikko, sisalto, alueviittaus, aikaleima, nimimerkki) VALUES( " + avaus.getAvaus_id() + ",'" + avaus.getOtsikko() + "', '" + avaus.getSisalto() + "', " + avaus.getAlueviittaus() + ", '" + avaus.getAikaleima() + "', '" + avaus.getNimimerkki() + "')";

        Connection connection = database.getConnection();
        connection.setAutoCommit(false);
        Statement stmt = connection.createStatement();
        stmt.executeUpdate(sql);
        stmt.close();
        connection.commit();
        connection.close();
    }

    @Override
    public void delete(Integer key) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
