package com.fabirt.weatherforecast.core.error

abstract class Failure

class LocationPermissionNotGrantedFailure : Failure()

class LatestLocationNotFoundFailure : Failure()

class NetworkFailure : Failure()

class UnexpectedFailure : Failure()