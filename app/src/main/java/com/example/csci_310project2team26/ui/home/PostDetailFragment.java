package com.example.csci_310project2team26.ui.home;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.csci_310project2team26.R;
import com.example.csci_310project2team26.data.model.Post;
import com.example.csci_310project2team26.databinding.FragmentPostDetailBinding;
import com.example.csci_310project2team26.viewmodel.CommentsViewModel;
import com.example.csci_310project2team26.viewmodel.PostDetailViewModel;

import java.text.NumberFormat;
import java.util.Locale;

public class PostDetailFragment extends Fragment {

    private FragmentPostDetailBinding binding;
    private CommentsViewModel commentsViewModel;
    private PostDetailViewModel postDetailViewModel;
    private CommentsAdapter commentsAdapter;
    private String postId;
    private int displayedCommentCount = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentPostDetailBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        commentsViewModel = new ViewModelProvider(this).get(CommentsViewModel.class);
        postDetailViewModel = new ViewModelProvider(this).get(PostDetailViewModel.class);

        commentsAdapter = new CommentsAdapter();
        commentsAdapter.setOnCommentVoteListener((comment, type) -> {
            if (postId != null && comment != null && comment.getId() != null && !comment.getId().isEmpty()) {
                // Check if already voting to prevent rapid clicks
                Boolean isVoting = commentsViewModel.getLoading().getValue();
                if (Boolean.TRUE.equals(isVoting)) {
                    return; // Already processing a vote
                }
                commentsViewModel.voteOnComment(postId, comment.getId(), type);
            }
        });
        binding.commentsRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.commentsRecyclerView.setAdapter(commentsAdapter);

        if (getArguments() != null) {
            postId = getArguments().getString("postId");
        }

        if (postId == null || postId.isEmpty()) {
            Toast.makeText(requireContext(), "Post ID is missing", Toast.LENGTH_LONG).show();
            requireActivity().onBackPressed();
            return;
        }

        postDetailViewModel.loadPost(postId);
        commentsViewModel.loadComments(postId);

        binding.upvoteButton.setOnClickListener(v -> vote("up"));
        binding.downvoteButton.setOnClickListener(v -> vote("down"));
        binding.commentButton.setOnClickListener(v -> focusOnCommentField());
        binding.addCommentButton.setOnClickListener(v -> addComment());

        observeViewModel();
    }

    private void observeViewModel() {
        // Observe post data (similar to how comments are observed)
        postDetailViewModel.getPost().observe(getViewLifecycleOwner(), post -> {
            if (binding == null || post == null) return;
            updatePostUI(post);
        });
        
        postDetailViewModel.getLoading().observe(getViewLifecycleOwner(), loading -> {
            if (binding == null) return;
            // Optionally show/hide loading indicator
        });
        
        postDetailViewModel.getError().observe(getViewLifecycleOwner(), err -> {
            if (err != null && binding != null && getContext() != null) {
                Toast.makeText(getContext(), err, Toast.LENGTH_LONG).show();
            }
        });
        
        // Observe comments
        commentsViewModel.getComments().observe(getViewLifecycleOwner(), list -> {
            if (binding == null) return;
            commentsAdapter.submitList(list);
            displayedCommentCount = list != null ? list.size() : 0;
            updateCommentCountText(displayedCommentCount);
        });
        commentsViewModel.getError().observe(getViewLifecycleOwner(), err -> {
            if (err != null && binding != null && getContext() != null) {
                Toast.makeText(getContext(), err, Toast.LENGTH_LONG).show();
            }
        });
        commentsViewModel.isPostingComment().observe(getViewLifecycleOwner(), posting -> {
            if (binding == null) return;
            boolean inFlight = Boolean.TRUE.equals(posting);
            binding.addCommentButton.setEnabled(!inFlight);
            binding.commentEditText.setEnabled(!inFlight);
        });
        commentsViewModel.getLatestPostedComment().observe(getViewLifecycleOwner(), comment -> {
            if (comment == null || binding == null) return;
            binding.commentEditText.setText("");
            // Comments will be reloaded automatically by the ViewModel
            // Scroll to top after a brief delay to allow RecyclerView to update
            binding.commentsRecyclerView.post(() -> {
                if (binding != null) {
                    binding.commentsRecyclerView.scrollToPosition(0);
                }
            });
        });
    }
    
    private void updatePostUI(Post post) {
        if (binding == null || getContext() == null || post == null) return;
        
        binding.titleTextView.setText(post.getTitle() != null ? post.getTitle() : "");
        binding.contentTextView.setText(post.getContent() != null ? post.getContent() : "");

        Resources resources = getResources();
        String author = post.getAuthor_name() != null && !post.getAuthor_name().isEmpty()
                ? post.getAuthor_name()
                : resources.getString(R.string.post_meta_unknown_author);
        boolean hasTag = post.getLlm_tag() != null && !post.getLlm_tag().isEmpty();
        String tagLabel = hasTag
                ? resources.getString(R.string.post_tag_format, post.getLlm_tag())
                : resources.getString(R.string.post_tag_unknown);
        binding.tagTextView.setText(tagLabel);
        binding.authorTextView.setText(resources.getString(R.string.post_author_format, author));

        NumberFormat numberFormat = NumberFormat.getIntegerInstance(Locale.getDefault());
        int upvotes = Math.max(post.getUpvotes(), 0);
        int downvotes = Math.max(post.getDownvotes(), 0);
        displayedCommentCount = Math.max(post.getComment_count(), 0);

        String upvoteText = resources.getQuantityString(
                R.plurals.post_upvotes,
                upvotes,
                numberFormat.format(upvotes)
        );
        String downvoteText = resources.getQuantityString(
                R.plurals.post_downvotes,
                downvotes,
                numberFormat.format(downvotes)
        );

        // Update vote counts - this will automatically update when post LiveData changes
        binding.upvoteCountTextView.setText(upvoteText);
        binding.downvoteCountTextView.setText(downvoteText);
        updateCommentCountText(displayedCommentCount);
    }

    private void vote(String type) {
        if (postId == null || binding == null) return;
        
        // Check buttons exist before accessing
        if (binding.upvoteButton == null || binding.downvoteButton == null) {
            return;
        }
        
        // Check if already voting to prevent rapid clicks (same pattern as comment voting)
        Boolean isVoting = postDetailViewModel.getLoading().getValue();
        if (Boolean.TRUE.equals(isVoting)) {
            return; // Already processing a vote
        }
        
        // Use ViewModel to vote (same pattern as comment voting)
        // This will automatically reload the post and update UI via LiveData
        postDetailViewModel.voteOnPost(postId, type);
    }

    private void addComment() {
        if (binding == null || postId == null) return;
        String text = binding.commentEditText.getText() != null 
                ? binding.commentEditText.getText().toString().trim() 
                : "";
        if (TextUtils.isEmpty(text)) return;
        commentsViewModel.addComment(postId, text);
    }

    private void focusOnCommentField() {
        if (binding == null || getContext() == null) return;
        binding.commentEditText.requestFocus();
        binding.postDetailScrollView.post(() -> {
            if (binding == null || getContext() == null) return;
            binding.postDetailScrollView.smoothScrollTo(0, binding.commentEditText.getBottom());
            InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.showSoftInput(binding.commentEditText, InputMethodManager.SHOW_IMPLICIT);
            }
        });
    }

    private void updateCommentCountText(int commentCount) {
        if (binding == null || getContext() == null) return;
        Resources resources = getResources();
        NumberFormat numberFormat = NumberFormat.getIntegerInstance(Locale.getDefault());
        String commentsText = resources.getQuantityString(
                R.plurals.post_comments,
                commentCount,
                numberFormat.format(commentCount)
        );
        binding.commentCountTextView.setText(commentsText);
    }
}