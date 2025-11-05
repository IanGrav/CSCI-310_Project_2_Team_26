const voteModel = require('../models/voteModel');
const postModel = require('../models/postModel');

const voteController = {
  async votePost(req, res) {
    try {
      const { id } = req.params;
      const { type } = req.body;
      const postId = parseInt(id);
      const userId = req.userId;

      if (!type || !['up', 'down'].includes(type)) {
        return res.status(400).json({ error: 'Vote type must be "up" or "down"' });
      }

      // Find existing vote
      const existingVote = await voteModel.findVote(userId, postId);

      let voteDelta = 0;

      if (existingVote) {
        if (existingVote.type === type) {
          // User is voting the same way - remove the vote
          await voteModel.deleteVote(userId, postId);
          voteDelta = type === 'up' ? -1 : 1;
        } else {
          // User is changing their vote
          await voteModel.updateVote(userId, postId, type);
          voteDelta = type === 'up' ? 2 : -2; // Changed from down to up (+2) or up to down (-2)
        }
      } else {
        // New vote
        await voteModel.createVote(userId, postId, type);
        voteDelta = type === 'up' ? 1 : -1;
      }

      // Update post vote count
      const newVoteCount = await postModel.updateVoteCount(postId, voteDelta);

      res.json({
        message: 'Vote recorded',
        votes: newVoteCount
      });
    } catch (error) {
      console.error('Vote error:', error);
      res.status(500).json({ error: 'Internal server error' });
    }
  }
};

module.exports = voteController;

