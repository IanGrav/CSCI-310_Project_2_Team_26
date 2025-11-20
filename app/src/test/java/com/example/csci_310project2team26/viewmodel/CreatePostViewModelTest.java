package com.example.csci_310project2team26.viewmodel;

import com.example.csci_310project2team26.data.model.Post;
import com.example.csci_310project2team26.data.repository.PostRepository;

import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

/**
 * White-box Test: CreatePostViewModel Unit Testing
 * 
 * Location: app/src/test/java/com/example/csci_310project2team26/viewmodel/CreatePostViewModelTest.java
 * Test Class: CreatePostViewModelTest
 * 
 * Description: Tests the CreatePostViewModel validation logic for creating regular and prompt posts.
 * 
 * How to Execute: Run as JUnit test in Android Studio or via: ./gradlew test --tests CreatePostViewModelTest
 * 
 * Coverage: Tests validation for title, tag, content, and prompt sections.
 */
public class CreatePostViewModelTest {

    private CreatePostViewModel viewModel;
    private CountDownLatch latch;

    @Before
    public void setUp() {
        viewModel = new CreatePostViewModel();
        latch = new CountDownLatch(1);
    }

    @Test
    public void testInitialState() {
        // Rationale: Verify ViewModel starts in correct initial state
        // Result: All LiveData should be null/false initially
        
        assertNull(viewModel.getCreatedPost().getValue());
        assertFalse(Boolean.TRUE.equals(viewModel.getLoading().getValue()));
        assertNull(viewModel.getError().getValue());
    }

    @Test
    public void testCreatePostWithEmptyTitle() throws InterruptedException {
        // Rationale: Test validation for empty title
        // Result: Error message "Title is required" should be set
        
        viewModel.getError().observeForever(error -> {
            if (error != null && error.equals("Title is required")) {
                latch.countDown();
            }
        });
        
        viewModel.createPost("", "Content", "GPT-4", false, null, null);
        
        latch.await(2, TimeUnit.SECONDS);
        
        assertEquals("Title is required", viewModel.getError().getValue());
        assertFalse(Boolean.TRUE.equals(viewModel.getLoading().getValue()));
    }

    @Test
    public void testCreatePostWithEmptyTag() throws InterruptedException {
        // Rationale: Test validation for empty tag (tag is required)
        // Result: Error message "Tag is required" should be set
        
        viewModel.getError().observeForever(error -> {
            if (error != null && error.equals("Tag is required")) {
                latch.countDown();
            }
        });
        
        viewModel.createPost("Title", "Content", "", false, null, null);
        
        latch.await(2, TimeUnit.SECONDS);
        
        assertEquals("Tag is required", viewModel.getError().getValue());
        assertFalse(Boolean.TRUE.equals(viewModel.getLoading().getValue()));
    }

    @Test
    public void testCreateRegularPostWithEmptyContent() throws InterruptedException {
        // Rationale: Test validation for regular post with empty content
        // Result: Error message "Content is required" should be set
        
        viewModel.getError().observeForever(error -> {
            if (error != null && error.equals("Content is required")) {
                latch.countDown();
            }
        });
        
        viewModel.createPost("Title", "", "GPT-4", false, null, null);
        
        latch.await(2, TimeUnit.SECONDS);
        
        assertEquals("Content is required", viewModel.getError().getValue());
        assertFalse(Boolean.TRUE.equals(viewModel.getLoading().getValue()));
    }

    @Test
    public void testCreatePromptPostWithoutPromptSections() throws InterruptedException {
        // Rationale: Test validation for prompt post without prompt or description sections
        // Result: Error message about prompt sections should be set
        
        viewModel.getError().observeForever(error -> {
            if (error != null && error.contains("prompt section")) {
                latch.countDown();
            }
        });
        
        viewModel.createPost("Title", "", "GPT-4", true, null, null);
        
        latch.await(2, TimeUnit.SECONDS);
        
        assertTrue(viewModel.getError().getValue().contains("prompt section"));
        assertFalse(Boolean.TRUE.equals(viewModel.getLoading().getValue()));
    }

    @Test
    public void testCreatePromptPostWithPromptSection() {
        // Rationale: Test creating prompt post with prompt section (valid)
        // Result: Should attempt to create post (loading should be set)
        
        viewModel.createPost("Title", "", "GPT-4", true, "Prompt section", null);
        
        // Should set loading state (validation passed)
        assertTrue(Boolean.TRUE.equals(viewModel.getLoading().getValue()));
    }

    @Test
    public void testCreatePromptPostWithDescriptionSection() {
        // Rationale: Test creating prompt post with description section (valid)
        // Result: Should attempt to create post (loading should be set)
        
        viewModel.createPost("Title", "", "GPT-4", true, null, "Description section");
        
        // Should set loading state (validation passed)
        assertTrue(Boolean.TRUE.equals(viewModel.getLoading().getValue()));
    }

    @Test
    public void testCreatePromptPostWithBothSections() {
        // Rationale: Test creating prompt post with both prompt and description sections (valid)
        // Result: Should attempt to create post (loading should be set)
        
        viewModel.createPost("Title", "", "GPT-4", true, "Prompt section", "Description section");
        
        // Should set loading state (validation passed)
        assertTrue(Boolean.TRUE.equals(viewModel.getLoading().getValue()));
    }

    @Test
    public void testCreateRegularPostWithValidData() {
        // Rationale: Test creating regular post with all required fields (valid)
        // Result: Should attempt to create post (loading should be set)
        
        viewModel.createPost("Title", "Content", "GPT-4", false, null, null);
        
        // Should set loading state (validation passed)
        assertTrue(Boolean.TRUE.equals(viewModel.getLoading().getValue()));
    }

    @Test
    public void testCreatePostTrimsWhitespace() {
        // Rationale: Test that whitespace is trimmed from inputs
        // Result: Should attempt to create post with trimmed values
        
        viewModel.createPost("  Title  ", "  Content  ", "  GPT-4  ", false, null, null);
        
        // Should set loading state (validation passed, trimming happens in repository)
        assertTrue(Boolean.TRUE.equals(viewModel.getLoading().getValue()));
    }
}

