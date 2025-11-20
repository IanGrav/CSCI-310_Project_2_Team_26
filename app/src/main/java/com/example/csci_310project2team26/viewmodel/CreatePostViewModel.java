package com.example.csci_310project2team26.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.csci_310project2team26.data.model.Post;
import com.example.csci_310project2team26.data.repository.PostRepository;

public class CreatePostViewModel extends ViewModel {

    private final PostRepository postRepository = new PostRepository();
    private final MutableLiveData<Boolean> loading = new MutableLiveData<>(false);
    private final MutableLiveData<String> error = new MutableLiveData<>(null);
    private final MutableLiveData<Post> createdPost = new MutableLiveData<>(null);

    public LiveData<Boolean> getLoading() {
        return loading;
    }

    public LiveData<String> getError() {
        return error;
    }

    public LiveData<Post> getCreatedPost() {
        return createdPost;
    }

    public void createPost(String title, String content, String tag, boolean isPrompt, 
                           String promptSection, String descriptionSection) {
        if (title == null || title.trim().isEmpty()) {
            error.postValue("Title is required");
            return;
        }
        
        // Tag (llm_tag) is required by backend
        if (tag == null || tag.trim().isEmpty()) {
            error.postValue("Tag is required");
            return;
        }
        
        // Validate based on post type
        if (isPrompt) {
            // For prompt posts, require either prompt_section or description_section
            // Normalize prompt sections (trim and check if empty)
            String trimmedPromptSection = (promptSection != null && !promptSection.trim().isEmpty()) ? promptSection.trim() : null;
            String trimmedDescriptionSection = (descriptionSection != null && !descriptionSection.trim().isEmpty()) ? descriptionSection.trim() : null;
            
            if (trimmedPromptSection == null && trimmedDescriptionSection == null) {
                error.postValue("Prompt posts require either prompt section or description section");
                return;
            }
            
            // Use trimmed versions
            promptSection = trimmedPromptSection;
            descriptionSection = trimmedDescriptionSection;
        } else {
            // For regular posts, ensure prompt sections are null and require content
            promptSection = null;
            descriptionSection = null;
            
            if (content == null || content.trim().isEmpty()) {
                error.postValue("Content is required");
                return;
            }
        }

        loading.postValue(true);
        error.postValue(null);
        createdPost.postValue(null);

        postRepository.createPost(
                title.trim(), 
                content != null ? content.trim() : "", 
                tag != null ? tag.trim() : "", 
                isPrompt,
                promptSection != null ? promptSection.trim() : null,
                descriptionSection != null ? descriptionSection.trim() : null,
                new PostRepository.Callback<Post>() {
                    @Override
                    public void onSuccess(Post result) {
                        loading.postValue(false);
                        createdPost.postValue(result);
                    }

                    @Override
                    public void onError(String err) {
                        loading.postValue(false);
                        error.postValue(err);
                    }
                });
    }
}
