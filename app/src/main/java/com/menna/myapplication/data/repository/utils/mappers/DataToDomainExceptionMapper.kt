package com.popcorntime.repository.utils.mappers

import com.popcorntime.domain.exception.UnauthorizedActionException
import com.popcorntime.domain.exception.NoDataException
import com.popcorntime.domain.exception.ParsingException
import com.popcorntime.domain.exception.InternetConnectionException
import com.popcorntime.domain.exception.MovioException
import com.popcorntime.domain.exception.NetworkException
import com.popcorntime.domain.exception.UnknownException
import com.popcorntime.repository.utils.exception.ApiException
import com.popcorntime.repository.utils.exception.BadRequestException
import com.popcorntime.repository.utils.exception.ConflictException
import com.popcorntime.repository.utils.exception.DataSourceException
import com.popcorntime.repository.utils.exception.ForbiddenException
import com.popcorntime.repository.utils.exception.NoInternetException
import com.popcorntime.repository.utils.exception.NotFoundException
import com.popcorntime.repository.utils.exception.RepoEmptyResponseException
import com.popcorntime.repository.utils.exception.RepoJsonParsingException
import com.popcorntime.repository.utils.exception.RequestTimeoutException
import com.popcorntime.repository.utils.exception.ServerException
import com.popcorntime.repository.utils.exception.TooManyRequestsException
import com.popcorntime.repository.utils.exception.UnauthorizedException
import com.popcorntime.repository.utils.exception.UnknownDataSourceException

suspend fun <T> tryToCall(execute: suspend () -> T): T {
    return try {
        execute()
    } catch (exception: DataSourceException) {
        throw getDomainExceptionFromDataException(exception)
    } catch (exception: Exception) {
        throw UnknownException(exception.message ?: "")
    }
}


fun getDomainExceptionFromDataException(exception: DataSourceException): MovioException {
    return when (exception) {
        is ApiException -> when (exception) {
            is RequestTimeoutException -> NetworkException(exception.message)
            is TooManyRequestsException -> NetworkException(exception.message)
            is ForbiddenException -> NetworkException(exception.message)
            is ConflictException -> NetworkException(exception.message)
            is BadRequestException -> NetworkException(exception.message)
            is NotFoundException -> UnknownException("Not Found")
            is UnauthorizedException -> UnauthorizedActionException(exception.message)
        }
        is ServerException -> NetworkException(exception.message)
        is NoInternetException -> InternetConnectionException(exception.message)
        is UnknownDataSourceException -> UnknownException(exception.message)
        is RepoEmptyResponseException -> NoDataException(exception.message)
        is RepoJsonParsingException -> ParsingException(exception.message)
    }
}