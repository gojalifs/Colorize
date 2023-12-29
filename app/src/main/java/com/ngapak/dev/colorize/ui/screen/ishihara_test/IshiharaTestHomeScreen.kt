package com.ngapak.dev.colorize.ui.screen.ishihara_test

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.ngapak.dev.colorize.R
import com.ngapak.dev.colorize.ui.factory.IshiharaViewModelFactory

@Composable
fun IshiharaTestHomeScreen(
    startTest: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: IshiharaViewModel = viewModel(
        factory = IshiharaViewModelFactory.getInstance(
            LocalContext.current
        )
    )
) {
    val context = LocalContext.current
    val isImageDownloaded = viewModel.isImageDownloaded.collectAsState(null)
    LaunchedEffect(Unit) {
        viewModel.getAllData()
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp, 24.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(text = stringResource(R.string.ishihara_test), modifier.align(Alignment.CenterHorizontally), fontSize = 30.sp)
        Text(
            text = stringResource(R.string.ishihara_test_head),
            textAlign = TextAlign.Justify,
            color = MaterialTheme.colorScheme.tertiary
        )
        SubcomposeAsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data("https://drive.google.com/uc?id=1qucqXqJMleuCs_LrnigIrgb1HqZB4_fo&export=download")
                .crossfade(true).build(),
            contentDescription = stringResource(R.string.ishihara_test),
            contentScale = ContentScale.Fit,
            loading = {
                CircularProgressIndicator()
            },
            modifier = modifier
                .align(Alignment.CenterHorizontally)
                .padding(vertical = 24.dp)
                .width(200.dp)
                .height(200.dp)
        )
        Text(
            text = stringResource(R.string.ishihara_sub_head),
            textAlign = TextAlign.Justify,
            color = MaterialTheme.colorScheme.tertiary
        )
        Text(
            text = stringResource(R.string.ishihara_advice),
            textAlign = TextAlign.Justify,
            color = MaterialTheme.colorScheme.tertiary
        )

        isImageDownloaded.value.let {
            when (it) {
                true -> {
                    Button(
                        onClick = { startTest() },
                        modifier = modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(vertical = 16.dp)
                    ) {
                        Text(text = stringResource(R.string.start))
                    }
                }

                false -> {
                    Button(
                        onClick = {
                            viewModel.downloadData(context)
                        },
                        modifier = modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(vertical = 16.dp)
                    ) {
                        Text(text = stringResource(R.string.download_data))
                    }
                }

                else -> {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.tertiary, modifier = modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(vertical = 16.dp)
                    )
                }
            }
        }
    }
}