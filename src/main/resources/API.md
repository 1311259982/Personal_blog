# API Documentation

## Article Controller (`/api/posts`)

### 1. Create Articles

-   **Endpoint**: `POST /api/posts`
-   **Description**: Creates one or more new articles.
-   **Request Body**:
    ```json
    [
      {
        "title": "string",
        "content": "string",
        "slug": "string",
        "isPublished": "boolean"
        // other Post fields as applicable, userId will likely be set by backend
      }
    ]
    ```
    (List of `Post` objects)
-   **Responses**:
    -   `201 Created`: Successfully created the article(s).
        ```json
        {
          "code": 201,
          "message": "success",
          "data": null
        }
        ```

### 2. Get Article List

-   **Endpoint**: `GET /api/posts`
-   **Description**: Retrieves a paginated list of articles, with optional filtering by category or tag.
-   **Query Parameters**:
    -   `page` (Integer, optional): Page number (default: `0`)
    -   `size` (Integer, optional): Number of items per page (default: `10`)
    -   `sort` (String, optional): Sorting criteria (e.g., `createdAt,desc`, `title,asc`) (default: `createdAt,desc`)
    -   `categoryId` (Integer, optional): Filter by category ID.
    -   `tagName` (String, optional): Filter by tag name.
-   **Responses**:
    -   `200 OK`: Successfully retrieved the article list.
        ```json
        {
          "code": 200,
          "message": "success",
          "data": [
            {
              "id": 1,
              "userId": 1,
              "title": "Article Title 1",
              "content": "Article Content 1",
              "slug": "article-title-1",
              "views": 100,
              "isPublished": true,
              "createdAt": "2023-01-01T10:00:00",
              "updatedAt": "2023-01-01T10:00:00"
            },
            // ... more Post objects
          ]
        }
        ```

### 3. Get Article Detail

-   **Endpoint**: `GET /api/posts/{id}`
-   **Description**: Retrieves the details of a single article by its ID.
-   **Path Parameters**:
    -   `id` (Integer, required): The ID of the article.
-   **Responses**:
    -   `200 OK`: Successfully retrieved the article details.
        ```json
        {
          "code": 200,
          "message": "success",
          "data": {
            "id": 1,
            "userId": 1,
            "title": "Article Title 1",
            "content": "Article Content 1",
            "slug": "article-title-1",
            "views": 100,
            "isPublished": true,
            "createdAt": "2023-01-01T10:00:00",
            "updatedAt": "2023-01-01T10:00:00"
          }
        }
        ```
    -   `400 Bad Request`: Article not found.
        ```json
        {
          "code": 400,
          "message": "文章不存在",
          "data": null
        }
        ```

### 4. Update Article

-   **Endpoint**: `PUT /api/posts/{id}`
-   **Description**: Updates an existing article by its ID.
-   **Path Parameters**:
    -   `id` (Integer, required): The ID of the article to update.
-   **Request Body**:
    ```json
    {
      "title": "string",
      "content": "string",
      "isPublished": "boolean"
      // Only fields to be updated are required
    }
    ```
    (`Post` object with fields to update)
-   **Responses**:
    -   `200 OK`: Successfully updated the article.
        ```json
        {
          "code": 200,
          "message": "success",
          "data": null // or updated Post object
        }
        ```
    -   `400 Bad Request`: Article not found or update failed.
        ```json
        {
          "code": 400,
          "message": "文章不存在", // or "更新失败"
          "data": null
        }
        ```

### 5. Delete Article

-   **Endpoint**: `DELETE /api/posts/{id}`
-   **Description**: Deletes an article by its ID.
-   **Path Parameters**:
    -   `id` (Integer, required): The ID of the article to delete.
-   **Responses**:
    -   `200 OK`: Successfully deleted the article.
        ```json
        {
          "code": 200,
          "message": "success",
          "data": null
        }
        ```
    -   `400 Bad Request`: Deletion failed (e.g., article not found).
        ```json
        {
          "code": 400,
          "message": "删除失败",
          "data": null
        }
        ```

## Auth Controller (`/api/auth`)

### 1. Register User

-   **Endpoint**: `POST /api/auth/register`
-   **Description**: Registers a new user.
-   **Request Body**:
    ```json
    {
      "username": "string",
      "password": "string",
      "email": "user@example.com",
      "role": "USER"
    }
    ```
    (`User` object)
-   **Responses**:
    -   `200 OK`: User registered successfully.
        ```json
        {
          "code": 200,
          "message": "success",
          "data": null
        }
        ```
    -   `400 Bad Request`: Email already registered.
        ```json
        {
          "code": 400,
          "message": "邮箱已被注册",
          "data": null
        }
        ```

### 2. Login User

-   **Endpoint**: `POST /api/auth/login`
-   **Description**: Authenticates a user and returns login information.
-   **Request Body**:
    ```json
    {
      "email": "user@example.com",
      "password": "string"
    }
    ```
    (`User` object with email and password)
-   **Responses**:
    -   `200 OK`: Login successful.
        ```json
        {
          "code": 200,
          "message": "success",
          "data": {
            "token": "jwt_token_string",
            "userId": 1,
            "username": "string",
            "email": "user@example.com",
            "role": "USER"
          }
        }
        ```
    -   `400 Bad Request`: Login failed (e.g., invalid credentials).
        ```json
        {
          "code": 400,
          "message": "登录失败",
          "data": null
        }
        ```

## Comments Controller (`/api/comments`)

### 1. Create a Comment

-   **Endpoint**: `POST /api/comments`
-   **Description**: Creates a new comment for a post.
-   **Request Body**:
    ```json
    {
      "postId": 1,
      "userId": 1,
      "content": "This is a comment.",
      "parentId": null
    }
    ```
    (`Comment` object)
-   **Responses**:
    -   `200 OK`: Comment created successfully.
        ```json
        {
          "code": 200,
          "message": "success",
          "data": null
        }
        ```
    -   `400 Bad Request`: Failed to create the comment.
        ```json
        {
          "code": 400,
          "message": "评论失败",
          "data": null
        }
        ```

### 2. Get Comments by Post ID

-   **Endpoint**: `GET /api/comments/by-post/{postId}`
-   **Description**: Retrieves all comments for a specific post.
-   **Path Parameters**:
    -   `postId` (Long, required): The ID of the post.
-   **Responses**:
    -   `200 OK`: Successfully retrieved the comments.
        ```json
        {
          "code": 200,
          "message": "success",
          "data": [
            {
              "id": 1,
              "postId": 1,
              "userId": 1,
              "content": "This is a comment.",
              "parentId": null,
              "createdAt": "2023-01-01T12:00:00"
            }
            // ... more Comment objects
          ]
        }
        ```
    -   `400 Bad Request`: No comments found for the post.
        ```json
        {
          "code": 400,
          "message": "该文章暂无评论",
          "data": null
        }
        ```