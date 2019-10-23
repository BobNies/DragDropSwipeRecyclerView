package com.movemedical.recyclerview.sample.view.ui

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
import com.movemedical.recyclerview.DragDropSwipeRecyclerView
import com.movemedical.recyclerview.listener.OnItemDragListener
import com.movemedical.recyclerview.listener.OnItemSwipeListener
import com.movemedical.recyclerview.listener.OnListScrollListener
import com.movemedical.recyclerview.sample.R
import com.movemedical.recyclerview.sample.ListFragmentType
import com.movemedical.recyclerview.sample.currentListFragmentConfig
import com.movemedical.recyclerview.sample.currentListFragmentType
import com.movemedical.recyclerview.sample.model.IceCream
import com.movemedical.recyclerview.sample.persist.BaseRepository
import com.movemedical.recyclerview.sample.persist.IceCreamRepository
import com.movemedical.recyclerview.sample.util.Logger
import com.movemedical.recyclerview.sample.view.adapters.IceCreamListAdapter

/**
 * The base implementation of a fragment that displays a list of ice creams.
 */
abstract class BaseListFragment : Fragment() {

    val adapter = IceCreamListAdapter()
    private val repository = IceCreamRepository.getInstance()

    lateinit var list: DragDropSwipeRecyclerView
    lateinit var loadingIndicator: ProgressBar

    protected abstract val optionsMenuId: Int

    val onItemSwipeListener = object : OnItemSwipeListener<IceCream> {
        override fun onItemSwiped(position: Int, direction: OnItemSwipeListener.SwipeDirection, item: IceCream): Boolean {
            when (direction) {
                OnItemSwipeListener.SwipeDirection.RIGHT_TO_LEFT -> onItemSwipedLeft(item, position)
                OnItemSwipeListener.SwipeDirection.LEFT_TO_RIGHT -> onItemSwipedRight(item, position)
                OnItemSwipeListener.SwipeDirection.DOWN_TO_UP -> onItemSwipedUp(item, position)
                OnItemSwipeListener.SwipeDirection.UP_TO_DOWN -> onItemSwipedDown(item, position)
            }

            return false
        }
    }

    val onItemDragListener = object : OnItemDragListener<IceCream> {
        override fun onItemDragged(previousPosition: Int, newPosition: Int, item: IceCream) {
            Logger.log("$item is being dragged from position $previousPosition to position $newPosition")
        }

        override fun onItemDropped(initialPosition: Int, finalPosition: Int, item: IceCream) {
            if (initialPosition != finalPosition) {
                Logger.log("$item moved (dragged from position $initialPosition and dropped in position $finalPosition)")

                // Change item position inside the repository
                repository.removeItem(item)
                repository.insertItem(item, finalPosition)
            } else {
                Logger.log("$item dragged from (and also dropped in) the position $initialPosition")
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

    val onItemAddedListener = object : BaseRepository.OnItemAdditionListener<IceCream> {
        override fun onItemAdded(item: IceCream, position: Int) {
            // Add the item to the adapter's data set if necessary
            if (!adapter.dataSet.contains(item)) {
                Logger.log("Added new item $item")

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

    private fun onItemSwipedLeft(item: IceCream, position: Int) {
        Logger.log("$item (position $position) swiped to the left")

        removeItem(item, position)
    }

    private fun onItemSwipedRight(item: IceCream, position: Int) {
        Logger.log("$item (position $position) swiped to the right")

        archiveItem(item, position)
    }

    private fun onItemSwipedUp(item: IceCream, position: Int) {
        Logger.log("$item (position $position) swiped up")

        archiveItem(item, position)
    }

    private fun onItemSwipedDown(item: IceCream, position: Int) {
        Logger.log("$item (position $position) swiped down")

        removeItem(item, position)
    }

    private fun removeItem(item: IceCream, position: Int) {
        Logger.log("Removed item $item")

        removeItemFromList(item, position, R.string.itemRemovedMessage)
    }

    private fun archiveItem(item: IceCream, position: Int) {
        Logger.log("Archived item $item")

        removeItemFromList(item, position, R.string.itemArchivedMessage)
    }

    private fun removeItemFromList(item: IceCream, position: Int, stringResourceId: Int) {
        repository.removeItem(item)

        val itemSwipedSnackBar = Snackbar.make(view!!, getString(stringResourceId, item), Snackbar.LENGTH_SHORT)
        itemSwipedSnackBar.setAction(getString(R.string.undoCaps)) {
            Logger.log("UNDO: $item has been added back to the list in the position $position")

            repository.insertItem(item, position)
        }
        itemSwipedSnackBar.show()
    }
}