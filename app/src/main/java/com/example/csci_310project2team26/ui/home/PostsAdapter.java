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
        private final TextView tagTextView;
        private final TextView authorTextView;
        private final TextView contentTextView;
        private final TextView upvoteTextView;
        private final TextView downvoteTextView;
        private final TextView commentCountTextView;
        private final NumberFormat numberFormat;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            tagTextView = itemView.findViewById(R.id.tagTextView);
            authorTextView = itemView.findViewById(R.id.authorTextView);
            contentTextView = itemView.findViewById(R.id.contentTextView);
            upvoteTextView = itemView.findViewById(R.id.upvoteTextView);
            downvoteTextView = itemView.findViewById(R.id.downvoteTextView);
            commentCountTextView = itemView.findViewById(R.id.commentCountTextView);
            numberFormat = NumberFormat.getIntegerInstance(Locale.getDefault());
        }

        public void bind(Post post) {
            Resources resources = itemView.getResources();
            titleTextView.setText(post.getTitle());

            String author = post.getAuthor_name() != null && !post.getAuthor_name().isEmpty()
                    ? post.getAuthor_name()
                    : resources.getString(R.string.post_meta_unknown_author);
            boolean hasTag = post.getLlm_tag() != null && !post.getLlm_tag().isEmpty();
            String tagLabel = hasTag
                    ? resources.getString(R.string.post_tag_format, post.getLlm_tag())
                    : resources.getString(R.string.post_tag_unknown);
            tagTextView.setText(tagLabel);
            authorTextView.setText(resources.getString(R.string.post_author_format, author));

            contentTextView.setText(post.getContent());

            int upvotes = Math.max(post.getUpvotes(), 0);
            int downvotes = Math.max(post.getDownvotes(), 0);
            int commentCount = Math.max(post.getComment_count(), 0);

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
            String commentsText = resources.getQuantityString(
                    R.plurals.post_comments,
                    commentCount,
                    numberFormat.format(commentCount)
            );

            upvoteTextView.setText(upvoteText);
            downvoteTextView.setText(downvoteText);
            commentCountTextView.setText(commentsText);
        }
    }
}