package com.ngapak.dev.colorize.ui.screen.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ngapak.dev.colorize.R

@Composable
fun HomeScreen(
    onclick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            MyAppBar()
        }
    ) { padding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp, vertical = 16.dp)
        ) {
            Row(
                modifier = modifier
                    .align(Alignment.CenterHorizontally)
            ) {
                TestCard(
                    title = stringResource(R.string.ishihara_test),
                    imageDrawable = R.drawable.ishihara12cpy,
                    onclick = onclick,
                    modifier = modifier.weight(1f)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyAppBar() {
    TopAppBar(title = { Text(text = stringResource(R.string.colorize)) })
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TestCard(
    title: String,
    imageDrawable: Int,
    onclick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        onClick = onclick,
        modifier = modifier
            .height(200.dp)
            .width(200.dp)
            .padding(16.dp)
    ) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(8.dp)
        ) {
            Text(
                text = title,
                fontSize = 20.sp,
                modifier = Modifier.align(Alignment.BottomCenter)
            )
            Image(
                painter = painterResource(imageDrawable),
                contentDescription = stringResource(R.string.ishihara_test),
                alignment = Alignment.Center,
                modifier = modifier
                    .padding(bottom = 24.dp)
                    .fillMaxSize()
            )
        }
    }
}
