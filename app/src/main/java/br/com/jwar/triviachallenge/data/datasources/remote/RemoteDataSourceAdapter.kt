package br.com.jwar.triviachallenge.data.datasources.remote

import br.com.jwar.triviachallenge.domain.model.Activity
import br.com.jwar.triviachallenge.domain.model.Unit

interface RemoteDataSourceAdapter {
    suspend fun adaptToUnit(data: Any): Unit
    suspend fun adaptToActivity(data: Any): Activity
}