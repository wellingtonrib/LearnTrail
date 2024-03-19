package br.com.jwar.triviachallenge.data.datasources.opentdb

import br.com.jwar.triviachallenge.data.datasources.RemoteDataSourceAdapter
import br.com.jwar.triviachallenge.data.datasources.opentdb.dto.TriviaCategoryResponse
import br.com.jwar.triviachallenge.domain.model.Activity

class TriviaRemoteDataSourceAdapter : RemoteDataSourceAdapter {
    override fun adaptToUnit(data: Any) {
        (data as List<TriviaCategoryResponse>).map{ }
    }

    override fun adaptToActivity(data: Any): Activity {
        TODO("Not yet implemented")
    }
}