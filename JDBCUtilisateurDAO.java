package dao;

import model.Utilisateur;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JDBCUtilisateurDAO implements UtilisateurDAO {
    private Connection conn;

    public JDBCUtilisateurDAO(Connection conn) {
        this.conn = conn;
    }

    @Override
    public Utilisateur findById(int id) {
        // TODO : implémentation JDBC pour retrouver un utilisateur par ID
        return null;
    }

    @Override
    public List<Utilisateur> findAll() {
        // TODO : implémentation JDBC pour récupérer tous les utilisateurs
        return new ArrayList<>();
    }

    @Override
    public void create(Utilisateur utilisateur) {
        // TODO : implémentation JDBC pour créer un nouvel utilisateur
    }

    @Override
    public void update(Utilisateur utilisateur) {
        // TODO : implémentation JDBC pour mettre à jour un utilisateur
    }

    @Override
    public void delete(int id) {
        // TODO : implémentation JDBC pour supprimer un utilisateur
    }
}
