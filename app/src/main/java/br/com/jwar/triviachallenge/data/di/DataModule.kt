package br.com.jwar.triviachallenge.data.di

import br.com.jwar.triviachallenge.data.datasources.TRIVIA_API_BASE_URL
import br.com.jwar.triviachallenge.data.datasources.TriviaApi
import br.com.jwar.triviachallenge.data.datasources.TriviaRemoteDataSource
import br.com.jwar.triviachallenge.data.mappers.TriviaCategoryResponseToUnitMapper
import br.com.jwar.triviachallenge.data.mappers.TriviaQuestionResponseToActivityMapper
import br.com.jwar.triviachallenge.data.repositories.TriviaActivityRepository
import br.com.jwar.triviachallenge.data.repositories.TriviaUnitRepository
import br.com.jwar.triviachallenge.data.services.translator.MLKitTranslatorService
import br.com.jwar.triviachallenge.data.services.translator.TranslatorService
import br.com.jwar.triviachallenge.data.utils.HtmlStringAdapter
import br.com.jwar.triviachallenge.domain.repositories.ActivityRepository
import br.com.jwar.triviachallenge.domain.repositories.UnitRepository
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

@Module
@InstallIn(SingletonComponent::class)
class DataModule {

    @Provides
    fun provideCoroutineDispatcher(): CoroutineDispatcher = Dispatchers.IO

    @Provides
    @Singleton
    fun provideConvertFactory() : Converter.Factory =
        MoshiConverterFactory.create(
            Moshi.Builder()
                .add(HtmlStringAdapter())
                .build()
        )

    @Provides
    @Singleton
    fun provideTriviaApi(
        convertFactory: Converter.Factory
    ): TriviaApi =
        Retrofit.Builder()
            .baseUrl(TRIVIA_API_BASE_URL)
            .addConverterFactory(convertFactory)
            .build()
            .create(TriviaApi::class.java)

    @Provides
    @Singleton
    fun provideActivityRepository(
        triviaRemoteDataSource: TriviaRemoteDataSource,
        triviaQuestionResponseToActivityMapper: TriviaQuestionResponseToActivityMapper,
    ): ActivityRepository = TriviaActivityRepository(triviaRemoteDataSource, triviaQuestionResponseToActivityMapper)

    @Provides
    fun provideUnitRepository(
        triviaRemoteDataSource: TriviaRemoteDataSource,
        triviaCategoryToUnitMapper: TriviaCategoryResponseToUnitMapper,
    ): UnitRepository = TriviaUnitRepository(triviaRemoteDataSource, triviaCategoryToUnitMapper)

    @Provides
    @Singleton
    fun provideTranslatorService(): TranslatorService = MLKitTranslatorService()
}