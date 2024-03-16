package br.com.jwar.triviachallenge.data.di

import br.com.jwar.triviachallenge.data.datasources.trivia.TRIVIA_API_BASE_URL
import br.com.jwar.triviachallenge.data.datasources.trivia.TriviaRemoteDataSource
import br.com.jwar.triviachallenge.data.datasources.trivia.TriviaRemoteDataSourceImpl
import br.com.jwar.triviachallenge.data.repositories.TriviaUnitRepository
import br.com.jwar.triviachallenge.data.repositories.TriviaActivityRepository
import br.com.jwar.triviachallenge.data.datasources.trivia.TriviaApi
import br.com.jwar.triviachallenge.data.mappers.TriviaCategoryResponseToUnitMapper
import br.com.jwar.triviachallenge.data.mappers.TriviaQuestionResponseToActivityMapper
import br.com.jwar.triviachallenge.data.services.translator.TranslatorService
import br.com.jwar.triviachallenge.data.services.translator.TranslatorServiceImpl
import br.com.jwar.triviachallenge.data.utils.HtmlStringAdapter
import br.com.jwar.triviachallenge.domain.repositories.UnitRepository
import br.com.jwar.triviachallenge.domain.repositories.ActivityRepository
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataModule {

    @Provides
    @Singleton
    fun providesConvertFactory() : Converter.Factory =
        MoshiConverterFactory.create(
            Moshi.Builder()
                .add(HtmlStringAdapter())
                .build()
        )

    @Provides
    @Singleton
    fun providesTriviaApi(
        convertFactory: Converter.Factory
    ): TriviaApi =
        Retrofit.Builder()
            .baseUrl(TRIVIA_API_BASE_URL)
            .addConverterFactory(convertFactory)
            .build()
            .create(TriviaApi::class.java)

    @Provides
    fun providesTriviaRemoteDataSource(
        triviaApi: TriviaApi,
    ): TriviaRemoteDataSource = TriviaRemoteDataSourceImpl(triviaApi)

    @Provides
    @Singleton
    fun providesActivityRepository(
        triviaRemoteDataSource: TriviaRemoteDataSource,
        triviaQuestionResponseToActivityMapper: TriviaQuestionResponseToActivityMapper,
    ): ActivityRepository = TriviaActivityRepository(triviaRemoteDataSource, triviaQuestionResponseToActivityMapper)

    @Provides
    fun providesUnitRepository(
        triviaRemoteDataSource: TriviaRemoteDataSource,
        triviaCategoryToUnitMapper: TriviaCategoryResponseToUnitMapper,
    ): UnitRepository = TriviaUnitRepository(triviaRemoteDataSource, triviaCategoryToUnitMapper)

    @Provides
    @Singleton
    fun providesTranslatorService(): TranslatorService = TranslatorServiceImpl()
}