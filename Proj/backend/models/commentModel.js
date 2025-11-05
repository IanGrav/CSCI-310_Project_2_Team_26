const pool = require('../config/db');

const commentModel = {
  async createComment(postId, authorId, text) {
    const query = `
      INSERT INTO comments (post_id, author_id, text)
      VALUES ($1, $2, $3)
      RETURNING *
    `;
    const result = await pool.query(query, [postId, authorId, text]);
    return result.rows[0];
  },

  async findByPostId(postId) {
    const query = `
      SELECT c.*, u.username as author_name
      FROM comments c
      LEFT JOIN users u ON c.author_id = u.id
      WHERE c.post_id = $1
      ORDER BY c.id ASC
    `;
    const result = await pool.query(query, [postId]);
    return result.rows;
  }
};

module.exports = commentModel;

