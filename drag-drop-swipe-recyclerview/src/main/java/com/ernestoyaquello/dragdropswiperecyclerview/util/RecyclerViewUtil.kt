package com.ernestoyaquello.dragdropswiperecyclerview.util

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * MIT License
 * Copyright (c) 2017 Chetan Sachdeva
 * Modifications copyright (C) 2019 MoveMedical/RNies
 * https://github.com/chetdeva/recyclerview-bindings
 *
 */

object RecyclerViewUtil {

    fun computeVisibleThreshold(layoutManager: RecyclerView.LayoutManager,
                                visibleThreshold: Int): Int =
            when (layoutManager) {
                is LinearLayoutManager -> visibleThreshold
                is GridLayoutManager -> layoutManager.spanCount * visibleThreshold
                else -> 0
            }

    fun getLastVisibleItemPosition(
            layoutManager: RecyclerView.LayoutManager?
    ): Int {
        layoutManager?.let {
            return when (layoutManager) {
                is LinearLayoutManager -> layoutManager.findLastVisibleItemPosition()
                is GridLayoutManager -> layoutManager.findLastVisibleItemPosition()
                else -> 0
            }
        }
        return 0
    }

    fun getFirstVisibleItemPosition(
            layoutManager: RecyclerView.LayoutManager?
    ): Int {
        layoutManager?.let {
            return when (layoutManager) {
                is LinearLayoutManager -> layoutManager.findFirstVisibleItemPosition()
                is GridLayoutManager -> layoutManager.findFirstVisibleItemPosition()
                else -> 0
            }
        }
        return 0
    }
}
