package br.com.jwar.triviachallenge.data.datasources

import br.com.jwar.triviachallenge.domain.model.Activity

interface RemoteDataSourceAdapter {
    fun adaptToUnit(data: Any): Unit
    fun adaptToActivity(data: Any): Activity
}