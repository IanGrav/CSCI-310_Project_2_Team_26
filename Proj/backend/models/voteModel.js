const pool = require('../config/db');

const voteModel = {
  async findVote(userId, postId) {
    const query = 'SELECT * FROM votes WHERE user_id = $1 AND post_id = $2';
    const result = await pool.query(query, [userId, postId]);
    return result.rows[0];
  },

  async createVote(userId, postId, type) {
    const query = `
      INSERT INTO votes (user_id, post_id, type)
      VALUES ($1, $2, $3)
      RETURNING *
    `;
    const result = await pool.query(query, [userId, postId, type]);
    return result.rows[0];
  },

  async updateVote(userId, postId, type) {
    const query = `
      UPDATE votes
      SET type = $1
      WHERE user_id = $2 AND post_id = $3
      RETURNING *
    `;
    const result = await pool.query(query, [type, userId, postId]);
    return result.rows[0];
  },

  async deleteVote(userId, postId) {
    const query = 'DELETE FROM votes WHERE user_id = $1 AND post_id = $2 RETURNING *';
    const result = await pool.query(query, [userId, postId]);
    return result.rows[0];
  }
};

module.exports = voteModel;

