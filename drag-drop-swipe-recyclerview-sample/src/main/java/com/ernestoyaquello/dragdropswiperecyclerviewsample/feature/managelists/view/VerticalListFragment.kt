package com.ernestoyaquello.dragdropswiperecyclerviewsample.feature.managelists.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.ernestoyaquello.dragdropswiperecyclerview.DragDropSwipeRecyclerView
import com.ernestoyaquello.dragdropswiperecyclerviewsample.R
import com.ernestoyaquello.dragdropswiperecyclerviewsample.config.local.currentListFragmentConfig
import com.ernestoyaquello.dragdropswiperecyclerviewsample.databinding.FragmentVerticalListBinding
import com.ernestoyaquello.dragdropswiperecyclerviewsample.feature.managelists.view.base.BaseListFragment

class VerticalListFragment : BaseListFragment() {

    private lateinit var binding: FragmentVerticalListBinding
    override val optionsMenuId = R.menu.fragment_vertical_list_options

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_vertical_list, container, false)
        binding.lifecycleOwner = viewLifecycleOwner

        loadingIndicator = binding.loadingIndicator

        list = binding.list
        list.adapter = adapter
        list.swipeListener = onItemSwipeListener
        list.dragListener = onItemDragListener
        list.scrollListener = onListScrollListener

        list.layoutManager = LinearLayoutManager(activity)
        list.reduceItemAlphaOnSwiping = currentListFragmentConfig.isUsingFadeOnSwipedItems

        list.orientation = if (currentListFragmentConfig.isRestrictingDraggingDirections) {
            DragDropSwipeRecyclerView.ListOrientation.VERTICAL_LIST_WITH_VERTICAL_DRAGGING
        } else {
            DragDropSwipeRecyclerView.ListOrientation.VERTICAL_LIST_WITH_UNCONSTRAINED_DRAGGING
        }
        if (currentListFragmentConfig.isUsingStandardItemLayout) {
            setStandardItemLayoutAndDivider()
        } else {
            setCardViewItemLayoutAndNoDivider()
        }

        setupLayoutBehindItemLayoutOnSwiping()

        return binding.root
    }


    private fun setStandardItemLayoutAndDivider() {
        list.itemLayoutId = R.layout.list_item_vertical_list
        list.dividerDrawableId = R.drawable.divider_vertical_list
    }

    private fun setCardViewItemLayoutAndNoDivider() {
        list.itemLayoutId = R.layout.list_item_vertical_list_cardview
        list.dividerDrawableId = null
    }

    private fun setupLayoutBehindItemLayoutOnSwiping() {
        // We set to null all the properties that can be used to display something behind swiped items
        // In XML: app:behind_swiped_item_bg_color="@null"
        list.behindSwipedItemBackgroundColor = null

        // In XML: app:behind_swiped_item_bg_color_secondary="@null"
        list.behindSwipedItemBackgroundSecondaryColor = null

        // In XML: app:behind_swiped_item_icon="@null"
        list.behindSwipedItemIconDrawableId = null

        // In XML: app:behind_swiped_item_icon_secondary="@null"
        list.behindSwipedItemIconSecondaryDrawableId = null

        // In XML: app:behind_swiped_item_custom_layout="@null"
        list.behindSwipedItemLayoutId = null

        // In XML: app:behind_swiped_item_custom_layout_secondary="@null"
        list.behindSwipedItemSecondaryLayoutId = null

        val currentContext = context
        if (currentListFragmentConfig.isDrawingBehindSwipedItems && currentContext != null)
            if (currentListFragmentConfig.isUsingStandardItemLayout) {
                // We set certain properties to show an icon and a background colour behind swiped items
                // In XML: app:behind_swiped_item_icon="@drawable/ic_remove_item"
                list.behindSwipedItemIconDrawableId = R.drawable.ic_remove_item

                // In XML: app:behind_swiped_item_icon_secondary="@drawable/ic_archive_item"
                list.behindSwipedItemIconSecondaryDrawableId = R.drawable.ic_archive_item

                // In XML: app:behind_swiped_item_bg_color="@color/swipeBehindBackground"
                list.behindSwipedItemBackgroundColor = ContextCompat.getColor(currentContext, R.color.swipeBehindBackground)

                // In XML: app:behind_swiped_item_bg_color_secondary="@color/swipeBehindBackgroundSecondary"
                list.behindSwipedItemBackgroundSecondaryColor = ContextCompat.getColor(currentContext, R.color.swipeBehindBackgroundSecondary)

                // In XML: app:behind_swiped_item_icon_margin="@dimen/spacing_normal"
                list.behindSwipedItemIconMargin = resources.getDimension(R.dimen.spacing_normal)
            } else {
                // We set our custom layouts to be displayed behind swiped items
                // In XML: app:behind_swiped_item_custom_layout="@layout/behind_swiped_vertical_list"
                list.behindSwipedItemLayoutId = R.layout.behind_swiped_vertical_list

                // In XML: app:behind_swiped_item_custom_layout_secondary="@layout/behind_swiped_vertical_list_secondary"
                list.behindSwipedItemSecondaryLayoutId = R.layout.behind_swiped_vertical_list_secondary
            }
    }

    companion object {
        fun newInstance() = VerticalListFragment()
    }
}