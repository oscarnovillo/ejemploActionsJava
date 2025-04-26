package com.example.proyectospring.dao.repositories.JDBC;

import com.example.proyectospring.dao.model.Patient;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.List;

@Repository
public class PatientRepositoryJDBC {

    public List<Patient> getAll() {
        // Añadir clase conexión y coger la url de application.properties
//        try (Connection myConnection = DriverManager.getConnection("jdbc:mysql://dam2.mysql.iesquevedo.es:3335/Luciasanmiguel_Coffeecompanydb", "root", "quevedo2dam");
//             PreparedStatement preparedStatement = myConnection.prepareStatement("SELECT * from coffees where id_prod= ?")) {

//            preparedStatement.setInt(1, 1);
//            ResultSet rs = preparedStatement.executeQuery();
//
//            while (rs.next()) {
//                int prodId = rs.getInt("id_prod");
//                String coffeeName = rs.getString("COF_NAME");
//                int supplierID = rs.getInt("SUPP_ID");
//                float price = rs.getFloat("PRICE");
//                int sales = rs.getInt("SALES");
//                int total = rs.getInt("TOTAL");
//                System.out.println(prodId + ", " + coffeeName + ", " + supplierID + ", " + price +
//                        ", " + sales + ", " + total);
//            }
//        } catch (SQLException e) {
//            e.printStackTrace(System.err);
//        } catch (Exception e) {
//            e.printStackTrace(System.err);
//        }

        return null;  //Habría que crear una lista de objetos con los datos
    }
}