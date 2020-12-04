package com.ernestoyaquello.recyclerview.sample.view.ui

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.ernestoyaquello.recyclerview.DragDropSwipeRecyclerView
import com.ernestoyaquello.recyclerview.listener.OnItemDragListener
import com.ernestoyaquello.recyclerview.listener.OnItemSwipeListener
import com.ernestoyaquello.recyclerview.listener.OnListScrollListener
import com.ernestoyaquello.recyclerview.sample.R
import com.ernestoyaquello.recyclerview.sample.ListFragmentType
import com.ernestoyaquello.recyclerview.sample.currentListFragmentConfig
import com.ernestoyaquello.recyclerview.sample.currentListFragmentType
import com.ernestoyaquello.recyclerview.sample.model.SampleItem
import com.ernestoyaquello.recyclerview.sample.persist.BaseRepository
import com.ernestoyaquello.recyclerview.sample.persist.SampleItemRepository
import com.ernestoyaquello.recyclerview.sample.view.adapters.SampleListAdapter

/**
 * The base implementation of a fragment that displays a list of ice creams.
 */
abstract class BaseListFragment : Fragment() {

    val adapter = SampleListAdapter()
    private val repository = SampleItemRepository.getInstance()

    lateinit var list: DragDropSwipeRecyclerView
    lateinit var loadingIndicator: ProgressBar

    protected abstract val optionsMenuId: Int

    val onItemSwipeListener = object : OnItemSwipeListener<SampleItem> {
        override fun onItemSwiped(position: Int, direction: OnItemSwipeListener.SwipeDirection, item: SampleItem): Boolean {
            when (direction) {
                OnItemSwipeListener.SwipeDirection.RIGHT_TO_LEFT -> onItemSwipedLeft(item, position)
                OnItemSwipeListener.SwipeDirection.LEFT_TO_RIGHT -> onItemSwipedRight(item, position)
                OnItemSwipeListener.SwipeDirection.DOWN_TO_UP -> onItemSwipedUp(item, position)
                OnItemSwipeListener.SwipeDirection.UP_TO_DOWN -> onItemSwipedDown(item, position)
            }

            return false
        }
    }

    val onItemDragListener = object : OnItemDragListener<SampleItem> {
        override fun onItemDragged(previousPosition: Int, newPosition: Int, item: SampleItem) {

        }

        override fun onItemDropped(initialPosition: Int, finalPosition: Int, item: SampleItem) {
            if (initialPosition != finalPosition) {

                // Change item position inside the repository
                repository.removeItem(item)
                repository.insertItem(item, finalPosition)
            }
        }
    }

    val onListScrollListener = object : OnListScrollListener {
        override fun onListScrollStateChanged(scrollState: OnListScrollListener.ScrollState) {
            // Call commented out to avoid saturating the log
            //Log.v("bob","List scroll state changed to $scrollState")
        }

        override fun onListScrolled(scrollDirection: OnListScrollListener.ScrollDirection, distance: Int) {
            // Call commented out to avoid saturating the log
            Log.v("bob", "List scrolled To $scrollDirection")
        }
    }

    val onItemAddedListener = object : BaseRepository.OnItemAdditionListener<SampleItem> {
        override fun onItemAdded(item: SampleItem, position: Int) {
            // Add the item to the adapter's data set if necessary
            if (!adapter.dataSet.contains(item)) {
                adapter.insertItem(position, item)

                // We scroll to the position of the added item (positions match in both adapter and repository)
                list.smoothScrollToPosition(position)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) = inflater.inflate(optionsMenuId, menu)

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {

            R.id.change_item_layout ->
                currentListFragmentConfig.isUsingStandardItemLayout = !currentListFragmentConfig.isUsingStandardItemLayout

            R.id.change_dragging_restrictions ->
                currentListFragmentConfig.isRestrictingDraggingDirections = !currentListFragmentConfig.isRestrictingDraggingDirections

            R.id.enable_disable_drawing_behind ->
                currentListFragmentConfig.isDrawingBehindSwipedItems = !currentListFragmentConfig.isDrawingBehindSwipedItems

            R.id.enable_disable_fade_on_swiping ->
                currentListFragmentConfig.isUsingFadeOnSwipedItems = !currentListFragmentConfig.isUsingFadeOnSwipedItems
        }

        // Reload the whole fragment to apply the changes
        // (notifying changes on the data set wouldn't be enough because of view recycling)
        val fragmentManager = activity?.supportFragmentManager
        if (fragmentManager != null) {
            val fragment: BaseListFragment = when (currentListFragmentType) {
                ListFragmentType.VERTICAL -> VerticalListFragment.newInstance()
                ListFragmentType.HORIZONTAL -> HorizontalListFragment.newInstance()
                ListFragmentType.GRID -> GridListFragment.newInstance()
            }
            fragmentManager.beginTransaction().apply {
                replace(R.id.content_frame, fragment, tag)
            }.commit()
        }

        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)

        updateMenuItemTitles(menu)
    }

    private fun updateMenuItemTitles(menu: Menu) {

        // Update the menu titles depending on the selected options
        for (index in 0 until menu.size()) {
            val menuItem = menu.getItem(index)
            when (menuItem.itemId) {

                R.id.change_item_layout ->
                    menuItem.title = getString(
                            if (currentListFragmentConfig.isUsingStandardItemLayout)
                                R.string.action_use_card_view_item_layout
                            else
                                R.string.action_use_default_item_layout)

                R.id.change_dragging_restrictions ->
                    menuItem.title = getString(
                            if (currentListFragmentConfig.isRestrictingDraggingDirections)
                                R.string.action_not_restrict_dragging
                            else
                                R.string.action_restrict_dragging)

                R.id.enable_disable_drawing_behind ->
                    menuItem.title = getString(
                            if (currentListFragmentConfig.isDrawingBehindSwipedItems)
                                R.string.action_not_draw_behind_swiped_items
                            else
                                R.string.action_draw_behind_swiped_items)

                R.id.enable_disable_fade_on_swiping ->
                    menuItem.title = getString(
                            if (currentListFragmentConfig.isUsingFadeOnSwipedItems)
                                R.string.action_not_reduce_alpha_on_swiping
                            else
                                R.string.action_reduce_alpha_on_swiping)
            }
        }
    }

    override fun onPause() {
        super.onPause()

        // Unsubscribe from repository changes
        repository.removeOnItemAdditionListener(onItemAddedListener)
    }

    override fun onResume() {
        super.onResume()

        // Subscribe to repository changes and then reload items
        repository.addOnItemAdditionListener(onItemAddedListener)
        reloadItems()
    }

    private fun reloadItems() {
        // Show loader view
        loadingIndicator.visibility = View.VISIBLE
        list.visibility = View.GONE

        loadData()

        // Hide loader view after a small delay to simulate real data retrieval
        Handler().postDelayed({
            loadingIndicator.visibility = View.GONE
            list.visibility = View.VISIBLE
        }, 150)
    }

    private fun loadData() {
        adapter.dataSet = repository.getAllItems()
    }

    private fun onItemSwipedLeft(item: SampleItem, position: Int) {
        removeItem(item, position)
    }

    private fun onItemSwipedRight(item: SampleItem, position: Int) {
        archiveItem(item, position)
    }

    private fun onItemSwipedUp(item: SampleItem, position: Int) {
        archiveItem(item, position)
    }

    private fun onItemSwipedDown(item: SampleItem, position: Int) {
        removeItem(item, position)
    }

    private fun removeItem(item: SampleItem, position: Int) {
        removeItemFromList(item, position, R.string.itemRemovedMessage)
    }

    private fun archiveItem(item: SampleItem, position: Int) {
        removeItemFromList(item, position, R.string.itemArchivedMessage)
    }

    private fun removeItemFromList(item: SampleItem, position: Int, stringResourceId: Int) {
        repository.removeItem(item)

        val itemSwipedSnackBar = Snackbar.make(view!!, getString(stringResourceId, item), Snackbar.LENGTH_SHORT)
        itemSwipedSnackBar.setAction(getString(R.string.undoCaps)) {
            repository.insertItem(item, position)
        }

        itemSwipedSnackBar.show()
    }
}