package com.example.csci_310project2team26.ui.notifications;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.csci_310project2team26.R;
import com.example.csci_310project2team26.data.repository.SessionManager;
import com.example.csci_310project2team26.databinding.FragmentNotificationsBinding;

public class NotificationsFragment extends Fragment {

    private FragmentNotificationsBinding binding;
    private NotificationsViewModel viewModel;
    private UserActivityAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(NotificationsViewModel.class);

        setupRecyclerView();
        observeViewModel();

        String userId = SessionManager.getUserId();
        viewModel.loadUserActivity(userId);

        return binding.getRoot();
    }

    private void setupRecyclerView() {
        adapter = new UserActivityAdapter(this::onActivityClicked);
        binding.activityRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.activityRecyclerView.setAdapter(adapter);
    }

    private void observeViewModel() {
        viewModel.getActivityItems().observe(getViewLifecycleOwner(), items -> {
            adapter.submitList(items);
            boolean isEmpty = items == null || items.isEmpty();
            binding.activityEmptyStateTextView.setVisibility(isEmpty ? View.VISIBLE : View.GONE);
            binding.activityRecyclerView.setVisibility(isEmpty ? View.GONE : View.VISIBLE);
        });

        viewModel.getLoading().observe(getViewLifecycleOwner(), loading -> {
            boolean visible = Boolean.TRUE.equals(loading);
            binding.activityProgressBar.setVisibility(visible ? View.VISIBLE : View.GONE);
            if (visible) {
                binding.activityEmptyStateTextView.setVisibility(View.GONE);
            }
        });

        viewModel.getError().observe(getViewLifecycleOwner(), error -> {
            if (error != null && !error.isEmpty()) {
                Toast.makeText(requireContext(), error, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void onActivityClicked(UserActivityItem item) {
        Bundle args = new Bundle();
        switch (item.getType()) {
            case POST:
                args.putString("postId", item.getPostId());
                Navigation.findNavController(binding.getRoot()).navigate(R.id.editPostFragment, args);
                break;
            case COMMENT:
                args.putString("postId", item.getPostId());
                args.putString("commentId", item.getId());
                Navigation.findNavController(binding.getRoot()).navigate(R.id.editCommentFragment, args);
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
