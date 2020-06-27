package com.fabirt.weatherforecast.core.error

sealed class Failure {
    abstract val message: String

    object LocationPermissionNotGrantedFailure : Failure() {
        override val message: String
            get() = "Please accept location permissions"
    }

    object LatestLocationNotFoundFailure : Failure() {
        override val message: String
            get() = "Sorry, we couldn't find your location"
    }

    object NetworkFailure : Failure() {
        override val message: String
            get() = "Network request error"
    }

    object UnexpectedFailure : Failure() {
        override val message: String
            get() = "Something went wrong"
    }
}