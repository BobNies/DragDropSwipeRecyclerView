package com.ernestoyaquello.dragdropswiperecyclerviewsample.feature.managelists.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import com.ernestoyaquello.dragdropswiperecyclerview.DragDropSwipeRecyclerView
import com.ernestoyaquello.dragdropswiperecyclerviewsample.R
import com.ernestoyaquello.dragdropswiperecyclerviewsample.config.local.currentListFragmentConfig
import com.ernestoyaquello.dragdropswiperecyclerviewsample.databinding.FragmentGridListBinding
import com.ernestoyaquello.dragdropswiperecyclerviewsample.feature.managelists.view.base.BaseListFragment

class GridListFragment : BaseListFragment() {

    private lateinit var binding: FragmentGridListBinding
    private val numberOfColumns = 2

    override val optionsMenuId = R.menu.fragment_grid_list_options

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_grid_list, container, false)
        binding.lifecycleOwner = viewLifecycleOwner

        loadingIndicator = binding.loadingIndicator

        list = binding.list
        list.adapter = adapter
        list.swipeListener = onItemSwipeListener
        list.dragListener = onItemDragListener
        list.scrollListener = onListScrollListener
        list.orientation

        list.layoutManager = GridLayoutManager(activity, numberOfColumns)

        setupListOrientation()
        setupLayoutBehindItemLayoutOnSwiping()
        setupFadeItemLayoutOnSwiping()

        return binding.root
    }

    private fun setupListOrientation() {
        // It is necessary to set the orientation in code so the list can work correctly.
        // Horizontal swiping is specified because this grid list is vertically scrollable.
        // For horizontally scrollable grid lists, vertical swiping should be used instead.
        list.orientation = DragDropSwipeRecyclerView.ListOrientation.GRID_LIST_WITH_HORIZONTAL_SWIPING

        // We set this property to stop the grid list from drawing top dividers in the first row
        list.numOfColumnsPerRowInGridList = numberOfColumns
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
                // In XML: app:behind_swiped_item_custom_layout="@layout/behind_swiped_grid_list"
                list.behindSwipedItemLayoutId = R.layout.behind_swiped_grid_list

                // In XML: app:behind_swiped_item_custom_layout_secondary="@layout/behind_swiped_grid_list_secondary"
                list.behindSwipedItemSecondaryLayoutId = R.layout.behind_swiped_grid_list_secondary
            }
    }

    private fun setupFadeItemLayoutOnSwiping() {
        // In XML: app:swiped_item_opacity_fades_on_swiping="true/false"
        list.reduceItemAlphaOnSwiping = currentListFragmentConfig.isUsingFadeOnSwipedItems
    }

    companion object {
        fun newInstance() = GridListFragment()
    }
}