package br.com.jwar.triviachallenge.configuration.di

import br.com.jwar.triviachallenge.data.datasources.CategoryDataSource
import br.com.jwar.triviachallenge.data.datasources.CategoryDataSourceImpl
import br.com.jwar.triviachallenge.data.services.ChallengeService
import br.com.jwar.triviachallenge.data.datasources.ChallengeDataSource
import br.com.jwar.triviachallenge.data.datasources.ChallengeDataSourceImpl
import br.com.jwar.triviachallenge.data.mappers.CategoryResponseToCategoryMapper
import br.com.jwar.triviachallenge.data.mappers.CategoryResponseToCategoryMapperImpl
import br.com.jwar.triviachallenge.data.mappers.ChallengeResponseToChallengeMapper
import br.com.jwar.triviachallenge.data.mappers.ChallengeResponseToChallengeMapperImpl
import br.com.jwar.triviachallenge.domain.repositories.CategoryRepository
import br.com.jwar.triviachallenge.data.repositories.CategoryRepositoryImpl
import br.com.jwar.triviachallenge.domain.repositories.ChallengeRepository
import br.com.jwar.triviachallenge.data.repositories.ChallengeRepositoryImpl
import br.com.jwar.triviachallenge.data.services.translator.TranslatorService
import br.com.jwar.triviachallenge.data.services.translator.TranslatorServiceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

@Module
@InstallIn(SingletonComponent::class)
class DataModule {

    @Provides
    fun providesChallengeService(): ChallengeService =
        Retrofit.Builder()
            .baseUrl("https://opentdb.com/")
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(ChallengeService::class.java)

    @Provides
    fun providesChallengeDataSource(
        challengeService: ChallengeService,
    ): ChallengeDataSource = ChallengeDataSourceImpl(challengeService)

    @Provides
    fun providesChallengeResponseToChallengeMapper(
        translatorService: TranslatorService,
    ): ChallengeResponseToChallengeMapper = ChallengeResponseToChallengeMapperImpl(translatorService)

    @Provides
    fun providesChallengeRepository(
        challengeDataSource: ChallengeDataSource,
        challengeResponseToChallengeMapper: ChallengeResponseToChallengeMapper,
    ): ChallengeRepository =
        ChallengeRepositoryImpl(challengeDataSource, challengeResponseToChallengeMapper)

    @Provides
    fun providesCategoryDataSource(): CategoryDataSource = CategoryDataSourceImpl()

    @Provides
    fun providesCategoryResponseToCategoryMapper(
        translatorService: TranslatorService
    ): CategoryResponseToCategoryMapper = CategoryResponseToCategoryMapperImpl(translatorService)

    @Provides
    fun providesCategoryRepository(
        categoryDataSource: CategoryDataSource,
        categoryResponseToCategoryMapper: CategoryResponseToCategoryMapper,
    ): CategoryRepository = CategoryRepositoryImpl(categoryDataSource, categoryResponseToCategoryMapper)

    @Provides
    fun providesTranslatorService(): TranslatorService = TranslatorServiceImpl()
}