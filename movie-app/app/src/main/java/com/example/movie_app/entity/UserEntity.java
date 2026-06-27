package com.example.movie_app.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "users")
public class UserEntity {
    @PrimaryKey
    @NonNull
    public String userId;

    public String email;
    public String displayName;
    public String avatarUrl;

    // Constructor, Getter, Setter
    public UserEntity(@NonNull String userId, String email, String displayName, String avatarUrl) {
        this.userId = userId;
        this.email = email;
        this.displayName = displayName;
        this.avatarUrl = avatarUrl;
    }
}