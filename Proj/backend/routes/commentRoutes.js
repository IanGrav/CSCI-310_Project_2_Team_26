const express = require('express');
const router = express.Router();
const commentController = require('../controllers/commentController');
const authMiddleware = require('../middleware/authMiddleware');

router.get('/posts/:id/comments', commentController.getComments);
router.post('/posts/:id/comments', authMiddleware, commentController.createComment);

module.exports = router;

