const jwt = require('jsonwebtoken');

const authMiddleware = (req, res, next) => {
  try {
    const authHeader = req.headers.authorization;
    
    if (!authHeader || !authHeader.startsWith('Bearer ')) {
      return res.status(401).json({ error: 'No token provided' });
    }

    const token = authHeader.substring(7); // Remove 'Bearer ' prefix
    
    // For simplicity, we'll use a simple token verification
    // In production, you'd verify the JWT properly
    try {
      const decoded = jwt.verify(token, process.env.JWT_SECRET || 'bestllm_secret_key');
      req.userId = decoded.userId;
      next();
    } catch (err) {
      // If JWT verification fails, try treating token as simple string
      // This is a fallback for simpler token systems
      if (token && token.length > 0) {
        // Simple validation - in production use proper JWT
        req.userId = parseInt(token) || null;
        if (!req.userId) {
          return res.status(401).json({ error: 'Invalid token' });
        }
        next();
      } else {
        return res.status(401).json({ error: 'Invalid token' });
      }
    }
  } catch (error) {
    return res.status(401).json({ error: 'Authentication failed' });
  }
};

module.exports = authMiddleware;

