package com.example.csci_310project2team26.data.repository;

import com.example.csci_310project2team26.data.model.Post;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

/**
 * PostRepository - Provides in-memory demo data with rich querying helpers.
 *
 * This implementation removes the dependency on the backend service so that
 * the Android client can be exercised end-to-end while the API is unstable.
 * All operations are thread offloaded to mimic the asynchronous Retrofit
 * behaviour the UI layer was originally written for.
 */
public class PostRepository {

    public static class PostsResult {
        private final List<Post> posts;
        private final int count;
        private final int limit;
        private final int offset;

        public PostsResult(List<Post> posts, int count, int limit, int offset) {
            this.posts = posts;
            this.count = count;
            this.limit = limit;
            this.offset = offset;
        }

        public List<Post> getPosts() {
            return posts;
        }

        public int getCount() {
            return count;
        }

        public int getLimit() {
            return limit;
        }

        public int getOffset() {
            return offset;
        }
    }

    public static class VoteActionResult {
        private final String message;
        private final String action;
        private final String type;

        public VoteActionResult(String message, String action, String type) {
            this.message = message;
            this.action = action;
            this.type = type;
        }

        public String getMessage() {
            return message;
        }

        public String getAction() {
            return action;
        }

        public String getType() {
            return type;
        }
    }

    private static final List<Post> SHARED_POSTS = Collections.synchronizedList(new ArrayList<>());
    private static boolean seeded = false;

    private final ExecutorService executorService;

    public PostRepository() {
        this.executorService = Executors.newSingleThreadExecutor();
        seedDummyPosts();
    }

    public interface Callback<T> {
        void onSuccess(T result);
        void onError(String error);
    }

    public void fetchPosts(String sort,
                           Integer limit,
                           Integer offset,
                           Boolean isPromptPost,
                           Callback<PostsResult> callback) {
        executorService.execute(() -> {
            try {
                List<Post> result = queryPosts(null, "full_text", sort, isPromptPost);
                callback.onSuccess(buildResult(result, limit, offset));
            } catch (Exception e) {
                callback.onError(e.getMessage() != null ? e.getMessage() : "Unable to load posts");
            }
        });
    }

    public void searchPosts(String query,
                            String searchType,
                            String sort,
                            Integer limit,
                            Integer offset,
                            Boolean isPromptPost,
                            Callback<PostsResult> callback) {
        executorService.execute(() -> {
            try {
                List<Post> result = queryPosts(query, searchType, sort, isPromptPost);
                callback.onSuccess(buildResult(result, limit, offset));
            } catch (Exception e) {
                callback.onError(e.getMessage() != null ? e.getMessage() : "Unable to search posts");
            }
        });
    }

    public void getPostById(String postId, Callback<Post> callback) {
        executorService.execute(() -> {
            Post post = null;
            synchronized (SHARED_POSTS) {
                for (Post candidate : SHARED_POSTS) {
                    if (candidate.getId().equals(postId)) {
                        post = copyPost(candidate);
                        break;
                    }
                }
            }
            if (post != null) {
                callback.onSuccess(post);
            } else {
                callback.onError("Post not found");
            }
        });
    }

    public void createPost(String title,
                           String content,
                           String llmTag,
                           boolean isPromptPost,
                           Callback<Post> callback) {
        executorService.execute(() -> {
            try {
                String userId = SessionManager.getUserId();
                String authorId = userId != null ? userId : "demo-user";
                String authorName = userId != null ? userId : "Demo User";

                long now = System.currentTimeMillis();
                Post newPost = new Post();
                newPost.setId(UUID.randomUUID().toString());
                newPost.setAuthor_id(authorId);
                newPost.setAuthor_name(authorName);
                newPost.setTitle(title);
                newPost.setContent(content);
                newPost.setLlm_tag(llmTag);
                newPost.setIs_prompt_post(isPromptPost);
                String timestamp = Long.toString(now);
                newPost.setCreated_at(timestamp);
                newPost.setUpdated_at(timestamp);
                newPost.setUpvotes(0);
                newPost.setDownvotes(0);
                newPost.setComment_count(0);

                synchronized (SHARED_POSTS) {
                    SHARED_POSTS.add(0, newPost);
                }

                callback.onSuccess(copyPost(newPost));
            } catch (Exception e) {
                callback.onError(e.getMessage() != null ? e.getMessage() : "Unable to create post");
            }
        });
    }

    public void votePost(String postId, String type, Callback<VoteActionResult> callback) {
        executorService.execute(() -> {
            synchronized (SHARED_POSTS) {
                for (Post post : SHARED_POSTS) {
                    if (post.getId().equals(postId)) {
                        String normalized = type != null ? type.toLowerCase(Locale.US) : "";
                        switch (normalized) {
                            case "up":
                                post.setUpvotes(post.getUpvotes() + 1);
                                callback.onSuccess(new VoteActionResult("Vote recorded", "added", "up"));
                                return;
                            case "down":
                                post.setDownvotes(post.getDownvotes() + 1);
                                callback.onSuccess(new VoteActionResult("Vote recorded", "added", "down"));
                                return;
                            default:
                                callback.onError("Invalid vote type");
                                return;
                        }
                    }
                }
            }
            callback.onError("Post not found");
        });
    }

    public void fetchPostsForUser(String userId, Callback<List<Post>> callback) {
        executorService.execute(() -> {
            if (userId == null || userId.trim().isEmpty()) {
                callback.onSuccess(new ArrayList<>());
                return;
            }
            List<Post> results = new ArrayList<>();
            synchronized (SHARED_POSTS) {
                for (Post post : SHARED_POSTS) {
                    if (userId.equals(post.getAuthor_id())) {
                        results.add(copyPost(post));
                    }
                }
            }
            callback.onSuccess(results);
        });
    }

    public void updatePost(String postId,
                           String title,
                           String content,
                           String llmTag,
                           boolean isPromptPost,
                           Callback<Post> callback) {
        executorService.execute(() -> {
            synchronized (SHARED_POSTS) {
                for (Post post : SHARED_POSTS) {
                    if (post.getId().equals(postId)) {
                        post.setTitle(title);
                        post.setContent(content);
                        post.setLlm_tag(llmTag);
                        post.setIs_prompt_post(isPromptPost);
                        post.setUpdated_at(Long.toString(System.currentTimeMillis()));
                        callback.onSuccess(copyPost(post));
                        return;
                    }
                }
            }
            callback.onError("Post not found");
        });
    }

    private PostsResult buildResult(List<Post> posts, Integer limit, Integer offset) {
        int safeLimit = limit != null ? Math.max(limit, 0) : posts.size();
        int safeOffset = offset != null ? Math.max(offset, 0) : 0;

        if (safeLimit == 0) {
            safeLimit = posts.size();
        }

        int from = Math.min(safeOffset, posts.size());
        int to = Math.min(from + safeLimit, posts.size());
        List<Post> slice = posts.subList(from, to)
                .stream()
                .map(this::copyPost)
                .collect(Collectors.toList());

        return new PostsResult(slice, posts.size(), safeLimit, safeOffset);
    }

    private List<Post> queryPosts(String query,
                                  String searchType,
                                  String sort,
                                  Boolean isPromptPost) {
        List<Post> working;
        synchronized (SHARED_POSTS) {
            working = new ArrayList<>(SHARED_POSTS);
        }

        if (isPromptPost != null) {
            working = working.stream()
                    .filter(post -> post.isIs_prompt_post() == isPromptPost)
                    .collect(Collectors.toList());
        }

        if (query != null && !query.trim().isEmpty()) {
            String search = query.trim().toLowerCase(Locale.US);
            String mode = searchType != null ? searchType.toLowerCase(Locale.US) : "full_text";
            working = working.stream()
                    .filter(post -> matchesSearch(post, search, mode))
                    .collect(Collectors.toList());
        }

        Comparator<Post> comparator = buildComparator(sort);
        working.sort(comparator);
        return working;
    }

    private boolean matchesSearch(Post post, String search, String mode) {
        switch (mode) {
            case "title":
                return contains(post.getTitle(), search);
            case "content":
                return contains(post.getContent(), search);
            case "author":
                return contains(post.getAuthor_name(), search);
            case "tag":
                return contains(post.getLlm_tag(), search);
            case "full_text":
            default:
                return contains(post.getTitle(), search)
                        || contains(post.getContent(), search)
                        || contains(post.getAuthor_name(), search)
                        || contains(post.getLlm_tag(), search);
        }
    }

    private boolean contains(String value, String search) {
        return value != null && value.toLowerCase(Locale.US).contains(search);
    }

    private Comparator<Post> buildComparator(String sort) {
        Comparator<Post> byCreatedDesc = Comparator.comparing(this::resolveCreatedAt).reversed();
        if (sort == null) {
            return byCreatedDesc;
        }
        switch (sort.toLowerCase(Locale.US)) {
            case "oldest":
                return byCreatedDesc.reversed();
            case "top":
                return Comparator.comparingInt(this::score)
                        .thenComparing(this::resolveCreatedAt)
                        .reversed();
            case "trending":
                return Comparator.comparingInt(this::score)
                        .reversed()
                        .thenComparing(this::resolveCreatedAt, Comparator.reverseOrder());
            case "new":
            case "newest":
            default:
                return byCreatedDesc;
        }
    }

    private int score(Post post) {
        return post.getUpvotes() - post.getDownvotes();
    }

    private long resolveCreatedAt(Post post) {
        String created = post.getCreated_at();
        if (created == null || created.isEmpty()) {
            return Long.MIN_VALUE;
        }
        try {
            return Long.parseLong(created);
        } catch (NumberFormatException ignored) {
            return Long.MIN_VALUE;
        }
    }

    private Post copyPost(Post original) {
        return new Post(
                original.getId(),
                original.getAuthor_id(),
                original.getAuthor_name(),
                original.getTitle(),
                original.getContent(),
                original.getLlm_tag(),
                original.isIs_prompt_post(),
                original.getCreated_at(),
                original.getUpdated_at(),
                original.getUpvotes(),
                original.getDownvotes(),
                original.getComment_count()
        );
    }

    private void seedDummyPosts() {
        synchronized (SHARED_POSTS) {
            if (seeded || !SHARED_POSTS.isEmpty()) {
                return;
            }
            seeded = true;
        }
        long now = System.currentTimeMillis();
        addDummyPost("1", "alice", "Alice Johnson", "Why GPT-4 excels at summarisation",
                "Sharing a few tips to get consistent summaries from GPT-4 when dealing with long academic texts.",
                "GPT-4", false, now - daysToMillis(1), 42, 3, 12);
        addDummyPost("2", "bob", "Bob Martinez", "Claude vs. Gemini for coding assistance",
                "Curious how people feel about the latest Claude update compared to Gemini Advanced for debugging.",
                "Claude", false, now - hoursToMillis(10), 35, 5, 18);
        addDummyPost("3", "carol", "Carol Nguyen", "Prompt: Creative brainstorming partner",
                "Looking for a prompt that turns the model into a lively brainstorming buddy for product features.",
                "PromptCraft", true, now - daysToMillis(2), 18, 1, 6);
        addDummyPost("4", "dave", "Dave Kim", "Fine-tuning smaller models on niche datasets",
                "Anyone experimented with adapting open LLMs on domain-specific corpora without breaking coherence?",
                "LLaMA", false, now - hoursToMillis(5), 27, 2, 9);
        addDummyPost("5", "erin", "Erin Patel", "Prompt: Study guide generator",
                "Share your favourite template for turning lecture notes into concise study guides.",
                "StudyBuddy", true, now - daysToMillis(3), 22, 0, 14);
        addDummyPost("6", "frank", "Frank Li", "Reducing hallucinations with retrieval",
                "Posting the workflow we use to keep responses grounded when the model has limited knowledge.",
                "RAG", false, now - hoursToMillis(30), 31, 4, 20);
        addDummyPost("7", "grace", "Grace Hopper", "Prompt: Lightweight code reviewer",
                "Looking for prompts that catch style inconsistencies without overwhelming junior devs.",
                "CodeReview", true, now - hoursToMillis(18), 40, 6, 25);
        addDummyPost("8", "henry", "Henry Zhao", "Best datasets for benchmarking reasoning",
                "What evaluation sets are people using to measure reasoning progress beyond GSM8K?",
                "Benchmarks", false, now - daysToMillis(4), 19, 2, 7);
        addDummyPost("9", "isabel", "Isabel Flores", "Prompt: Daily reflection journaling partner",
                "Looking for a gentle prompt to help with reflective journaling without it feeling repetitive.",
                "Wellness", true, now - hoursToMillis(8), 24, 1, 4);
        addDummyPost("10", "james", "James Rivera", "Streaming responses with function calling",
                "Has anyone combined streaming outputs with function calls reliably?", "APIs", false,
                now - hoursToMillis(2), 29, 3, 11);
        addDummyPost("11", "kate", "Kate Andersen", "Prompt: Classroom debate facilitator",
                "Need a prompt that helps students explore both sides of a debate constructively.",
                "Education", true, now - daysToMillis(5), 33, 2, 16);
        addDummyPost("12", "leo", "Leon Wu", "Quantisation tricks for edge deployment",
                "Collected a few resources on 4-bit quantisation for running models on edge devices.",
                "Optimization", false, now - hoursToMillis(40), 21, 5, 9);
    }

    private void addDummyPost(String id,
                              String authorId,
                              String authorName,
                              String title,
                              String content,
                              String tag,
                              boolean isPromptPost,
                              long createdAtMillis,
                              int upvotes,
                              int downvotes,
                              int commentCount) {
        Post post = new Post();
        post.setId(id);
        post.setAuthor_id(authorId);
        post.setAuthor_name(authorName);
        post.setTitle(title);
        post.setContent(content);
        post.setLlm_tag(tag);
        post.setIs_prompt_post(isPromptPost);
        String created = Long.toString(createdAtMillis);
        post.setCreated_at(created);
        post.setUpdated_at(Long.toString(createdAtMillis + hoursToMillis(1)));
        post.setUpvotes(upvotes);
        post.setDownvotes(downvotes);
        post.setComment_count(commentCount);
        synchronized (SHARED_POSTS) {
            SHARED_POSTS.add(post);
        }
    }

    private long hoursToMillis(int hours) {
        return hours * 60L * 60L * 1000L;
    }

    private long daysToMillis(int days) {
        return hoursToMillis(24) * days;
    }
}