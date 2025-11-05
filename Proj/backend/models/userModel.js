const pool = require('../config/db');

const userModel = {
  async createUser(username, email, passwordHash) {
    const query = `
      INSERT INTO users (username, email, password_hash)
      VALUES ($1, $2, $3)
      RETURNING id, username, email
    `;
    const result = await pool.query(query, [username, email, passwordHash]);
    return result.rows[0];
  },

  async findByUsername(username) {
    const query = 'SELECT * FROM users WHERE username = $1';
    const result = await pool.query(query, [username]);
    return result.rows[0];
  },

  async findByEmail(email) {
    const query = 'SELECT * FROM users WHERE email = $1';
    const result = await pool.query(query, [email]);
    return result.rows[0];
  },

  async findById(id) {
    const query = 'SELECT id, username, email FROM users WHERE id = $1';
    const result = await pool.query(query, [id]);
    return result.rows[0];
  },

  async updateUser(id, username, email, passwordHash = null) {
    const query = `
      UPDATE users
      SET 
        username = COALESCE($1, username),
        email = COALESCE($2, email),
        password_hash = COALESCE($3, password_hash)
      WHERE id = $4
      RETURNING id, username, email
    `;
    const result = await pool.query(query, [username || null, email || null, passwordHash, id]);
    return result.rows[0];
  }
};

module.exports = userModel;

