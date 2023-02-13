package com.example.kursinisjavafx.Controller;

import com.example.kursinisjavafx.Jdbc.JdbcDao;
import com.example.kursinisjavafx.Model.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.Window;
import lombok.SneakyThrows;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ForumPostController {

    User currUser;
    String postId;

    @FXML
    ListView<String> commentListView;
    @FXML
    TextArea commentTextArea;
    @FXML
    Button commentButton;
    @FXML
    Button deleteButton;
    @FXML
    Label postContentLabel;
    public void init(User currUser, String postId, String postContent) throws SQLException {
        this.currUser = currUser;
        this.postId = postId;
        postContentLabel.setText(postContent);
        updateComments();
    }

    public void updateComments() throws SQLException {

        List<String> results = new ArrayList<>();

        Connection connection = JdbcDao.connectToDB();
        PreparedStatement ps = connection.prepareStatement("SELECT * FROM `forum` WHERE `type` = ? AND `belongsToPostId` = ?");
        ps.setString(1, "comment");
        ps.setString(2, postId);
        ResultSet rs = ps.executeQuery();
        while(rs.next()) {
            String strComment = "";
            strComment += rs.getString("content") + "\n\n" + "Created by: " + rs.getString("author") + "\n" + "Creation date: " + rs.getString("postingDate");
            results.add(strComment);
        }

        ObservableList<String> post = FXCollections.observableArrayList(results);
        commentListView.setItems(post);
    }

    public void commentButtonOnAction(ActionEvent e) throws SQLException {

        if (commentTextArea.getText().isBlank())
            return;

        Connection connection = JdbcDao.connectToDB();
        PreparedStatement ps = connection.prepareStatement("INSERT INTO forum(type, author, content, postingDate, belongsToPostId) VALUES (?, ?, ?, ?, ?)");
        ps.setString(1, "comment");
        ps.setString(2, currUser.getUsername());
        ps.setString(3, commentTextArea.getText());
        ps.setString(4, String.valueOf(LocalDate.now()));
        ps.setString(5, postId);
        ps.execute();
        updateComments();

    }

    public void deleteButtonOnAction() throws SQLException {

        if (commentListView.getSelectionModel().getSelectedItem().isBlank())
            return;

        if (!Objects.equals(getAuthorFromComment(), currUser.getUsername()) && !Objects.equals(currUser.getRole(), "administrator"))
            return;

        String[] tempArr = (commentListView.getSelectionModel().getSelectedItem().split("\n\n"));
        String content = tempArr[0];
        Connection connection = JdbcDao.connectToDB();
        PreparedStatement ps = connection.prepareStatement("DELETE FROM `forum` WHERE `content` = ?");
        ps.setString(1, content);
        ps.executeUpdate();
        updateComments();
    }

    public String getAuthorFromComment() {

        String author = commentListView.getSelectionModel().getSelectedItem();
        String[] str = author.split("\\R");
        for (String line: str) {
            if (line.contains("by:"))
                return line.substring(12);
        }
        return null;
    }

}
