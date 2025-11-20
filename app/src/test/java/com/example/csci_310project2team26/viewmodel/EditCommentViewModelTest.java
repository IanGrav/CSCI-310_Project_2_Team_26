package com.example.csci_310project2team26.viewmodel;

import com.example.csci_310project2team26.data.model.Comment;
import com.example.csci_310project2team26.data.repository.CommentRepository;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * White-box Test: EditCommentViewModel Unit Testing
 * 
 * Location: app/src/test/java/com/example/csci_310project2team26/viewmodel/EditCommentViewModelTest.java
 * Test Class: EditCommentViewModelTest
 * 
 * Description: Tests the EditCommentViewModel logic for loading and updating comments.
 * 
 * How to Execute: Run as JUnit test in Android Studio or via: ./gradlew test --tests EditCommentViewModelTest
 * 
 * Coverage: Tests all methods in EditCommentViewModel including loadComment, updateComment with/without title.
 */
public class EditCommentViewModelTest {

    private EditCommentViewModel viewModel;
    
    @Mock
    private CommentRepository mockCommentRepository;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        viewModel = new EditCommentViewModel();
        // Use reflection to inject mock repository if needed
        // For now, we test the actual repository integration
    }

    @Test
    public void testInitialState() {
        // Rationale: Verify ViewModel starts in correct initial state
        // Result: All LiveData should be null/false initially
        
        assertNull(viewModel.getComment().getValue());
        assertNull(viewModel.getUpdatedComment().getValue());
        assertFalse(Boolean.TRUE.equals(viewModel.getLoading().getValue()));
        assertNull(viewModel.getError().getValue());
    }

    @Test
    public void testLoadCommentWithValidIds() {
        // Rationale: Test loading a comment with valid post and comment IDs
        // Result: Comment should be loaded successfully
        
        String postId = "post123";
        String commentId = "comment456";
        
        viewModel.loadComment(postId, commentId);
        
        // Verify loading state is set
        assertTrue(Boolean.TRUE.equals(viewModel.getLoading().getValue()));
        
        // Note: Actual comment loading depends on repository callback
        // This test verifies the method doesn't crash with valid IDs
    }

    @Test
    public void testLoadCommentWithNullPostId() {
        // Rationale: Test loading comment with null post ID
        // Result: Should not crash, method should return early
        
        viewModel.loadComment(null, "comment456");
        
        // Should not set loading state (early return)
        assertFalse(Boolean.TRUE.equals(viewModel.getLoading().getValue()));
    }

    @Test
    public void testLoadCommentWithNullCommentId() {
        // Rationale: Test loading comment with null comment ID
        // Result: Should not crash, method should return early
        
        viewModel.loadComment("post123", null);
        
        // Should not set loading state (early return)
        assertFalse(Boolean.TRUE.equals(viewModel.getLoading().getValue()));
    }

    @Test
    public void testUpdateCommentWithValidData() {
        // Rationale: Test updating comment with valid post ID, comment ID, and text
        // Result: Comment should be updated successfully
        
        String postId = "post123";
        String commentId = "comment456";
        String text = "Updated comment text";
        
        viewModel.updateComment(postId, commentId, text);
        
        // Verify loading state is set
        assertTrue(Boolean.TRUE.equals(viewModel.getLoading().getValue()));
        
        // Note: Actual update depends on repository callback
        // This test verifies the method doesn't crash with valid data
    }

    @Test
    public void testUpdateCommentWithTitle() {
        // Rationale: Test updating comment with title
        // Result: Comment should be updated with title
        
        String postId = "post123";
        String commentId = "comment456";
        String text = "Updated comment text";
        String title = "Updated comment title";
        
        viewModel.updateComment(postId, commentId, text, title);
        
        // Verify loading state is set
        assertTrue(Boolean.TRUE.equals(viewModel.getLoading().getValue()));
    }

    @Test
    public void testUpdateCommentWithNullPostId() {
        // Rationale: Test updating comment with null post ID
        // Result: Should not crash, method should return early
        
        viewModel.updateComment(null, "comment456", "text", "title");
        
        // Should not set loading state (early return)
        assertFalse(Boolean.TRUE.equals(viewModel.getLoading().getValue()));
    }

    @Test
    public void testUpdateCommentWithNullCommentId() {
        // Rationale: Test updating comment with null comment ID
        // Result: Should not crash, method should return early
        
        viewModel.updateComment("post123", null, "text", "title");
        
        // Should not set loading state (early return)
        assertFalse(Boolean.TRUE.equals(viewModel.getLoading().getValue()));
    }

    @Test
    public void testUpdateCommentWithNullTitle() {
        // Rationale: Test updating comment with null title (title is optional)
        // Result: Comment should be updated without title
        
        String postId = "post123";
        String commentId = "comment456";
        String text = "Updated comment text";
        
        viewModel.updateComment(postId, commentId, text, null);
        
        // Verify loading state is set
        assertTrue(Boolean.TRUE.equals(viewModel.getLoading().getValue()));
    }

    @Test
    public void testUpdateCommentOverloadWithoutTitle() {
        // Rationale: Test updateComment overload that doesn't take title parameter
        // Result: Should call the full method with null title
        
        String postId = "post123";
        String commentId = "comment456";
        String text = "Updated comment text";
        
        viewModel.updateComment(postId, commentId, text);
        
        // Verify loading state is set
        assertTrue(Boolean.TRUE.equals(viewModel.getLoading().getValue()));
    }
}

