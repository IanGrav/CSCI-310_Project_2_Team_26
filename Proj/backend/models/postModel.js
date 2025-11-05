const pool = require('../config/db');

const postModel = {
  async createPost(authorId, title, content, llmTag) {
    const query = `
      INSERT INTO posts (author_id, title, content, llm_tag, votes)
      VALUES ($1, $2, $3, $4, 0)
      RETURNING *
    `;
    const result = await pool.query(query, [authorId, title, content, llmTag]);
    return result.rows[0];
  },

  async findAll(tag = null) {
    let query = `
      SELECT p.*, u.username as author_name
      FROM posts p
      LEFT JOIN users u ON p.author_id = u.id
    `;
    const params = [];
    
    if (tag) {
      query += ' WHERE p.llm_tag = $1';
      params.push(tag);
    }
    
    query += ' ORDER BY p.id DESC';
    const result = await pool.query(query, params);
    return result.rows;
  },

  async findById(id) {
    const query = `
      SELECT p.*, u.username as author_name
      FROM posts p
      LEFT JOIN users u ON p.author_id = u.id
      WHERE p.id = $1
    `;
    const result = await pool.query(query, [id]);
    return result.rows[0];
  },

  async deletePost(id, authorId) {
    const query = 'DELETE FROM posts WHERE id = $1 AND author_id = $2 RETURNING *';
    const result = await pool.query(query, [id, authorId]);
    return result.rows[0];
  },

  async updateVoteCount(postId, delta) {
    const query = 'UPDATE posts SET votes = votes + $1 WHERE id = $2 RETURNING votes';
    const result = await pool.query(query, [delta, postId]);
    return result.rows[0]?.votes || 0;
  }
};

module.exports = postModel;

