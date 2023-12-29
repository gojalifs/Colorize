package com.ngapak.dev.colorize.ui.screen.ishihara_test

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.ngapak.dev.colorize.R
import com.ngapak.dev.colorize.data.local.entities.IshiharaEntities
import kotlinx.coroutines.launch

@Composable
fun IshiharaTestScreen(
    navigateResult: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: IshiharaViewModel,
) {
    Column(modifier = Modifier.padding(16.dp)) {
        IshiharaTestPager(navigateResult, modifier, viewModel)
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun IshiharaTestPager(
    navigateResult: () -> Unit,
    modifier: Modifier,
    viewModel: IshiharaViewModel
) {
    val scrollScope = rememberCoroutineScope()
    val data = viewModel.ishiharaTests.collectAsLazyPagingItems()
    val state = rememberPagerState { data.itemCount }
    HorizontalPager(state = state, userScrollEnabled = false) { page ->
        val item = data[page]

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            SubcomposeAsyncImage(
                model = ImageRequest.Builder(LocalContext.current).data(item?.imgPath)
                    .crossfade(true).build(),
                contentDescription = stringResource(R.string.ishihara_test),
                contentScale = ContentScale.Fit,
                loading = {
                    CircularProgressIndicator()
                },
            )
            IshiharaAnswerButton(
                page = page,
                modifier = modifier.align(Alignment.BottomCenter),
                item,
                onClick = {
                    if (page == 10) {
                        navigateResult()
                    } else {
                        scrollScope.launch {
                            state.animateScrollToPage(page + 1)
                        }
                    }
                },
                viewModel = viewModel
            )
        }
    }
}

@Composable
fun IshiharaAnswerButton(
    page: Int,
    modifier: Modifier = Modifier,
    ishiharaEntities: IshiharaEntities?,
    onClick: () -> Unit,
    viewModel: IshiharaViewModel
) {
    Column(modifier) {
        Row(modifier = Modifier.align(Alignment.CenterHorizontally)) {
            if (!ishiharaEntities?.protan.isNullOrEmpty()) {
                Button(
                    onClick = {
                        viewModel.addAnswer(page + 1, false)
                        onClick()
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text(text = "${ishiharaEntities?.protan}")
                }
                Spacer(modifier = modifier.width(8.dp))
            }
            if (!ishiharaEntities?.deutan.isNullOrEmpty()) {
                Button(
                    onClick = {
                        viewModel.addAnswer(page + 1, false)
                        onClick()
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text(text = "${ishiharaEntities?.deutan}")
                }
                Spacer(modifier = modifier.width(8.dp))
            }
            Button(
                onClick = {
                    viewModel.addAnswer(page + 1, true)
                    onClick()
                },
                modifier = Modifier.weight(1f)
            ) {
                Text(text = "${ishiharaEntities?.answerTrue}")
            }
            if (ishiharaEntities?.answerFalse != null && ishiharaEntities.answerFalse.toIntOrNull() != 0) {
                Spacer(modifier = modifier.width(8.dp))
                Button(
                    onClick = {
                        viewModel.addAnswer(page + 1, false)
                        onClick()
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text(text = ishiharaEntities.answerFalse)
                }
            }
        }
        Row(modifier = modifier) {
            Button(
                onClick = {
                    viewModel.addAnswer(page + 1, false)
                    onClick()
                },
                modifier = Modifier.weight(1f)
            ) {
                Text(text = "${ishiharaEntities?.otherNumber}")
            }
            Spacer(modifier = modifier.width(8.dp))
            Button(
                onClick = {
                    viewModel.addAnswer(page + 1, false)
                    onClick()
                },
                modifier = Modifier.weight(1f)
            ) {
                Text(text = "${ishiharaEntities?.unreadable}")
            }
        }
    }
}