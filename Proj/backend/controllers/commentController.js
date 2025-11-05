const commentModel = require('../models/commentModel');

const commentController = {
  async getComments(req, res) {
    try {
      const { id } = req.params;
      const postId = parseInt(id);
      const comments = await commentModel.findByPostId(postId);
      res.json(comments);
    } catch (error) {
      console.error('Get comments error:', error);
      res.status(500).json({ error: 'Internal server error' });
    }
  },

  async createComment(req, res) {
    try {
      const { id } = req.params;
      const { text } = req.body;
      const postId = parseInt(id);
      const authorId = req.userId;

      if (!text) {
        return res.status(400).json({ error: 'Comment text is required' });
      }

      const comment = await commentModel.createComment(postId, authorId, text);
      res.status(201).json(comment);
    } catch (error) {
      console.error('Create comment error:', error);
      res.status(500).json({ error: 'Internal server error' });
    }
  }
};

module.exports = commentController;

