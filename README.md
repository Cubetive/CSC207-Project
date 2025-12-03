# Connectivity 23

A forum built with Java that allows users to create, browse, and interact with posts.

## Features

- **User Authentication**: Signup, login, and logout with session management
- **Browsing, Reading & Searching Posts**: Browse & read all posts; search for posts with specific titles
- **Post Creation**: Create posts with titles and content
- **Post Interactions**: Upvote/downvote posts; reply to other posts to have discussions
- **Profile Management**: Edit user profile's username, bio, password, profile picture etc.
- **Translation**: Translate post content to multiple languages via Google Translate API
- **Post Editing**: Edit posts to update thoughts
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
- **posts.json** - Forum posts and replies
- **votesRecording.json** - Forum tracking on whether upvote or downvote has been done on posts

## API Usage

### Google Translate API
Used for translating post content. Requires an API key stored in `secrets.properties`.

Endpoint: `https://translation.googleapis.com/language/translate/v2`

This project was created for CSC207 at UofT
