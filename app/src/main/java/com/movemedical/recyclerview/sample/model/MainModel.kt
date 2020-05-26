package com.movemedical.recyclerview.sample.model

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.library.baseAdapters.BR

class MainModel : BaseObservable() {

    @get:Bindable
    var id: Int = 0
        set(id) {
            field = id
            notifyPropertyChanged(BR.id)
        }

    @get:Bindable
    var name: String = ""
        set(name) {
            field = name
            notifyPropertyChanged(BR.name)
        }

    @get:Bindable
    var resetLoadingState: Boolean = false
        set(resetLoadingState) {
            field = resetLoadingState
            notifyPropertyChanged(BR.resetLoadingState)
        }

    @get:Bindable
    var visibleThreshold: Int = 0
        set(visibleThreshold) {
            field = visibleThreshold
            notifyPropertyChanged(BR.visibleThreshold)
        }
}