package com.movemedical.recyclerview.sample.model

open class SampleItem(
    open val isHeader: Boolean = false,
    open val isConstruct: Boolean = false,
    open var id: String = "",
    val title: String = "",
    val description: String = "",
    val isDraggable: Boolean = true,
    val isSwipable: Boolean = true,
    val isDisable: Boolean = false
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is SampleItem) return false

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}
