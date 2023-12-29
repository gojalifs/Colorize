package com.ngapak.dev.colorize.data.paging_source

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.ngapak.dev.colorize.data.local.dao.IshiharaDao
import com.ngapak.dev.colorize.data.local.entities.IshiharaEntities

class IshiharaPagingSource(private val dao: IshiharaDao) : PagingSource<Int, IshiharaEntities>() {
    override fun getRefreshKey(state: PagingState<Int, IshiharaEntities>): Int? {
        return state.anchorPosition?.let {
            val anchorPage = state.closestPageToPosition(it)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, IshiharaEntities> {
        return try {
            val position = params.key ?: STARTING_KEY
            val data = dao.getIshiharaById(position)

            LoadResult.Page(
                data = data,
                prevKey = if (position == STARTING_KEY) null else position - 1,
                nextKey = if (data.isEmpty()) null else position + 1
            )
        } catch (exception: Exception) {
            return LoadResult.Error(exception)
        }
    }

    companion object {
        private const val STARTING_KEY = 1
    }
}