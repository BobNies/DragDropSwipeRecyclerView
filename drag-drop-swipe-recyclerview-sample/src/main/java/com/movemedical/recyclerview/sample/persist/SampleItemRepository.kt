package com.movemedical.recyclerview.sample.persist

import com.movemedical.recyclerview.sample.model.SampleItem

import java.util.*
import kotlin.math.floor

/**
 * Sample data
 */
class SampleItemRepository : BaseRepository<SampleItem>() {

    override fun generateNewItem(): SampleItem {
        val item = SampleItem(randomName(), randomPrice(), randomColor(), randomColor(), randomColor())
        addItem(item)

        return item
    }

    private fun randomName() = "New Item ${floor(Math.random() * 100)}"

    private fun randomPrice() = (80..500).random().toFloat() / 100f

    private fun randomColor() = (80..210).random().toFloat() / 255f

    private fun ClosedRange<Int>.random() =
            Random().nextInt((endInclusive + 1) - start) +  start

    companion object {
        private var instance: SampleItemRepository? = null

        fun getInstance(): SampleItemRepository {
            if (instance == null)
                instance = SampleItemRepository()

            return instance as SampleItemRepository
        }
    }
}