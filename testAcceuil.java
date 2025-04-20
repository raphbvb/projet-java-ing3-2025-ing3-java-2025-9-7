package dao;

import model.Hebergement;

import java.sql.*;
import java.util.*;
import java.math.BigDecimal;

/**
 * JDBC implementation of HebergementDAO.
 */
public class JDBCHebergementDAO implements HebergementDAO {

    private final Connection conn;

    public JDBCHebergementDAO(Connection conn) {
        this.conn = conn;
    }

    @Override
    public Hebergement findById(int id) {
        String sql = "SELECT * FROM Hebergement WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Hebergement> findAll() {
        List<Hebergement> list = new ArrayList<>();
        String sql = "SELECT * FROM Hebergement";
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public List<Hebergement> findByVille(String ville) {
        List<Hebergement> list = new ArrayList<>();
        String sql = "SELECT * FROM Hebergement WHERE ville = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, ville);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public List<Hebergement> findByCriteria(String ville, String type, int minEtoiles, BigDecimal maxPrix) {
        List<Hebergement> list = new ArrayList<>();
        String sql = "SELECT * FROM Hebergement WHERE ville = ? AND type = ? AND etoiles >= ? AND prix_base <= ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, ville);
            ps.setString(2, type);
            ps.setInt(3, minEtoiles);
            ps.setBigDecimal(4, maxPrix);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public void create(Hebergement hebergement) {
        String sql = "INSERT INTO Hebergement(nom,type,etoiles,adresse,ville,latitude,longitude,distance_centre_km,description,prix_base) VALUES(?,?,?,?,?,?,?,?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, hebergement.getNom());
            ps.setString(2, hebergement.getType());
            ps.setInt(3, hebergement.getEtoiles());
            ps.setString(4, hebergement.getAdresse());
            ps.setString(5, hebergement.getVille());
            ps.setBigDecimal(6, hebergement.getLatitude());
            ps.setBigDecimal(7, hebergement.getLongitude());
            ps.setBigDecimal(8, hebergement.getDistanceCentreKm());
            ps.setString(9, hebergement.getDescription());
            ps.setBigDecimal(10, hebergement.getPrixBase());
            int affected = ps.executeUpdate();
            if (affected == 0) {
                throw new SQLException("Creating hebergement failed, no rows affected.");
            }
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    hebergement.setId(keys.getInt(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Hebergement hebergement) {
        String sql = "UPDATE Hebergement SET nom=?, type=?, etoiles=?, adresse=?, ville=?, latitude=?, longitude=?, distance_centre_km=?, description=?, prix_base=? WHERE id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, hebergement.getNom());
            ps.setString(2, hebergement.getType());
            ps.setInt(3, hebergement.getEtoiles());
            ps.setString(4, hebergement.getAdresse());
            ps.setString(5, hebergement.getVille());
            ps.setBigDecimal(6, hebergement.getLatitude());
            ps.setBigDecimal(7, hebergement.getLongitude());
            ps.setBigDecimal(8, hebergement.getDistanceCentreKm());
            ps.setString(9, hebergement.getDescription());
            ps.setBigDecimal(10, hebergement.getPrixBase());
            ps.setInt(11, hebergement.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM Hebergement WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Hebergement mapRow(ResultSet rs) throws SQLException {
        Hebergement h = new Hebergement();
        h.setId(rs.getInt("id"));
        h.setNom(rs.getString("nom"));
        h.setType(rs.getString("type"));
        h.setEtoiles(rs.getInt("etoiles"));
        h.setAdresse(rs.getString("adresse"));
        h.setVille(rs.getString("ville"));
        h.setLatitude(rs.getBigDecimal("latitude"));
        h.setLongitude(rs.getBigDecimal("longitude"));
        h.setDistanceCentreKm(rs.getBigDecimal("distance_centre_km"));
        h.setDescription(rs.getString("description"));
        h.setPrixBase(rs.getBigDecimal("prix_base"));
        return h;
    }
}
