package com.fabirt.weatherforecast.core.error

sealed class Failure {
    abstract val title: String
    abstract val message: String

    object LocationPermissionNotGrantedFailure : Failure() {
        override val title: String
            get() = "Permissions Denied"

        override val message: String
            get() = "Please allow location permissions in order to obtain weather information for your current location."
    }

    object LatestLocationNotFoundFailure : Failure() {
        override val title: String
            get() = "Missing Location"

        override val message: String
            get() = "Sorry, we couldn't find your location. Please try again."
    }

    object NetworkFailure : Failure() {
        override val title: String
            get() = "Network Request Failed"

        override val message: String
            get() = "You have lost network connectivity, please check your settings and try again."
    }

    object UnexpectedFailure : Failure() {
        override val title: String
            get() = "Sorry, something went wrong"

        override val message: String
            get() = "An unexpected error has ocurred, please try again."
    }
}