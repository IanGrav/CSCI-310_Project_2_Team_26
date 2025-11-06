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
import com.example.csci_310project2team26.data.repository.PostRepository;
import com.example.csci_310project2team26.databinding.FragmentPostDetailBinding;
import com.example.csci_310project2team26.viewmodel.CommentsViewModel;

import java.text.NumberFormat;
import java.util.Locale;

public class PostDetailFragment extends Fragment {

    private FragmentPostDetailBinding binding;
    private CommentsViewModel commentsViewModel;
    private CommentsAdapter commentsAdapter;
    private PostRepository postRepository;
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
        postRepository = new PostRepository();

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

        loadPost(postId);
        commentsViewModel.loadComments(postId);

        binding.upvoteButton.setOnClickListener(v -> vote("up"));
        binding.downvoteButton.setOnClickListener(v -> vote("down"));
        binding.commentButton.setOnClickListener(v -> focusOnCommentField());
        binding.addCommentButton.setOnClickListener(v -> addComment());

        observeViewModel();
    }

    private void observeViewModel() {
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

    private void loadPost(String id) {
        postRepository.getPostById(id, new PostRepository.Callback<Post>() {
            @Override
            public void onSuccess(Post post) {
                if (binding == null || getContext() == null) {
                    return;
                }
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

                binding.upvoteCountTextView.setText(upvoteText);
                binding.downvoteCountTextView.setText(downvoteText);
                updateCommentCountText(displayedCommentCount);
            }

            @Override
            public void onError(String error) {
                if (binding == null || getContext() == null) {
                    return;
                }
                Toast.makeText(getContext(), error, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void vote(String type) {
        if (postId == null || binding == null) return;
        
        // Check buttons exist before accessing
        if (binding.upvoteButton == null || binding.downvoteButton == null) {
            Toast.makeText(getContext(), "Unable to vote", Toast.LENGTH_SHORT).show();
            return;
        }
        
        // Disable buttons to prevent rapid clicks
        binding.upvoteButton.setEnabled(false);
        binding.downvoteButton.setEnabled(false);
        
        postRepository.votePost(postId, type, new PostRepository.Callback<PostRepository.VoteActionResult>() {
            @Override
            public void onSuccess(PostRepository.VoteActionResult result) {
                if (binding == null || getContext() == null || postId == null) {
                    return;
                }
                
                // Re-enable buttons
                try {
                    binding.upvoteButton.setEnabled(true);
                    binding.downvoteButton.setEnabled(true);
                    
                    // Safely get message and type
                    String message = result != null && result.getMessage() != null 
                            ? result.getMessage() 
                            : "Vote updated";
                    String typeStr = result != null ? result.getType() : null;
                    
                    if (typeStr != null && !typeStr.isEmpty()) {
                        Toast.makeText(getContext(), "Voted " + typeStr, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                    }
                    
                    // Reload post to get updated vote counts
                    loadPost(postId);
                } catch (Exception e) {
                    // If anything fails, at least re-enable buttons and show error
                    if (binding != null) {
                        binding.upvoteButton.setEnabled(true);
                        binding.downvoteButton.setEnabled(true);
                    }
                    if (getContext() != null) {
                        Toast.makeText(getContext(), "Vote updated but failed to refresh", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onError(String error) {
                if (binding == null || getContext() == null) {
                    return;
                }
                // Re-enable buttons on error
                try {
                    binding.upvoteButton.setEnabled(true);
                    binding.downvoteButton.setEnabled(true);
                    Toast.makeText(getContext(), error != null ? error : "Failed to vote", Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    // Ignore errors in error handler
                }
            }
        });
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