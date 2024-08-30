package com.dev.agalperin.presentation

import android.net.Uri
import com.dev.agalperin.utils.ErrorType

sealed class HomeScreenEffect {
    data class ShowError(val error: ErrorType): HomeScreenEffect()
    data class ShowImageSavedSnackbar(val uri: Uri) : HomeScreenEffect()
}

