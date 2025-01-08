package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import Model.Message;
import Util.ConnectionUtil;

public class MessageDAO {
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
}
