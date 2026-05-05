package ivan.karpiuk.mindful.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

interface DataRepository {
    val data: Flow<List<String>>
}

class DefaultDataRepository
    @Inject
    constructor() : DataRepository {
        override val data: Flow<List<String>> = flow { emit(listOf("Android")) }
    }
