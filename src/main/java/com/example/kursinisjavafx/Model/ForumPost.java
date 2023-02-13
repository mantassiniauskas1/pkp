package com.example.kursinisjavafx.Model;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;




@Getter
@Setter
public class ForumPost {

    private String type;
    private Date postingDate;
    private String authorUsername;
    private String title;
    private String content;


    public ForumPost(String type, String title, String content, String authorUsername, Date postingDate) {
        this.type = type;
        this.title = title;
        this.content = content;
        this.authorUsername = authorUsername;
        this.postingDate = postingDate;
    }
}
