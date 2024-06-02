package org.userDedication;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserDedication {
    String specifiedTechnician;
    static int DEFAULT_WAITING_CYCLE_TIME = 10000;
    static long DEFAULT_TESTTIME = 20;
    Connection connection;

    public String alocateTechnician(String testname) throws Exception {
        return alocateTechnician(testname, "", DEFAULT_TESTTIME);
    }

    public String alocateTechnician(String testname, long minutesOfUse) throws Exception {
        return alocateTechnician(testname, "", minutesOfUse);
    }

    public String alocateTechnician(String testname, String specifiedTechnician) throws Exception {
        return alocateTechnician(testname, specifiedTechnician, DEFAULT_TESTTIME);
    }

    public String alocateTechnician(String testname, String specifiedTechnician, long minutesOfUse) throws Exception {
        if (connection==null||connection.isClosed()){
            createConnection();
        }
        this.specifiedTechnician = specifiedTechnician;
        for (int i = 0; i < 200; i++) {
            List<UserObject> objects = getUsersFromTable(specifiedTechnician);
            Optional<UserObject> users = objects.stream().filter(u -> u.time == null || LocalDateTime.now().isAfter(u.time)).findFirst();
            UserObject userObject;
            if (users.isPresent()) {
                userObject = users.get();
                setUserInDB(connection, userObject.user, testname, LocalDateTime.now().plusMinutes(minutesOfUse));
                connection.commit();
                return userObject.user;
            } else {
                try {
                    if (!specifiedTechnician.isEmpty()) {
                        setUserInDB(connection, specifiedTechnician, objects.get(0).testName, objects.get(0).time, false);
                        connection.commit();
                    }
                    connection.rollback();
                    Thread.sleep(DEFAULT_WAITING_CYCLE_TIME);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("User not found");
            }
        }
        throw new Exception("Po 30 minutách, nebyl přiřazen žádný technik.");
    }

    public void freeTechnician(String user) throws SQLException {
        if (getUsersFromTable(user).stream().anyMatch(userObject -> userObject.universalPick)) {
            setUserInDB(connection, user, null, null, true);
        } else {
            setUserInDB(connection, user, null, null, false);
            specifiedTechnician = "";
        }
        connection.commit();
        connection.close();
    }

    private void createConnection() throws SQLException {
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/userdedication", "root", "HesloHeslo2024");
        connection.setAutoCommit(false);
        connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
    }

    private List<UserObject> getUsersFromTable(String specifiedUser) throws SQLException {
        PreparedStatement statement;
        String sql;
        if (specifiedUser.isEmpty()) {
            sql = "SELECT * FROM user_dedication WHERE UNIVERSAL_PICK=? FOR UPDATE";
            statement = connection.prepareStatement(sql);
            statement.setBoolean(1, true);
        } else {
            sql = "SELECT * FROM user_dedication WHERE user=? FOR UPDATE";
            statement = connection.prepareStatement(sql);
            statement.setString(1, specifiedUser);
        }
        ResultSet resultSet = statement.executeQuery();
        List<UserObject> listOfUsers = new ArrayList<>();

        while (resultSet.next()) {
            listOfUsers.add(new UserObject(
                    resultSet.getString("USER"),
                    resultSet.getString("TESTNAME"),
                    resultSet.getTimestamp("RESERVED_UNTIL") != null ? resultSet.getTimestamp("RESERVED_UNTIL").toLocalDateTime() : null,
                    resultSet.getBoolean("UNIVERSAL_PICK")
            ));
        }
        return listOfUsers;
    }

    private void setUserInDB(Connection connection, String user, String testName, LocalDateTime date) throws SQLException {
        setUserInDB(connection, user, testName, date, true);
    }

    private void setUserInDB(Connection connection, String user, String testName, LocalDateTime date, boolean universalPick) throws SQLException {
        String updateSQL = "UPDATE user_dedication SET RESERVED_UNTIL = ?, TESTNAME = ?, UNIVERSAL_PICK = ? WHERE USER = ?";
        PreparedStatement statement = connection.prepareStatement(updateSQL);
        statement.setTimestamp(1, date != null ? Timestamp.valueOf(date) : null);
        statement.setString(2, testName);
        statement.setBoolean(3, universalPick);
        statement.setString(4, user);
        statement.executeUpdate();
        System.out.println("Řádek úspěšně vložen.");
    }
}