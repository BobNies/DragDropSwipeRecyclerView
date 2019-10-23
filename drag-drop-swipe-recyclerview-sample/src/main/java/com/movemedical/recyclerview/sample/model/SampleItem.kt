package com.movemedical.recyclerview.sample.model

data class SampleItem(
        val name: String,
        val price: String,
        val colorRed: Float,
        val colorGreen: Float,
        val colorBlue: Float,
        val isHeader: Boolean = false
)