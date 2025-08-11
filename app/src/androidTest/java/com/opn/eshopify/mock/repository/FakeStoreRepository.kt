package com.opn.eshopify.mock.repository

import com.opn.eshopify.domain.DataError
import com.opn.eshopify.domain.Result
import com.opn.eshopify.domain.model.Store
import com.opn.eshopify.domain.repository.StoreRepository
import kotlinx.datetime.LocalTime

class FakeStoreRepository : StoreRepository {

    override suspend fun getStoreDetail(): Result<Store, DataError> {
        return Result.Success(
            Store(
                name = "Test Store",
                rating = 4.8f,
                openingTime = LocalTime(9, 0),
                closingTime = LocalTime(21, 0)
            )
        )
    }
}
