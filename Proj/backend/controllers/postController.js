const postModel = require('../models/postModel');

const postController = {
  async getAllPosts(req, res) {
    try {
      const tag = req.query.tag || null;
      const posts = await postModel.findAll(tag);
      res.json(posts);
    } catch (error) {
      console.error('Get posts error:', error);
      res.status(500).json({ error: 'Internal server error' });
    }
  },

  async getPostById(req, res) {
    try {
      const { id } = req.params;
      const post = await postModel.findById(parseInt(id));

      if (!post) {
        return res.status(404).json({ error: 'Post not found' });
      }

      res.json(post);
    } catch (error) {
      console.error('Get post error:', error);
      res.status(500).json({ error: 'Internal server error' });
    }
  },

  async createPost(req, res) {
    try {
      const { title, content, llm_tag } = req.body;
      const authorId = req.userId;

      if (!title || !content) {
        return res.status(400).json({ error: 'Title and content are required' });
      }

      const post = await postModel.createPost(authorId, title, content, llm_tag || null);
      res.status(201).json(post);
    } catch (error) {
      console.error('Create post error:', error);
      res.status(500).json({ error: 'Internal server error' });
    }
  },

  async deletePost(req, res) {
    try {
      const { id } = req.params;
      const postId = parseInt(id);
      const authorId = req.userId;

      const deletedPost = await postModel.deletePost(postId, authorId);

      if (!deletedPost) {
        return res.status(404).json({ error: 'Post not found or unauthorized' });
      }

      res.json({ message: 'Post deleted successfully' });
    } catch (error) {
      console.error('Delete post error:', error);
      res.status(500).json({ error: 'Internal server error' });
    }
  }
};

module.exports = postController;

