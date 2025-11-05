package com.bestllm.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bestllm.R
import com.bestllm.databinding.FragmentPostDetailBinding
import com.bestllm.data.database.PostEntity
import com.bestllm.ui.adapter.CommentAdapter
import com.bestllm.ui.viewmodel.CommentViewModel
import com.bestllm.ui.viewmodel.PostViewModel

class PostDetailFragment : Fragment() {
    private var _binding: FragmentPostDetailBinding? = null
    private val binding get() = _binding!!
    private lateinit var postViewModel: PostViewModel
    private lateinit var commentViewModel: CommentViewModel
    private var postId: Int = -1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPostDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        postId = arguments?.getInt("postId", -1) ?: -1
        if (postId == -1) {
            Toast.makeText(requireContext(), "Invalid post", Toast.LENGTH_SHORT).show()
            parentFragmentManager.popBackStack()
            return
        }

        postViewModel = ViewModelProvider(this)[PostViewModel::class.java]
        commentViewModel = ViewModelProvider(this)[CommentViewModel::class.java]

        setupRecyclerView()
        setupListeners()
        observeViewModel()

        // Load post and comments
        postViewModel.getPostById(postId)
        commentViewModel.loadComments(postId)
    }

    private fun setupRecyclerView() {
        val adapter = CommentAdapter(emptyList())
        binding.rvComments.layoutManager = LinearLayoutManager(requireContext())
        binding.rvComments.adapter = adapter
    }

    private fun setupListeners() {
        binding.btnUpvote.setOnClickListener {
            postViewModel.votePost(postId, "up")
        }

        binding.btnDownvote.setOnClickListener {
            postViewModel.votePost(postId, "down")
        }

        binding.btnAddComment.setOnClickListener {
            val commentText = binding.etComment.text.toString().trim()
            if (commentText.isEmpty()) {
                Toast.makeText(requireContext(), "Please enter a comment", Toast.LENGTH_SHORT).show()
            } else {
                commentViewModel.createComment(postId, commentText)
                binding.etComment.text.clear()
            }
        }
    }

    private fun observeViewModel() {
        // Observe post
        postViewModel.getPostByIdLiveData(postId).observe(viewLifecycleOwner) { post ->
            post?.let {
                updatePostUI(it)
            }
        }

        postViewModel.currentPost.observe(viewLifecycleOwner) { post ->
            post?.let {
                updatePostUIFromModel(it)
            }
        }

        // Observe comments
        commentViewModel.comments.observe(viewLifecycleOwner) { comments ->
            val adapter = CommentAdapter(comments)
            binding.rvComments.adapter = adapter
        }

        commentViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            // Handle loading state if needed
        }

        commentViewModel.error.observe(viewLifecycleOwner) { error ->
            error?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                commentViewModel.clearError()
            }
        }

        postViewModel.error.observe(viewLifecycleOwner) { error ->
            error?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                postViewModel.clearError()
            }
        }
    }

    private fun updatePostUI(post: PostEntity) {
        binding.tvPostTitle.text = post.title
        binding.tvPostAuthor.text = "By ${post.author_name ?: "Unknown"}"
        binding.tvPostContent.text = post.content
        binding.tvVotes.text = post.votes.toString()

        if (post.llm_tag != null && post.llm_tag.isNotEmpty()) {
            binding.tvPostTag.text = "#${post.llm_tag}"
            binding.tvPostTag.visibility = View.VISIBLE
        } else {
            binding.tvPostTag.visibility = View.GONE
        }
    }

    private fun updatePostUIFromModel(post: com.bestllm.data.model.Post) {
        binding.tvPostTitle.text = post.title
        binding.tvPostAuthor.text = "By ${post.author_name ?: "Unknown"}"
        binding.tvPostContent.text = post.content
        binding.tvVotes.text = post.votes.toString()

        if (post.llm_tag != null && post.llm_tag.isNotEmpty()) {
            binding.tvPostTag.text = "#${post.llm_tag}"
            binding.tvPostTag.visibility = View.VISIBLE
        } else {
            binding.tvPostTag.visibility = View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(postId: Int): PostDetailFragment {
            val fragment = PostDetailFragment()
            val args = Bundle()
            args.putInt("postId", postId)
            fragment.arguments = args
            return fragment
        }
    }
}

