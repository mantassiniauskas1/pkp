package com.example.kursinisjavafx.Model;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
@Getter
@Setter
public class Comment extends ForumPost {

    private String type;
    private Date postingDate;
    private String authorUsername;
    private String title;
    private String content;

    public Comment(String type, String title, String content, String authorUsername, Date postingDate) {
        super(type, title, content, authorUsername, postingDate);
    }
}
