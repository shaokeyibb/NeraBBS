export type Message = {
    message: string
}

export type PostCreation = Partial<Pick<Post, 'title'>> & Pick<Post, 'content'>

export type Post = {
    id: number,
    posterID: number,
    title?: string | undefined,
    content: string,
    createAt: string
}

export type PreviewPost = Post

export type UserInfo = {
    id: number,
    username: string,
    email: string,
    createAt: string
}