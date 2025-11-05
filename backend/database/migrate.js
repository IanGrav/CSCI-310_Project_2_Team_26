/**
 * Database Migration Script
 * Run this to set up the database schema
 * 
 * Usage: node database/migrate.js
 */

require('dotenv').config();
const fs = require('fs');
const path = require('path');
const { query } = require('../config/database');

async function migrate() {
  try {
    console.log('🔄 Starting database migration...');

    // Read schema file
    const schemaPath = path.join(__dirname, 'schema.sql');
    const schema = fs.readFileSync(schemaPath, 'utf8');

    // Split by semicolons and execute each statement
    const statements = schema
      .split(';')
      .map(s => s.trim())
      .filter(s => s.length > 0 && !s.startsWith('--'));

    console.log(`📝 Found ${statements.length} SQL statements to execute`);

    for (let i = 0; i < statements.length; i++) {
      const statement = statements[i];
      if (statement.trim()) {
        try {
          await query(statement);
          console.log(`✅ Executed statement ${i + 1}/${statements.length}`);
        } catch (error) {
          // Ignore "already exists" errors
          if (error.message.includes('already exists') || 
              error.message.includes('duplicate')) {
            console.log(`⚠️  Statement ${i + 1} skipped (already exists)`);
          } else {
            throw error;
          }
        }
      }
    }

    // Run seed data
    console.log('🌱 Running seed data...');
    const seedPath = path.join(__dirname, 'seed.sql');
    if (fs.existsSync(seedPath)) {
      const seed = fs.readFileSync(seedPath, 'utf8');
      const seedStatements = seed
        .split(';')
        .map(s => s.trim())
        .filter(s => s.length > 0 && !s.startsWith('--'));

      for (const statement of seedStatements) {
        if (statement.trim()) {
          try {
            await query(statement);
          } catch (error) {
            // Ignore duplicate key errors
            if (!error.message.includes('duplicate key')) {
              console.error('Seed error:', error.message);
            }
          }
        }
      }
    }

    console.log('✅ Database migration completed successfully!');
    process.exit(0);
  } catch (error) {
    console.error('❌ Migration failed:', error);
    process.exit(1);
  }
}

migrate();

