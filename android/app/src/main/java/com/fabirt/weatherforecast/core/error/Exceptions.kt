package com.fabirt.weatherforecast.core.error

abstract class AppException : Exception() {
    abstract fun toFailure(): Failure
}

class LocationPermissionNotGrantedException : AppException() {
    override fun toFailure(): Failure = LocationPermissionNotGrantedFailure()
}

class LatestLocationNotFoundException : AppException() {
    override fun toFailure(): Failure = LatestLocationNotFoundFailure()
}

class NetworkException : AppException() {
    override fun toFailure(): Failure = NetworkFailure()
}

class UnexpectedException : AppException() {
    override fun toFailure(): Failure = UnexpectedFailure()
}
