# Connectivity 23

A Forum page (built with Java), where users can browse posts, create posts to share their thoughts and ideas on various topics, and have discussions through replies & threads. We created a place of free discussion.

## Features

- **User Authentication**: Signup, login, and logout with session management
- **Browsing, Reading & Searching Posts**: Browse & read all posts; search for posts with specific titles
- **Post Creation**: Create posts with a title and content
- **Post Interactions**: Upvote/downvote posts; reply to other posts to have discussions
- **Profile Management**: Edit user's profile username, bio, password, profile picture, etc.
- **Translation**: Translate post content to multiple languages via Google Cloud Translation API
- **Post Editing**: Edit post content with updated thoughts
- **Post Referencing**: Reference/link others' posts in your own

## Architecture

```
src/main/java/
├── entities/               # Domain entities
├── use_case/               # Business logic interactors
├── interface_adapter/      # Controllers, Presenters, ViewModels
├── view/                   # GUI components
└── data_access/            # Data persistence
```

## Data Storage

- **users.csv** - User account data
- **posts.json** - Forum posts and replies data
- **votesRecording.json** - Forum tracking on upvoted/downvoted posts by the user

## API Usage

### Google Cloud Translation API
Used for translating post content. Requires an API key stored in `secrets.properties`.

Endpoint: `https://translation.googleapis.com/language/translate/v2`

This project was created for CSC207 at UofT
