package com.opn.eshopify.data.repository

import com.opn.eshopify.data.extension.safeApiCall
import com.opn.eshopify.data.remote.api.ShoppingAPI
import com.opn.eshopify.data.remote.mapper.asDomain
import com.opn.eshopify.domain.DataError
import com.opn.eshopify.domain.Result
import com.opn.eshopify.domain.model.Store
import com.opn.eshopify.domain.repository.StoreRepository
import com.opn.eshopify.domain.util.AppDispatchers

class DefaultStoreRepository(
    private val api: ShoppingAPI,
    private val appDispatchers: AppDispatchers,
) : StoreRepository {

    override suspend fun getStoreDetail(): Result<Store, DataError> {
        return safeApiCall(appDispatchers.getIODispatcher()) {
            api.getStoreDetail().asDomain()
        }
    }
}
