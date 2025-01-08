package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import Model.Message;
import Util.ConnectionUtil;

public class MessageDAO {
    public Optional<Message> findById(int message_id){
        Connection connection = ConnectionUtil.getConnection();

        try{
            String query = "SELECT * FROM message WHERE message_id=?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, message_id);

            ResultSet rs = preparedStatement.executeQuery();

            while(rs.next()){
                Message result = new Message(
                    rs.getInt("message_id"),
                    rs.getInt("posted_by"),
                    rs.getString("message_text"),
                    rs.getLong("time_posted_epoch")
                );

                return Optional.of(result);
            }
        } catch(SQLException e){
            System.out.println(e.getMessage());
        }

        return Optional.empty();
    }

    /**
     * Find list of messages by specific account id
     * @param postedById the account reponsible for the message
     * @return optional containing List of messages or empty list
     */
    public Optional<List<Message>> findByPostedById(int postedById){
        Connection connection = ConnectionUtil.getConnection();

        try{
            String query = "SELECT * FROM message WHERE posted_by=?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, postedById);

            ResultSet rs = preparedStatement.executeQuery();
            
            List<Message> messages = new ArrayList<>();
            while (rs.next()) {
                Message message = new Message(
                    rs.getInt("message_id"),
                    rs.getInt("posted_by"),
                    rs.getString("message_text"),
                    rs.getLong("time_posted_epoch")
                );

                messages.add(message);
            }

            return Optional.of(messages);

        } catch(SQLException e){
            System.out.println(e.getMessage());
        }

        return Optional.of(new ArrayList<>());
    }


    public Optional<List<Message>> template(int user_id){
        Connection connection = ConnectionUtil.getConnection();
        try{
            String query = "SELECT * FROM message where posted_by=?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, user_id);

            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                    // do something
                    System.out.println("empty");
            }
        } catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return Optional.empty();
    }

    public Message save(Message message){
        Message result = message;

        Connection connection = ConnectionUtil.getConnection();
        try{
            String statement = "INSERT INTO message (posted_by, message_text, time_posted_epoch) VALUES(?,?,?)";
            PreparedStatement ps = connection.prepareStatement(statement, Statement.RETURN_GENERATED_KEYS);

            ps.setInt(1, message.posted_by);
            ps.setString(2, message.message_text);
            ps.setLong(3, message.time_posted_epoch);

            int status = ps.executeUpdate(); // assume status is always 1

            if(status == 0){
                throw new SQLException("Inserting message failed");
            }

            ResultSet generatedKey = ps.getGeneratedKeys();
            if(generatedKey.next()){
                result.setMessage_id(generatedKey.getInt(1));
            }

        } catch(SQLException e){
            System.out.println(e.getMessage());
        }

        return result;
    }

    /**
     * given message_id delete message from database
     * @param message_id
     * @return the number of rows affected by the delete execution
     */
    public int delete(int message_id){
        Connection connection = ConnectionUtil.getConnection();

        try{
            String query = "DELETE FROM message WHERE message_id=?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, message_id);

            return preparedStatement.executeUpdate(); //returns number of rows affected 

        } catch(SQLException e){
            System.out.println(e.getMessage());
        }

        return 0; // no row affected
    }

}
