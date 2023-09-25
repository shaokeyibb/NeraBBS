import {ComputedRef} from "vue";

export default function useLayout() {

    const compatQuery = useMediaQuery("(max-width: 600px)")
    const mediumQuery = useMediaQuery("(max-width: 840px)")

    function getSizeClass(): ComputedRef<'compact' | 'medium' | 'expanded'> {
        return computed(() => {
            if (compatQuery.value) {
                return 'compact'
            } else if (mediumQuery.value) {
                return 'medium'
            } else {
                return 'expanded'
            }
        })
    }

    function getPanes(): ComputedRef<number> {
        return computed(() => mediumQuery.value ? 1 : 2)
    }

    return {
        getSizeClass,
        getPanes
    }
}