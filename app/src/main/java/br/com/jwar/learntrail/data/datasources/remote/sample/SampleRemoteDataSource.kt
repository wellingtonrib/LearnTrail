package br.com.jwar.learntrail.data.datasources.remote.sample

import android.content.Context
import br.com.jwar.learntrail.data.datasources.remote.RemoteDataSource
import br.com.jwar.learntrail.domain.model.Activity
import br.com.jwar.learntrail.domain.model.Question
import br.com.jwar.learntrail.domain.model.Unit
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapter
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import java.io.IOException
import java.io.InputStream
import javax.inject.Inject

@OptIn(ExperimentalStdlibApi::class)
class SampleRemoteDataSource @Inject constructor(
    private val context: Context,
) : RemoteDataSource {

    private val moshi: Moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    override suspend fun getUnits(): List<Unit> =
        loadJSONFromAsset("units.json")?.let { json ->
            moshi.adapter<List<Unit>>().fromJson(json)
        }.orEmpty()

    override suspend fun getQuestions(activityId: String) =
        loadJSONFromAsset( "questions.json")?.let { json ->
            moshi.adapter<List<Question>>().fromJson(json)
        }.orEmpty()

    override suspend fun getActivities(unitId: String) =
        loadJSONFromAsset( "activities.json")?.let { json ->
            moshi.adapter<List<Activity>>().fromJson(json)
        }.orEmpty()

    private fun loadJSONFromAsset(fileName: String) =
        try {
            val inputStream: InputStream = context.assets.open(fileName)
            inputStream.bufferedReader().use { it.readText() }
        } catch (ex: IOException) {
            ex.printStackTrace()
            null
        }
}

