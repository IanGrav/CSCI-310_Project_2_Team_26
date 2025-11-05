package com.bestllm.ui

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bestllm.R
import com.bestllm.databinding.FragmentPostListBinding
import com.bestllm.ui.adapter.PostAdapter
import com.bestllm.ui.viewmodel.PostViewModel

class PostListFragment : Fragment() {
    private var _binding: FragmentPostListBinding? = null
    private val binding get() = _binding!!
    private lateinit var postViewModel: PostViewModel
    private lateinit var postAdapter: PostAdapter
    private var currentTag: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPostListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        postViewModel = ViewModelProvider(this)[PostViewModel::class.java]

        setupRecyclerView()
        setupListeners()
        observeViewModel()

        // Load posts
        postViewModel.loadPosts()
    }

    private fun setupRecyclerView() {
        postAdapter = PostAdapter(emptyList()) { post ->
            // Navigate to post detail
            val fragment = PostDetailFragment.newInstance(post.id)
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .addToBackStack(null)
                .commit()
        }
        binding.rvPosts.layoutManager = LinearLayoutManager(requireContext())
        binding.rvPosts.adapter = postAdapter
    }

    private fun setupListeners() {
        binding.btnSearch.setOnClickListener {
            val tag = binding.etSearchTag.text.toString().trim()
            currentTag = if (tag.isEmpty()) null else tag
            postViewModel.loadPosts(currentTag)
        }

        binding.btnCreatePost.setOnClickListener {
            showCreatePostDialog()
        }
    }

    private fun observeViewModel() {
        postViewModel.getPostsLiveData().observe(viewLifecycleOwner) { posts ->
            postAdapter = PostAdapter(posts) { post ->
                val fragment = PostDetailFragment.newInstance(post.id)
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainer, fragment)
                    .addToBackStack(null)
                    .commit()
            }
            binding.rvPosts.adapter = postAdapter
        }

        postViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        postViewModel.error.observe(viewLifecycleOwner) { error ->
            error?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                postViewModel.clearError()
            }
        }
    }

    private fun showCreatePostDialog() {
        val dialogView = LayoutInflater.from(requireContext())
            .inflate(R.layout.dialog_create_post, null)
        val etTitle = dialogView.findViewById<EditText>(R.id.etTitle)
        val etContent = dialogView.findViewById<EditText>(R.id.etContent)
        val etTag = dialogView.findViewById<EditText>(R.id.etTag)

        AlertDialog.Builder(requireContext())
            .setTitle("Create Post")
            .setView(dialogView)
            .setPositiveButton("Create") { _, _ ->
                val title = etTitle.text.toString().trim()
                val content = etContent.text.toString().trim()
                val tag = etTag.text.toString().trim().takeIf { it.isNotEmpty() }

                if (title.isEmpty() || content.isEmpty()) {
                    Toast.makeText(requireContext(), "Title and content are required", Toast.LENGTH_SHORT).show()
                } else {
                    postViewModel.createPost(title, content, tag)
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance() = PostListFragment()
    }
}

