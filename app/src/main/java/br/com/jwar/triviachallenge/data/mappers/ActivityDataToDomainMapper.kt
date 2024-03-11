package br.com.jwar.triviachallenge.data.mappers

import br.com.jwar.triviachallenge.domain.model.Activity
import br.com.jwar.triviachallenge.data.services.responses.ActivityResponse

interface ActivityDataToDomainMapper {
    suspend fun mapFrom(activityResponse: ActivityResponse): Activity
}