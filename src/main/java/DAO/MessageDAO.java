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

    /**
     * Retrive all messages in the database
     * @return Optional containing all messages in the database
     */
    public Optional<List<Message>> findAll(){
        Connection connection = ConnectionUtil.getConnection();
        try{
            String query = "SELECT * FROM message";
            Statement statement = connection.createStatement();

            ResultSet rs = statement.executeQuery(query);

            return extractMessagesFromResultSet(rs);

        } catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return Optional.empty();
    }

    /**
     * Find a message by message_id and return optional
     * @param message_id
     * @return Optional with message if message with id exist else
     *  return empty Optional
     */
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
            
            return extractMessagesFromResultSet(rs);

        } catch(SQLException e){
            System.out.println(e.getMessage());
        }

        return Optional.of(new ArrayList<>());
    }

    /**
     * Save message to database.
     * The content of the message must have all the required fields
     * 
     * @param message
     * @return saved message with the unique id
     */
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

    /**
     * Update content of a message in database
     * 
     * @param message_id
     * @param message
     * @return 0 if update was not successfull and 1 if update was successfull.
     */
    public int update(int message_id, Message message){
        Connection connection = ConnectionUtil.getConnection();

        try{
            String query = "UPDATE message SET message_text=? WHERE message_id=?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, message.message_text);
            preparedStatement.setInt(2, message_id);

            int rowUpdated = preparedStatement.executeUpdate();

            return rowUpdated;

        } catch(SQLException e){
            System.out.println(e.getMessage());
        }

        return 0;
    }

    /**
     * Helper method to convert ResultSet set to List
     * 
     * @param rs a result set from database query
     * @return the extraction of result set to a List
     */
    private Optional<List<Message>> extractMessagesFromResultSet(ResultSet rs) throws SQLException{

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
    }

}
