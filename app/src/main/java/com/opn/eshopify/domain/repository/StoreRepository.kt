package com.opn.eshopify.domain.repository

import com.opn.eshopify.domain.DataError
import com.opn.eshopify.domain.Result
import com.opn.eshopify.domain.model.Store

interface StoreRepository {

    suspend fun getStoreDetail(): Result<Store, DataError>
}
