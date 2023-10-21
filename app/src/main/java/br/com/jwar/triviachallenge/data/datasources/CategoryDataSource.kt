package br.com.jwar.triviachallenge.data.datasources

interface CategoryDataSource {
    fun getCategories(): Map<String, String>
}