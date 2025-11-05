package com.bestllm.data.database;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import kotlin.Unit;
import kotlin.coroutines.Continuation;

@SuppressWarnings({"unchecked", "deprecation"})
public final class PostDao_Impl implements PostDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<PostEntity> __insertionAdapterOfPostEntity;

  private final EntityDeletionOrUpdateAdapter<PostEntity> __updateAdapterOfPostEntity;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAllPosts;

  public PostDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfPostEntity = new EntityInsertionAdapter<PostEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `posts` (`id`,`author_id`,`title`,`content`,`llm_tag`,`votes`,`author_name`) VALUES (?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final PostEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getAuthor_id());
        if (entity.getTitle() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getTitle());
        }
        if (entity.getContent() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getContent());
        }
        if (entity.getLlm_tag() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getLlm_tag());
        }
        statement.bindLong(6, entity.getVotes());
        if (entity.getAuthor_name() == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.getAuthor_name());
        }
      }
    };
    this.__updateAdapterOfPostEntity = new EntityDeletionOrUpdateAdapter<PostEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `posts` SET `id` = ?,`author_id` = ?,`title` = ?,`content` = ?,`llm_tag` = ?,`votes` = ?,`author_name` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final PostEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getAuthor_id());
        if (entity.getTitle() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getTitle());
        }
        if (entity.getContent() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getContent());
        }
        if (entity.getLlm_tag() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getLlm_tag());
        }
        statement.bindLong(6, entity.getVotes());
        if (entity.getAuthor_name() == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.getAuthor_name());
        }
        statement.bindLong(8, entity.getId());
      }
    };
    this.__preparedStmtOfDeleteAllPosts = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM posts";
        return _query;
      }
    };
  }

  @Override
  public Object insertPost(final PostEntity post, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfPostEntity.insert(post);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertPosts(final List<PostEntity> posts,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfPostEntity.insert(posts);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updatePost(final PostEntity post, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfPostEntity.handle(post);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteAllPosts(final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteAllPosts.acquire();
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfDeleteAllPosts.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public LiveData<List<PostEntity>> getAllPosts() {
    final String _sql = "SELECT * FROM posts ORDER BY id DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return __db.getInvalidationTracker().createLiveData(new String[] {"posts"}, false, new Callable<List<PostEntity>>() {
      @Override
      @Nullable
      public List<PostEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfAuthorId = CursorUtil.getColumnIndexOrThrow(_cursor, "author_id");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfContent = CursorUtil.getColumnIndexOrThrow(_cursor, "content");
          final int _cursorIndexOfLlmTag = CursorUtil.getColumnIndexOrThrow(_cursor, "llm_tag");
          final int _cursorIndexOfVotes = CursorUtil.getColumnIndexOrThrow(_cursor, "votes");
          final int _cursorIndexOfAuthorName = CursorUtil.getColumnIndexOrThrow(_cursor, "author_name");
          final List<PostEntity> _result = new ArrayList<PostEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final PostEntity _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final int _tmpAuthor_id;
            _tmpAuthor_id = _cursor.getInt(_cursorIndexOfAuthorId);
            final String _tmpTitle;
            if (_cursor.isNull(_cursorIndexOfTitle)) {
              _tmpTitle = null;
            } else {
              _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            }
            final String _tmpContent;
            if (_cursor.isNull(_cursorIndexOfContent)) {
              _tmpContent = null;
            } else {
              _tmpContent = _cursor.getString(_cursorIndexOfContent);
            }
            final String _tmpLlm_tag;
            if (_cursor.isNull(_cursorIndexOfLlmTag)) {
              _tmpLlm_tag = null;
            } else {
              _tmpLlm_tag = _cursor.getString(_cursorIndexOfLlmTag);
            }
            final int _tmpVotes;
            _tmpVotes = _cursor.getInt(_cursorIndexOfVotes);
            final String _tmpAuthor_name;
            if (_cursor.isNull(_cursorIndexOfAuthorName)) {
              _tmpAuthor_name = null;
            } else {
              _tmpAuthor_name = _cursor.getString(_cursorIndexOfAuthorName);
            }
            _item = new PostEntity(_tmpId,_tmpAuthor_id,_tmpTitle,_tmpContent,_tmpLlm_tag,_tmpVotes,_tmpAuthor_name);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public LiveData<List<PostEntity>> getPostsByTag(final String tag) {
    final String _sql = "SELECT * FROM posts WHERE llm_tag = ? ORDER BY id DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (tag == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, tag);
    }
    return __db.getInvalidationTracker().createLiveData(new String[] {"posts"}, false, new Callable<List<PostEntity>>() {
      @Override
      @Nullable
      public List<PostEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfAuthorId = CursorUtil.getColumnIndexOrThrow(_cursor, "author_id");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfContent = CursorUtil.getColumnIndexOrThrow(_cursor, "content");
          final int _cursorIndexOfLlmTag = CursorUtil.getColumnIndexOrThrow(_cursor, "llm_tag");
          final int _cursorIndexOfVotes = CursorUtil.getColumnIndexOrThrow(_cursor, "votes");
          final int _cursorIndexOfAuthorName = CursorUtil.getColumnIndexOrThrow(_cursor, "author_name");
          final List<PostEntity> _result = new ArrayList<PostEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final PostEntity _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final int _tmpAuthor_id;
            _tmpAuthor_id = _cursor.getInt(_cursorIndexOfAuthorId);
            final String _tmpTitle;
            if (_cursor.isNull(_cursorIndexOfTitle)) {
              _tmpTitle = null;
            } else {
              _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            }
            final String _tmpContent;
            if (_cursor.isNull(_cursorIndexOfContent)) {
              _tmpContent = null;
            } else {
              _tmpContent = _cursor.getString(_cursorIndexOfContent);
            }
            final String _tmpLlm_tag;
            if (_cursor.isNull(_cursorIndexOfLlmTag)) {
              _tmpLlm_tag = null;
            } else {
              _tmpLlm_tag = _cursor.getString(_cursorIndexOfLlmTag);
            }
            final int _tmpVotes;
            _tmpVotes = _cursor.getInt(_cursorIndexOfVotes);
            final String _tmpAuthor_name;
            if (_cursor.isNull(_cursorIndexOfAuthorName)) {
              _tmpAuthor_name = null;
            } else {
              _tmpAuthor_name = _cursor.getString(_cursorIndexOfAuthorName);
            }
            _item = new PostEntity(_tmpId,_tmpAuthor_id,_tmpTitle,_tmpContent,_tmpLlm_tag,_tmpVotes,_tmpAuthor_name);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public LiveData<PostEntity> getPostById(final int postId) {
    final String _sql = "SELECT * FROM posts WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, postId);
    return __db.getInvalidationTracker().createLiveData(new String[] {"posts"}, false, new Callable<PostEntity>() {
      @Override
      @Nullable
      public PostEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfAuthorId = CursorUtil.getColumnIndexOrThrow(_cursor, "author_id");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfContent = CursorUtil.getColumnIndexOrThrow(_cursor, "content");
          final int _cursorIndexOfLlmTag = CursorUtil.getColumnIndexOrThrow(_cursor, "llm_tag");
          final int _cursorIndexOfVotes = CursorUtil.getColumnIndexOrThrow(_cursor, "votes");
          final int _cursorIndexOfAuthorName = CursorUtil.getColumnIndexOrThrow(_cursor, "author_name");
          final PostEntity _result;
          if (_cursor.moveToFirst()) {
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final int _tmpAuthor_id;
            _tmpAuthor_id = _cursor.getInt(_cursorIndexOfAuthorId);
            final String _tmpTitle;
            if (_cursor.isNull(_cursorIndexOfTitle)) {
              _tmpTitle = null;
            } else {
              _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            }
            final String _tmpContent;
            if (_cursor.isNull(_cursorIndexOfContent)) {
              _tmpContent = null;
            } else {
              _tmpContent = _cursor.getString(_cursorIndexOfContent);
            }
            final String _tmpLlm_tag;
            if (_cursor.isNull(_cursorIndexOfLlmTag)) {
              _tmpLlm_tag = null;
            } else {
              _tmpLlm_tag = _cursor.getString(_cursorIndexOfLlmTag);
            }
            final int _tmpVotes;
            _tmpVotes = _cursor.getInt(_cursorIndexOfVotes);
            final String _tmpAuthor_name;
            if (_cursor.isNull(_cursorIndexOfAuthorName)) {
              _tmpAuthor_name = null;
            } else {
              _tmpAuthor_name = _cursor.getString(_cursorIndexOfAuthorName);
            }
            _result = new PostEntity(_tmpId,_tmpAuthor_id,_tmpTitle,_tmpContent,_tmpLlm_tag,_tmpVotes,_tmpAuthor_name);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
