# **个人博客 API 文档**

## **通用响应格式**

所有 API 接口都遵循统一的响应结构：

```json
{
  "code": 200,
  "msg": "success",
  "data": {}
}
```

-   `code`: 状态码，`200` 表示成功，其他值表示失败。
-   `msg`: 响应消息，通常为 "success" 或错误信息。
-   `data`: 实际返回的数据，可以是对象、数组或 `null`。

---

## **1. 认证模块 (`/api/auth`)**

### **1.1 用户注册**

-   **功能**: 创建一个新用户账户。
-   **URL**: `/api/auth/register`
-   **方法**: `POST`
-   **请求体**:

    ```json
    {
      "username": "your_username",
      "email": "user@example.com",
      "password": "your_password",
      "role": "USER"
    }
    ```

### **1.2 用户登录**

-   **功能**: 用户登录并获取认证 Token。
-   **URL**: `/api/auth/login`
-   **方法**: `POST`
-   **请求体**:

    ```json
    {
      "email": "user@example.com",
      "password": "your_password"
    }
    ```

---

## **2. 文章模块 (`/api/posts`)**

*注意：除公共列表和详情接口外，其他接口均需要有效的 `Authorization: Bearer <token>` 请求头。*

### **2.1 创建文章**

-   **功能**: 创建一篇或多篇文章（支持批量创建）。
-   **URL**: `/api/posts`
-   **方法**: `POST`
-   **请求体**: `List<Post>`

    ```json
    [
      {
        "title": "我的第一篇文章",
        "content": "这是文章内容...",
        "isPublished": false
      }
    ]
    ```

    -   `isPublished`: `true` 表示直接发布，`false` 或 `null` 表示保存为草稿。

### **2.2 获取文章列表（公开）**

-   **功能**: 分页获取**已发布**的文章列表。
-   **URL**: `/api/posts`
-   **方法**: `GET`
-   **查询参数**:
    -   `page` (可选, 默认 `0`): 页码。
    -   `size` (可选, 默认 `10`): 每页数量。
    -   `sort` (可选, 默认 `createdAt,desc`): 排序字段和方向。
    -   `categoryId` (可选): 分类 ID。
    -   `tagName` (可选): 标签名称。

### **2.3 获取当前用户的文章（草稿箱/我的文章）**

-   **功能**: 获取当前登录用户的所有文章，可根据发布状态筛选。
-   **URL**: `/api/posts/my`
-   **方法**: `GET`
-   **查询参数**:
    -   `isPublished` (可选, 布尔类型): 根据发布状态筛选文章。
        -   `true`: 只返回已发布的文章。
        -   `false`: 只返回草稿状态的文章（用于草稿箱）。
        -   如果此参数省略，则返回所有文章。

### **2.4 获取单篇文章详情**

-   **功能**: 根据 ID 获取单篇文章的详细内容。
-   **URL**: `/api/posts/{id}`
-   **方法**: `GET`

### **2.5 更新文章**

-   **功能**: 根据 ID 更新一篇文章的完整内容。
-   **URL**: `/api/posts/{id}`
-   **方法**: `PUT`
-   **请求体**:

    ```json
    {
      "title": "更新后的标题",
      "content": "更新后的内容...",
      "isPublished": false
    }
    ```

### **2.6 发布草稿**

-   **功能**: 将一篇指定的草稿发布为公开文章。
-   **URL**: `/api/posts/{id}/publish`
-   **方法**: `PUT`
-   **请求体**: 无
-   **成功响应 (200 OK)**:
    ```json
    {
      "code": 200,
      "msg": "success",
      "data": null
    }
    ```
-   **失败响应 (权限不足)**:
    ```json
    {
      "code": 500,
      "msg": "权限不足，无法发布此文章",
      "data": null
    }
    ```

### **2.7 删除文章**

-   **功能**: 根据 ID 删除一篇文章（或草稿）。
-   **URL**: `/api/posts/{id}`
-   **方法**: `DELETE`

---

## **3. 可拓展的功能接口 (预留)**

以下是为未来功能预留的接口，当前尚未实现，可作为后续开发方向。

### **3.1 分类模块 (`/api/categories`)**

-   `GET /api/categories`: 获取所有分类的列表。
-   `GET /api/categories/{id}/posts`: 获取某个分类下的所有已发布文章。
-   `POST /api/categories`: (管理员) 创建新分类。
-   `PUT /api/categories/{id}`: (管理员) 更新分类信息。
-   `DELETE /api/categories/{id}`: (管理员) 删除分类。

### **3.2 标签模块 (`/api/tags`)**

-   `GET /api/tags`: 获取所有标签的列表（可用于标签云）。
-   `GET /api/tags/{tagName}/posts`: 获取带有某个标签的所有已发布文章。
-   `PUT /api/posts/{id}/tags`: 为指定文章批量设置标签，请求体为 `["Java", "Spring"]`。

### **3.3 评论模块 (`/api/comments`)**

-   `GET /api/posts/{postId}/comments`: 获取一篇文章的所有评论（可分页）。
-   `POST /api/posts/{postId}/comments`: (用户) 为文章添加新评论。
-   `DELETE /api/comments/{id}`: (用户/管理员) 删除一条评论。

### **3.4 用户模块 (`/api/users`)**

-   `GET /api/users/me`: 获取当前登录用户的详细信息（如邮箱、头像、简介等）。
-   `PUT /api/users/me`: 更新当前用户的信息。
-   `GET /api/users/{id}`: 获取任一用户的公开信息（用于展示作者主页）。

### **3.5 搜索模块 (`/api/search`)**

-   `GET /api/search?q={keyword}`: 根据关键词在所有已发布的文章中进行全文搜索。
