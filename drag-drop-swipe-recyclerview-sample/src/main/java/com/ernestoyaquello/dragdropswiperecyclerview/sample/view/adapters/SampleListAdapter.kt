package com.ernestoyaquello.recyclerview.sample.view.adapters

import android.graphics.Canvas
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ernestoyaquello.recyclerview.DragDropSwipeAdapter
import com.ernestoyaquello.recyclerview.sample.databinding.ListItemVerticalListBinding
import com.ernestoyaquello.recyclerview.sample.model.SampleItem

/**
 * Sample Adapter
 */
class SampleListAdapter(sampleItems: List<SampleItem> = emptyList())
    : DragDropSwipeAdapter<SampleItem, SampleListAdapter.ViewHolder>(sampleItems) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: ListItemVerticalListBinding = ListItemVerticalListBinding.inflate(LayoutInflater.from(parent.context))

        return ViewHolder(view)
    }

    override fun onBindViewHolder(item: SampleItem, viewHolder: ViewHolder, position: Int) {
        val item = dataSet[position]
        viewHolder.binding?.item = item
    }

    override fun getViewToTouchToStartDraggingItem(item: SampleItem, viewHolder: ViewHolder, position: Int): View? {
        return viewHolder.binding?.dragIcon
    }

    override fun onDragStarted(item: SampleItem, viewHolder: ViewHolder) {

    }

    override fun onSwipeStarted(item: SampleItem, viewHolder: ViewHolder) {

    }

    override fun onIsDragging(
            item: SampleItem?,
            viewHolder: ViewHolder,
            offsetX: Int,
            offsetY: Int,
            canvasUnder: Canvas?,
            canvasOver: Canvas?,
            isUserControlled: Boolean) {
    }

    override fun onIsSwiping(
            item: SampleItem?,
            viewHolder: ViewHolder,
            offsetX: Int,
            offsetY: Int,
            canvasUnder: Canvas?,
            canvasOver: Canvas?,
            isUserControlled: Boolean) {
    }

    override fun onDragFinished(item: SampleItem, viewHolder: ViewHolder) {

    }

    override fun onSwipeAnimationFinished(viewHolder: ViewHolder) {

    }

    inner class ViewHolder(itemView: View) : DragDropSwipeAdapter.ViewHolder(itemView) {
        var binding: ListItemVerticalListBinding? = null

        constructor(binding: ListItemVerticalListBinding) : this(binding.root) {
            this.binding = binding
            this.binding?.executePendingBindings()
        }
    }
}