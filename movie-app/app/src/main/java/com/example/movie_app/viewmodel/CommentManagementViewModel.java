package com.example.movie_app.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.movie_app.models.Comment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class CommentManagementViewModel extends AndroidViewModel {

    private final DatabaseReference databaseReference;
    private final MutableLiveData<List<Comment>> commentsLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> toastMessage = new MutableLiveData<>();
    private final String dbUrl = "https://movie-app-system-d6696-default-rtdb.asia-southeast1.firebasedatabase.app/";

    public CommentManagementViewModel(@NonNull Application application) {
        super(application);
        databaseReference = FirebaseDatabase.getInstance(dbUrl).getReference("comments");
        observeComments();
    }

    public LiveData<List<Comment>> getCommentsLiveData() {
        return commentsLiveData;
    }

    public LiveData<String> getToastMessage() {
        return toastMessage;
    }

    private void observeComments() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Comment> allComments = new ArrayList<>();
                for (DataSnapshot movieSnapshot : snapshot.getChildren()) {
                    String movieId = movieSnapshot.getKey();
                    // Duyệt qua từng bình luận trong movie đó
                    for (DataSnapshot commentSnapshot : movieSnapshot.getChildren()) {
                        Comment comment = commentSnapshot.getValue(Comment.class);
                        if (comment != null) {
                            comment.setId(commentSnapshot.getKey());
                            comment.setMovieId(movieId);
                            allComments.add(comment);
                        }
                    }
                }
                allComments.sort((c1, c2) -> Long.compare(c2.getTimestamp(), c1.getTimestamp()));
                commentsLiveData.setValue(allComments);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                toastMessage.setValue("Lỗi tải dữ liệu: " + error.getMessage());
            }
        });
    }

    public void deleteComment(Comment comment) {
        if (comment.getId() == null || comment.getMovieId() == null) {
            toastMessage.setValue("Không tìm thấy thông tin để xóa bình luận");
            return;
        }
        
        databaseReference.child(comment.getMovieId())
                .child(comment.getId())
                .removeValue((error, ref) -> {
            if (error == null) {
                toastMessage.setValue("Đã xóa bình luận thành công");
            } else {
                toastMessage.setValue("Xóa thất bại: " + error.getMessage());
            }
        });
    }
}