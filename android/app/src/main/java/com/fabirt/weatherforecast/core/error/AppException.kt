package com.fabirt.weatherforecast.core.error

sealed class AppException : Exception() {
    /**
     * @return [Failure] representation of this [AppException].
     */
    abstract fun toFailure(): Failure

    class LocationPermissionNotGrantedException : AppException() {
        override fun toFailure(): Failure = Failure.LocationPermissionNotGrantedFailure
    }

    class LatestLocationNotFoundException : AppException() {
        override fun toFailure(): Failure = Failure.LatestLocationNotFoundFailure
    }

    class NetworkException : AppException() {
        override fun toFailure(): Failure = Failure.NetworkFailure
    }

    class UnexpectedException : AppException() {
        override fun toFailure(): Failure = Failure.UnexpectedFailure
    }
}