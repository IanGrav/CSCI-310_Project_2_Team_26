Current plan: 


UI and app logic in android studio (XML and Java)

  - Initial screen for login or registration
    - if registration need to redirect to profile creation page
    - also need a page for updating profile info
  - Screen that shows regular posts, as well as their number of upvotes and downvotes and their number of comments (scrollable)
    - Need to get posts and their info via HTTP GET
    - Allows the user to upvote or downvote (or both?)
      - Seperate screen when clicking post which shows its comments and their upvotes/downvotes
        - Allows the user to comment (popup on bottom of screen maybe? - need some kind of textbox and submit button)
      - Need to send vote updates and new comments to database via HTTP POST
      - Need to make the page searchable by tag, author, title, and full text
        - search bar with dropdown to select what kind of search
  - Screen than shows prompt posts
    - basically all the same as above but for prompt posts
  - Screen for top k trending posts (we could just have the screen for posts be automatically sorted by top K trending. Or maybe we have a selection for newest posts, top posts, etc.)
  - Screen for the user to create a post or Prompt Post
    - Need to update post information with HTTP POST
  - need some way to edit posts and comments, either a screen that shows a log of the user's posts and comments with an option to edit each one, or an option to edit them in the main post and comment page, or both)


Retrofit library to handle HTTP GETs and POSTs (Java)


NodeJS server written in Javascript


PostgreSQL database
  - holds users and user info/profiles, posts, comments, and their upvotes and downvotes


Servers and database written in our github and hosted on railway








