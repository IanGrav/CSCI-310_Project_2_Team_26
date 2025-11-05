const express = require('express');
const router = express.Router();
const voteController = require('../controllers/voteController');
const authMiddleware = require('../middleware/authMiddleware');

router.post('/posts/:id/vote', authMiddleware, voteController.votePost);

module.exports = router;

