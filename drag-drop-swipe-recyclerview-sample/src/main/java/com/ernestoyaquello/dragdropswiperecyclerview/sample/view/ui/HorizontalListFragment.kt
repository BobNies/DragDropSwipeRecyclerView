package com.ernestoyaquello.recyclerview.sample.view.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ernestoyaquello.recyclerview.DragDropSwipeRecyclerView
import com.ernestoyaquello.recyclerview.sample.R
import com.ernestoyaquello.recyclerview.sample.currentListFragmentConfig
import com.ernestoyaquello.recyclerview.sample.databinding.FragmentHorizontalListBinding


class HorizontalListFragment : BaseListFragment() {

    private lateinit var binding: FragmentHorizontalListBinding
    override val optionsMenuId = R.menu.fragment_horizontal_list_options

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_horizontal_list, container, false)
        binding.lifecycleOwner = viewLifecycleOwner

        loadingIndicator = binding.loadingIndicator

        list = binding.list
        list.adapter = adapter
        list.swipeListener = onItemSwipeListener
        list.dragListener = onItemDragListener
        list.scrollListener = onListScrollListener

        list.layoutManager = LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false)
        list.reduceItemAlphaOnSwiping = currentListFragmentConfig.isUsingFadeOnSwipedItems

        list.orientation = if (currentListFragmentConfig.isRestrictingDraggingDirections) {

            DragDropSwipeRecyclerView.ListOrientation.HORIZONTAL_LIST_WITH_HORIZONTAL_DRAGGING
        } else {
            DragDropSwipeRecyclerView.ListOrientation.HORIZONTAL_LIST_WITH_UNCONSTRAINED_DRAGGING
        }

        setupLayoutBehindItemLayoutOnSwiping()

        return binding.root
    }

    private fun setupLayoutBehindItemLayoutOnSwiping() {
        // We set to null all the properties that can be used to display something behind swiped items
        // In XML: app:swiped_item_bg_color="@null"
        list.behindSwipedItemBackgroundColor = null

        // In XML: app:swiped_item_bg_color_secondary="@null"
        list.behindSwipedItemBackgroundSecondaryColor = null

        // In XML: app:swiped_item_icon="@null"
        list.behindSwipedItemIconDrawableId = null

        // In XML: app:swiped_item_icon_secondary="@null"
        list.behindSwipedItemIconSecondaryDrawableId = null

        // In XML: app:swiped_item_custom_layout="@null"
        list.behindSwipedItemLayoutId = null

        // In XML: app:swiped_item_custom_layout_secondary="@null"
        list.behindSwipedItemSecondaryLayoutId = null

        val currentContext = context
        if (currentListFragmentConfig.isDrawingBehindSwipedItems && currentContext != null)
            if (currentListFragmentConfig.isUsingStandardItemLayout) {
                // We set certain properties to show an icon and a background colour behind swiped items
                // In XML: app:swiped_item_icon="@drawable/ic_remove_item"
                list.behindSwipedItemIconDrawableId = R.drawable.ic_remove_item

                // In XML: app:swiped_item_icon_secondary="@drawable/ic_archive_item"
                list.behindSwipedItemIconSecondaryDrawableId = R.drawable.ic_archive_item

                // In XML: app:swiped_item_bg_color="@color/swipeBehindBackground"
                list.behindSwipedItemBackgroundColor = ContextCompat.getColor(currentContext, R.color.swipeBehindBackground)

                // In XML: app:swiped_item_bg_color_secondary="@color/swipeBehindBackgroundSecondary"
                list.behindSwipedItemBackgroundSecondaryColor = ContextCompat.getColor(currentContext, R.color.swipeBehindBackgroundSecondary)

                // In XML: app:swiped_item_icon_centered="true"
                list.behindSwipedItemCenterIcon = true
            } else {
                // We set our custom layouts to be displayed behind swiped items
                // In XML: app:swiped_item_custom_layout="@layout/swiped_horizontal_list"
                list.behindSwipedItemLayoutId = R.layout.behind_swiped_horizontal_list

                // In XML: app:swiped_item_custom_layout_secondary="@layout/swiped_horizontal_list_secondary"
                list.behindSwipedItemSecondaryLayoutId = R.layout.behind_swiped_horizontal_list_secondary
            }
    }


    companion object {
        fun newInstance() = HorizontalListFragment()
    }
}