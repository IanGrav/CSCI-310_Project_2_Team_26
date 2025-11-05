const userModel = require('../models/userModel');
const bcrypt = require('bcrypt');

const userController = {
  async getUserProfile(req, res) {
    try {
      const { id } = req.params;
      const user = await userModel.findById(parseInt(id));

      if (!user) {
        return res.status(404).json({ error: 'User not found' });
      }

      res.json(user);
    } catch (error) {
      console.error('Get user error:', error);
      res.status(500).json({ error: 'Internal server error' });
    }
  },

  async updateUser(req, res) {
    try {
      const { id } = req.params;
      const { username, email, password } = req.body;
      const userId = parseInt(id);

      // Only allow users to update their own profile
      if (req.userId !== userId) {
        return res.status(403).json({ error: 'Forbidden' });
      }

      // Check if username is already taken
      if (username) {
        const existingUser = await userModel.findByUsername(username);
        if (existingUser && existingUser.id !== userId) {
          return res.status(400).json({ error: 'Username already exists' });
        }
      }

      // Check if email is already taken
      if (email) {
        const existingEmail = await userModel.findByEmail(email);
        if (existingEmail && existingEmail.id !== userId) {
          return res.status(400).json({ error: 'Email already exists' });
        }
      }

      let passwordHash = null;
      if (password && password.length > 0) {
        passwordHash = await bcrypt.hash(password, 10);
      }

      const user = await userModel.updateUser(userId, username || null, email || null, passwordHash);

      if (!user) {
        return res.status(404).json({ error: 'User not found' });
      }

      res.json(user);
    } catch (error) {
      console.error('Update user error:', error);
      res.status(500).json({ error: 'Internal server error' });
    }
  }
};

module.exports = userController;

