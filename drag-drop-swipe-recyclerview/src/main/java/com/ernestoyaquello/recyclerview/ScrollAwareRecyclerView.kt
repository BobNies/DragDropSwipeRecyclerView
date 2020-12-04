package com.ernestoyaquello.recyclerview

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.RecyclerView
import com.ernestoyaquello.recyclerview.listener.OnListScrollListener
import com.ernestoyaquello.recyclerview.util.RecyclerViewUtil

/**
 * Extension of RecyclerView that detects when the user scrolls.
 */
open class ScrollAwareRecyclerView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : RecyclerView(context, attrs, defStyleAttr) {

    /**
     * Listener for the scrolling events.
     */
    var scrollListener: OnListScrollListener? = null

    // The current offset index of data you have loaded
    private var currentPage = 0
    // The total number of items in the dataset after the last load
    private var previousTotalItemCount = 0
    // True if we are still waiting for the scroller to settle
    private var isScrolling = true
    // Sets the starting page index
    private val startingPageIndex = 0

    // The minimum amount of items to have below your current scroll position before isScrolling more.
    var visibleThreshold = 3     //TODO - change layoutManager dynamically

    private val internalListScrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)

            when (newState) {
                SCROLL_STATE_IDLE -> {
                    scrollListener?.onListScrollStateChanged(OnListScrollListener.ScrollState.IDLE)
                }

                SCROLL_STATE_DRAGGING -> {
                    isScrolling = true
                    scrollListener?.onListScrollStateChanged(OnListScrollListener.ScrollState.DRAGGING)
                }

                SCROLL_STATE_SETTLING -> {
                    isScrolling = false
                    scrollListener?.onListScrollStateChanged(OnListScrollListener.ScrollState.SETTLING)
                }
            }
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {

            val lastVisibleItemPosition =
                    RecyclerViewUtil.getLastVisibleItemPosition(layoutManager)
            val totalItemCount = layoutManager?.itemCount ?: 0


            // If the total item count is zero and the previous isn't, assume the
            // list is invalidated and should be reset back to initial state
            if (totalItemCount < previousTotalItemCount) {
                currentPage = startingPageIndex
                previousTotalItemCount = totalItemCount
            }

            // If it’s still isScrolling, we check to see if the dataset count has
            // changed, if so we conclude it has finished isScrolling and update the current page
            // number and total item count.
            if (isScrolling && totalItemCount > previousTotalItemCount) {
                previousTotalItemCount = totalItemCount
            }

            // fire listener if he hit the TOP or BOTTOM of the list.
            if (!isScrolling) {
                if (lastVisibleItemPosition + visibleThreshold > totalItemCount) {
                    currentPage++
                    scrollListener?.onListScrolled(OnListScrollListener.ScrollDirection.BOTTOM, dy)
                    isScrolling = true
                } else {
                    val firstVisibleItemPosition =
                            RecyclerViewUtil.getFirstVisibleItemPosition(layoutManager)
                    if (firstVisibleItemPosition == 0) {
                        scrollListener?.onListScrolled(OnListScrollListener.ScrollDirection.TOP, dy)
                        isScrolling = true
                    }
                }
            }

        }
    }

    init {
        super.addOnScrollListener(internalListScrollListener)
    }

}