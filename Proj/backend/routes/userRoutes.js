const express = require('express');
const router = express.Router();
const userController = require('../controllers/userController');
const authMiddleware = require('../middleware/authMiddleware');

router.get('/:id', userController.getUserProfile);
router.put('/:id', authMiddleware, userController.updateUser);

module.exports = router;

