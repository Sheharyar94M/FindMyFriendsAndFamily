package com.care360.findmyfamilyandfriends.HomeScreen.ui.FragmentLocation.Chat.DB;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.care360.findmyfamilyandfriends.Util.Constants;

@Entity(tableName = Constants.TABLE_MESSAGES)
public class MessageEntity {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = Constants.SENDER_ID)
    private String senderId;

    @ColumnInfo(name = Constants.RECEIVER_ID)
    private String receiverId;

    @ColumnInfo(name = Constants.MESSAGE)
    private String message;

    @ColumnInfo(name = Constants.TIMESTAMP)
    private String timestamp;

    public MessageEntity() {}

    @Ignore
    public MessageEntity(String senderId, String receiverId, String message, String timestamp) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.message = message;
        this.timestamp = timestamp;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

}
