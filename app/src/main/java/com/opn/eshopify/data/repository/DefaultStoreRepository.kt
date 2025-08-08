package com.opn.eshopify.data.repository

import com.opn.eshopify.data.extension.safeApiCall
import com.opn.eshopify.data.remote.api.ShoppingAPI
import com.opn.eshopify.data.remote.mapper.toDomain
import com.opn.eshopify.domain.DataError
import com.opn.eshopify.domain.Result
import com.opn.eshopify.domain.model.Store
import com.opn.eshopify.domain.repository.StoreRepository

class DefaultStoreRepository(private val api: ShoppingAPI) : StoreRepository {

    override suspend fun getStoreDetail(): Result<Store, DataError> {
        return safeApiCall {
            api.getStoreDetail().toDomain()
        }
    }
}
