# Connectivity 23

A forum built with Java that allows users to create, browse, and interact with posts.

## Features

- **User Authentication**: Register, login, and logout with session management
- **Post Creation & Browsing**: Create posts with titles and content, browse all posts
- **Post Interactions**: Upvote/downvote posts, reply to posts and other replies
- **Profile Management**: Edit user profiles with bio and profile picture support
- **Translation**: Translate post content to multiple languages via Google Translate API

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

## API Usage

### Google Translate API
Used for translating post content. Requires an API key stored in `secrets.properties`.

Endpoint: `https://translation.googleapis.com/language/translate/v2`

This project was created for CSC207 at UofT
