package com.bestllm.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bestllm.R
import com.bestllm.data.database.PostEntity

class PostAdapter(
    private val posts: List<PostEntity>,
    private val onItemClick: (PostEntity) -> Unit
) : RecyclerView.Adapter<PostAdapter.PostViewHolder>() {

    class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitle: TextView = itemView.findViewById(R.id.tvPostTitle)
        val tvAuthor: TextView = itemView.findViewById(R.id.tvPostAuthor)
        val tvContent: TextView = itemView.findViewById(R.id.tvPostContent)
        val tvTag: TextView = itemView.findViewById(R.id.tvPostTag)
        val tvVotes: TextView = itemView.findViewById(R.id.tvVotes)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_post, parent, false)
        return PostViewHolder(view)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = posts[position]
        holder.tvTitle.text = post.title
        holder.tvAuthor.text = "By ${post.author_name ?: "Unknown"}"
        holder.tvContent.text = post.content
        holder.tvVotes.text = "${post.votes} votes"

        if (post.llm_tag != null && post.llm_tag.isNotEmpty()) {
            holder.tvTag.text = "#${post.llm_tag}"
            holder.tvTag.visibility = View.VISIBLE
        } else {
            holder.tvTag.visibility = View.GONE
        }

        holder.itemView.setOnClickListener {
            onItemClick(post)
        }
    }

    override fun getItemCount() = posts.size
}

