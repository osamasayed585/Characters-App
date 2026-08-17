package com.droidos.data.paging

/**
 * Drops items whose id has already been emitted by the owning [androidx.paging.PagingSource].
 *
 * The remote window can shift between page requests — a character added or removed upstream
 * pushes rows across the page boundary — so two consecutive pages can legitimately contain the
 * same id. Once the list is keyed by id, that duplicate is no longer a cosmetic repeated row:
 * Compose throws `IllegalArgumentException: Key "N" was already used`.
 *
 * State is deliberately scoped to a single PagingSource instance. Paging builds a new instance
 * on every invalidation, which is exactly when the presented list is rebuilt from scratch, so
 * the set of seen ids resets at precisely the right moment.
 *
 * Filtering is O(1) per item via [MutableSet.add], which reports whether the id was new.
 *
 * Caveat: this assumes a page is never loaded twice within one generation. That holds while
 * `PagingConfig.maxSize` is unbounded, as it is for both pagers here. Setting `maxSize` enables
 * page dropping, and a dropped page re-loaded on scroll-back would be filtered away to nothing.
 * If `maxSize` is ever introduced, this tracker has to become page-keyed rather than a flat set.
 */
class DistinctIdTracker<T : Any>(
    private val idOf: (T) -> Int,
) {
    private val seenIds = mutableSetOf<Int>()

    /**
     * Returns the items of [page] whose ids have not been seen yet, in their original order,
     * and records those ids as seen.
     */
    fun retainNew(page: List<T>): List<T> =
        synchronized(seenIds) {
            page.filter { seenIds.add(idOf(it)) }
        }
}
