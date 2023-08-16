import {RouteLocationRaw} from "vue-router";

async function withHandleUnauthorize<T>(rst: Promise<T>, onUnauthorize: (e: any) => void): Promise<T> {
    try {
        return await rst
    } catch (e: any) {
        if (e.status === 401) {
            onUnauthorize(e)
            return rst;
        }
        throw e
    }
}

export async function withJumpUnauthorize<T>(rst: Promise<T>, to: RouteLocationRaw): Promise<T> {
    return withHandleUnauthorize(rst, () => {
        useRouter().push(to)
    })
}