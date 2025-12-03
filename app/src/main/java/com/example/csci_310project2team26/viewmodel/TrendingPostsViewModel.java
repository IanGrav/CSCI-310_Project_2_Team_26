package com.example.csci_310project2team26.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.csci_310project2team26.data.model.Post;
import com.example.csci_310project2team26.data.repository.PostRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * TrendingPostsViewModel - Manages trending posts data
 */
public class TrendingPostsViewModel extends ViewModel {

    private final PostRepository postRepository = new PostRepository();

    private final MutableLiveData<Boolean> loading = new MutableLiveData<>(false);
    private final MutableLiveData<String> error = new MutableLiveData<>(null);
    private final MutableLiveData<List<Post>> posts = new MutableLiveData<>(new ArrayList<>());
    private Integer lastLimit = 10;

    public LiveData<Boolean> getLoading() { return loading; }
    public LiveData<String> getError() { return error; }
    public LiveData<List<Post>> getPosts() { return posts; }

    public void loadTrendingPosts(Integer k) {
        lastLimit = k != null ? k : 10;
        loading.postValue(true);
        error.postValue(null);

        postRepository.fetchTrendingPosts(lastLimit, new PostRepository.Callback<PostRepository.PostsResult>() {
            @Override
            public void onSuccess(PostRepository.PostsResult result) {
                loading.postValue(false);
                posts.postValue(result != null && result.getPosts() != null
                        ? result.getPosts() : new ArrayList<>());
            }

            @Override
            public void onError(String err) {
                loading.postValue(false);
                error.postValue(err);
                posts.postValue(new ArrayList<>());
            }
        });
    }

    public void voteOnPost(String postId, String type) {
        if (postId == null || type == null) {
            error.postValue("Invalid vote request");
            return;
        }

        loading.postValue(true);
        error.postValue(null);

        postRepository.votePost(postId, type, new PostRepository.Callback<PostRepository.VoteActionResult>() {
            @Override
            public void onSuccess(PostRepository.VoteActionResult result) {
                loadTrendingPosts(lastLimit);
            }

            @Override
            public void onError(String err) {
                loading.postValue(false);
                error.postValue(err != null ? err : "Failed to vote on post");
            }
        });
    }
}

