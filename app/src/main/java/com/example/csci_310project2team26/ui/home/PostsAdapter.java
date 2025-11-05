package com.example.csci_310project2team26.ui.home;

import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.csci_310project2team26.R;
import com.example.csci_310project2team26.data.model.Post;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.text.NumberFormat;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.PostViewHolder> {

    public interface OnPostClickListener {
        void onPostClick(Post post);
    }

    private final List<Post> items = new ArrayList<>();
    private final OnPostClickListener clickListener;

    public PostsAdapter(OnPostClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public void submitList(List<Post> newItems) {
        items.clear();
        if (newItems != null) items.addAll(newItems);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        Post post = items.get(position);
        holder.bind(post);
        holder.itemView.setOnClickListener(v -> {
            if (clickListener != null) clickListener.onPostClick(post);
        });
    }

    @Override
    public int getItemCount() { return items.size(); }

    static class PostViewHolder extends RecyclerView.ViewHolder {
        private final TextView titleTextView;
        private final TextView metaTextView;
        private final TextView contentTextView;
        private final TextView upvoteTextView;
        private final TextView scoreTextView;
        private final TextView downvoteTextView;
        private final NumberFormat numberFormat;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            metaTextView = itemView.findViewById(R.id.metaTextView);
            contentTextView = itemView.findViewById(R.id.contentTextView);
            upvoteTextView = itemView.findViewById(R.id.upvoteTextView);
            scoreTextView = itemView.findViewById(R.id.scoreTextView);
            downvoteTextView = itemView.findViewById(R.id.downvoteTextView);
            numberFormat = NumberFormat.getIntegerInstance(Locale.getDefault());
        }

        public void bind(Post post) {
            Resources resources = itemView.getResources();
            titleTextView.setText(post.getTitle());

            String author = post.getAuthor_name() != null && !post.getAuthor_name().isEmpty()
                    ? post.getAuthor_name()
                    : resources.getString(R.string.post_meta_unknown_author);
            String tag = post.getLlm_tag() != null && !post.getLlm_tag().isEmpty()
                    ? post.getLlm_tag()
                    : resources.getString(R.string.post_meta_unknown_tag);
            int commentCount = Math.max(post.getComment_count(), 0);
            String commentsText = resources.getQuantityString(
                    R.plurals.post_comments,
                    commentCount,
                    numberFormat.format(commentCount)
            );
            String meta = resources.getString(R.string.post_meta_format, author, tag, commentsText);
            metaTextView.setText(meta);

            contentTextView.setText(post.getContent());

            int upvotes = Math.max(post.getUpvotes(), 0);
            int downvotes = Math.max(post.getDownvotes(), 0);
            int score = post.getUpvotes() - post.getDownvotes();

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
            String scoreText = resources.getString(
                    R.string.post_score,
                    numberFormat.format(score)
            );

            upvoteTextView.setText(upvoteText);
            downvoteTextView.setText(downvoteText);
            scoreTextView.setText(scoreText);
        }
    }
}