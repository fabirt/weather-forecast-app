package com.fabirt.weatherforecast.core.error

abstract class Failure {
    abstract val message: String
}

class LocationPermissionNotGrantedFailure : Failure() {
    override val message: String
        get() = "Please accept location permissions"
}

class LatestLocationNotFoundFailure : Failure() {
    override val message: String
        get() = "Sorry, we couldn't find your location"
}

class NetworkFailure : Failure() {
    override val message: String
        get() = "Network request error"
}

class UnexpectedFailure : Failure() {
    override val message: String
        get() = "Something went wrong"
}