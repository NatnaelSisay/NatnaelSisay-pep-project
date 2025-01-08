package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import Model.Account;
import Util.ConnectionUtil;

public class AccountDAO {

    public Optional<Account> getAccountByUserName(String username){
        Connection connection = ConnectionUtil.getConnection();

        try {
            String query = "SELECT * FROM account WHERE username=?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, username);

            ResultSet rs = preparedStatement.executeQuery();

            while(rs.next()){
                Account account = new Account(
                    rs.getInt("account_id"),
                    rs.getString("username"),
                    rs.getString("password")
                );
                return Optional.of(account);
            }

        } catch(SQLException e){
            System.out.println(e.getMessage());
        }

        return Optional.empty();
    }
    
    public boolean save(Account account){
        Connection connection = ConnectionUtil.getConnection();

        try {
            String query = "INSERT INTO account (username, password) VALUES(?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            
            preparedStatement.setString(1, account.username);
            preparedStatement.setString(2, account.password);

            Integer rs = preparedStatement.executeUpdate();

            return rs != 0;

        } catch(SQLException e){
            System.out.println(e.getMessage());
        }

        return false;
    }

    /**
     * @param account contains username and password
     * @return Optional with the crosponding account information or empty optional
     */
    public Optional<Account> findAccountByUserNameAndPassword(Account account){
        Connection connection = ConnectionUtil.getConnection();

        try{
            String queryString = "SELECT * FROM account WHERE username=? AND password=?";
            PreparedStatement preparedStatement = connection.prepareStatement(queryString);

            preparedStatement.setString(1, account.getUsername());
            preparedStatement.setString(2, account.getPassword());

            ResultSet rs = preparedStatement.executeQuery();

            while(rs.next()){
                Account resulAccount = new Account(
                    rs.getInt("account_id"),
                    rs.getString("username"),
                    rs.getString("password")
                );

                return Optional.of(resulAccount);
            }
        } catch(SQLException e){
            System.out.println(e);
        }

        return Optional.empty();
    }
}
