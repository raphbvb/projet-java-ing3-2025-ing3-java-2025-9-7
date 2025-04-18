DROP DATABASE IF EXISTS Booking;
CREATE DATABASE Booking CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE Booking;

-- 1. Utilisateurs (client / admin)
CREATE TABLE Utilisateur (
                             id INT AUTO_INCREMENT PRIMARY KEY,
                             nom VARCHAR(50)    NOT NULL,
                             prenom VARCHAR(50),
                             email VARCHAR(100) NOT NULL UNIQUE,
                             mot_de_passe VARCHAR(255) NOT NULL,
                             role ENUM('client','admin') NOT NULL DEFAULT 'client'
);

-- 2. Hébergements
CREATE TABLE Hebergement (
                             id INT AUTO_INCREMENT PRIMARY KEY,
                             nom VARCHAR(100)    NOT NULL,
                             type ENUM('hôtel','appartement','villa','chalet','complexe') NOT NULL,
                             etoiles TINYINT CHECK (etoiles BETWEEN 1 AND 5),
                             adresse VARCHAR(255) NOT NULL,
                             ville VARCHAR(100)  NOT NULL,
                             latitude  DECIMAL(9,6),
                             longitude DECIMAL(9,6),
                             distance_centre_km DECIMAL(5,2),
                             description TEXT,
                             prix_base DECIMAL(10,2) NOT NULL
);

-- 3. Types de chambre
CREATE TABLE ChambreType (
                             id INT AUTO_INCREMENT PRIMARY KEY,
                             id_hebergement INT NOT NULL,
                             nom VARCHAR(50)    NOT NULL,
                             capacite INT       NOT NULL,
                             prix     DECIMAL(10,2) NOT NULL,
                             options  TEXT,
                             FOREIGN KEY (id_hebergement) REFERENCES Hebergement(id) ON DELETE CASCADE
);

-- 4. Disponibilités jour par jour
CREATE TABLE Disponibilite (
                               id INT AUTO_INCREMENT PRIMARY KEY,
                               id_chambre_type INT NOT NULL,
                               date_jour DATE    NOT NULL,
                               nb_disponibles INT NOT NULL,
                               UNIQUE(id_chambre_type,date_jour),
                               FOREIGN KEY (id_chambre_type) REFERENCES ChambreType(id) ON DELETE CASCADE
);

-- 5. Équipements (amenities) détaillés
CREATE TABLE Amenity (
                         id INT AUTO_INCREMENT PRIMARY KEY,
                         nom VARCHAR(50) NOT NULL UNIQUE
);

-- 6. Liaison Hébergement ↔ Amenity
CREATE TABLE Hebergement_Amenity (
                                     id_hebergement INT NOT NULL,
                                     id_amenity     INT NOT NULL,
                                     PRIMARY KEY(id_hebergement,id_amenity),
                                     FOREIGN KEY (id_hebergement) REFERENCES Hebergement(id) ON DELETE CASCADE,
                                     FOREIGN KEY (id_amenity)     REFERENCES Amenity(id) ON DELETE CASCADE
);

-- 7. Proximités (sports, transports, aéroports…)
CREATE TABLE Proximite (
                           id INT AUTO_INCREMENT PRIMARY KEY,
                           id_hebergement INT NOT NULL,
                           type ENUM('sport','transport','aeroport','autre') NOT NULL,
                           description VARCHAR(100),
                           distance_km DECIMAL(5,2),
                           FOREIGN KEY (id_hebergement) REFERENCES Hebergement(id) ON DELETE CASCADE
);

-- 8. Offres de réduction
CREATE TABLE Reduction (
                           id INT AUTO_INCREMENT PRIMARY KEY,
                           code VARCHAR(20) NOT NULL UNIQUE,
                           description VARCHAR(100),
                           pourcentage DECIMAL(5,2) NOT NULL CHECK (pourcentage BETWEEN 0 AND 100),
                           actif BOOLEAN NOT NULL DEFAULT TRUE
);

-- 9. Réservations
CREATE TABLE Reservation (
                             id INT AUTO_INCREMENT PRIMARY KEY,
                             id_utilisateur  INT NOT NULL,
                             id_chambre_type INT NOT NULL,
                             id_reduction    INT DEFAULT NULL,
                             date_arrivee    DATE NOT NULL,
                             date_depart     DATE NOT NULL,
                             nb_adultes      INT NOT NULL,
                             nb_enfants      INT NOT NULL DEFAULT 0,
                             nb_chambres     INT NOT NULL,
                             montant         DECIMAL(10,2) NOT NULL,
                             date_reservation TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                             FOREIGN KEY (id_utilisateur)  REFERENCES Utilisateur(id) ON DELETE CASCADE,
                             FOREIGN KEY (id_chambre_type) REFERENCES ChambreType(id) ON DELETE CASCADE,
                             FOREIGN KEY (id_reduction)    REFERENCES Reduction(id) ON DELETE SET NULL
);

-- 10. Photos multiples
CREATE TABLE Photo (
                       id INT AUTO_INCREMENT PRIMARY KEY,
                       id_hebergement INT NOT NULL,
                       chemin VARCHAR(255) NOT NULL,
                       FOREIGN KEY (id_hebergement) REFERENCES Hebergement(id) ON DELETE CASCADE
);

-- 11. Commentaires et avis
CREATE TABLE Commentaire (
                             id INT AUTO_INCREMENT PRIMARY KEY,
                             id_utilisateur INT NOT NULL,
                             id_hebergement INT NOT NULL,
                             note TINYINT NOT NULL CHECK (note BETWEEN 1 AND 5),
                             texte TEXT,
                             date_commentaire TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                             FOREIGN KEY (id_utilisateur) REFERENCES Utilisateur(id) ON DELETE CASCADE,
                             FOREIGN KEY (id_hebergement) REFERENCES Hebergement(id) ON DELETE CASCADE
);

-- 12. Paiements (simulation)
CREATE TABLE Paiement (
                          id INT AUTO_INCREMENT PRIMARY KEY,
                          id_reservation INT NOT NULL,
                          statut ENUM('en_attente','réussi','échoué') NOT NULL DEFAULT 'en_attente',
                          date_paiement TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                          montant DECIMAL(10,2) NOT NULL,
                          FOREIGN KEY (id_reservation) REFERENCES Reservation(id) ON DELETE CASCADE
);

