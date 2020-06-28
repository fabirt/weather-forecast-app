package com.fabirt.weatherforecast.data.repository

import com.fabirt.weatherforecast.core.error.AppException
import com.fabirt.weatherforecast.core.error.Failure
import com.fabirt.weatherforecast.core.other.Either
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.UnknownHostException

abstract class BaseRepository {
    /**
     * Catch and discriminate all application exceptions.
     * @return [Either] a [Failure], if an exception is thrown, or [T],
     * if the process is completed successfully.
     */
    protected suspend fun <T> transformExceptionsAsync(
        block: suspend () -> Either<Failure, T>
    ): Either<Failure, T> {
        return try {
            withContext(Dispatchers.IO) {
                block()
            }
        } catch (e: Exception) {
            when (e) {
                is AppException -> Either.Left(e.toFailure())
                is UnknownHostException -> Either.Left(Failure.NetworkFailure)
                else -> Either.Left(Failure.UnexpectedFailure)
            }
        }
    }
}