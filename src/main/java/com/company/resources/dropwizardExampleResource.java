package com.company.resources;


import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Path("/sat-results")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class dropwizardExampleResource {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/sat_results";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "Sandeep";

    private static Connection connection;

    public dropwizardExampleResource() throws SQLException {
        connection = DriverManager.getConnection(DB_URL,DB_USER,DB_PASSWORD);
        initializeDatabase();
    }


    @POST
    public Response insertData(SATResult satResult) throws SQLException{
        if(satResult.getSatScore() > 30) {
            satResult.setPassed(true);
        }
        else {
            satResult.setPassed(false);
        }
        String insertQuery = "INSERT INTO sat_results (name, address, city, country, pincode, sat_score, passed) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(insertQuery)) {
            statement.setString(1, satResult.getName());
            statement.setString(2, satResult.getAddress());
            statement.setString(3, satResult.getCity());
            statement.setString(4, satResult.getCountry());
            statement.setString(5, satResult.getPincode());
            statement.setInt(6, satResult.getSatScore());
            statement.setBoolean(7, satResult.isPassed());
            System.out.println(statement);
            int rowsInserted = statement.executeUpdate();
            System.out.println("row inserted");
            if (rowsInserted > 0) {
                return Response.ok().build();
            } else {
                return Response.serverError().build();
            }
        }
    }

    @GET
    @Path("/list")
    public Response viewAllData() throws SQLException{
        List<SATResult> resultsList = new ArrayList<>();
        String selectQuery = "SELECT * FROM sat_results";

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(selectQuery)) {
            while (resultSet.next()) {
                String name = resultSet.getString("name");
                String address = resultSet.getString("address");
                String city = resultSet.getString("city");
                String country = resultSet.getString("country");
                String pincode = resultSet.getString("pincode");
                int satScore = resultSet.getInt("sat_score");
                boolean passed = resultSet.getBoolean("passed");
                resultsList.add(new SATResult(name,address,city,country,pincode,satScore,passed));
            }
        }
       return Response.ok(resultsList).build();
    }

    @GET
    @Path("/rank/{name}")
    public Response getRank(@PathParam("name") String name) throws SQLException{
        String rankQuery = "SELECT COUNT(*) AS ranking FROM sat_results WHERE sat_score > " +
                "(SELECT sat_score FROM sat_results WHERE name = ?)";

        try (PreparedStatement statement = connection.prepareStatement(rankQuery)) {
            statement.setString(1, name);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int rank = resultSet.getInt("ranking") + 1;
                    return Response.ok(rank).build();
                } else {
                    return Response.serverError().build();
                }
            }
        }
    }

    @PUT
    @Path("/{name}")
    public Response updateScore(@PathParam("name") String name, int newScore) throws SQLException {
        String updateQuery = "UPDATE sat_results SET sat_score = ?, passed = ? WHERE name = ?";
        boolean passed;
        if(newScore > 30){
            passed = true;
        }
        else {
            passed = false;
        }
        try (PreparedStatement statement = connection.prepareStatement(updateQuery)) {
            statement.setInt(1, newScore);
            statement.setBoolean(2, passed);
            statement.setString(3, name);

            System.out.println(statement);
            int updated = statement.executeUpdate();
            System.out.println(updated);
            if(updated > 0){
                return Response.ok().build();
            }
            else {
                return Response.serverError().build();
            }
        }
    }

    @DELETE
    @Path("/{name}")
    public Response deleteRecord(@PathParam("name") String name) throws SQLException {
        String deleteQuery = "DELETE FROM sat_results WHERE name = ?";

        try (PreparedStatement statement = connection.prepareStatement(deleteQuery)) {
            statement.setString(1, name);

            int deleted = statement.executeUpdate();
            if(deleted > 0){
                return Response.ok().build();
            }
            else {
                return Response.serverError().build();
            }
        }
    }

    private static void initializeDatabase() throws SQLException {
        String createTableQuery = "CREATE TABLE IF NOT EXISTS sat_results (" +
                "name VARCHAR(100) PRIMARY KEY," +
                "address VARCHAR(200)," +
                "city VARCHAR(100)," +
                "country VARCHAR(100)," +
                "pincode VARCHAR(20)," +
                "sat_score INT," +
                "passed BOOLEAN)";

        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(createTableQuery);
        }
    }
}
