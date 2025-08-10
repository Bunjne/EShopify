package com.opn.eshopify.domain.usecase.store

import com.opn.eshopify.domain.DataError
import com.opn.eshopify.domain.Result
import com.opn.eshopify.domain.model.Store
import com.opn.eshopify.domain.repository.StoreRepository

class GetStoreDetailUseCase(private val repository: StoreRepository) {

    suspend operator fun invoke(): Result<Store, DataError> = repository.getStoreDetail()
}
