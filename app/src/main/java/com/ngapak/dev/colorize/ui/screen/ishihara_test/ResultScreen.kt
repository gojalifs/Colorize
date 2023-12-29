package com.ngapak.dev.colorize.ui.screen.ishihara_test

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ngapak.dev.colorize.R

@Composable
fun ResultScreen(
    modifier: Modifier = Modifier,
    viewModel: IshiharaViewModel,
    onClick: () -> Unit
) {

    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(
            text = stringResource(R.string.your_grade_is),
            fontSize = 32.sp,
            modifier = modifier
                .align(Alignment.TopCenter)
                .padding(top = 24.dp),
            color = Color.Gray
        )
        Row(modifier = Modifier) {
            Text(
                text = "${viewModel.answer.count { it.value }}",
                fontSize = 120.sp,
                modifier = modifier.align(Alignment.Bottom)
            )
            Text(
                text = "/${viewModel.answer.count()}",
                fontSize = 24.sp,
                modifier = modifier.align(Alignment.Bottom),
                color = Color.Gray
            )
        }
        Column(
            modifier = modifier
                .align(Alignment.BottomCenter)
                .padding(horizontal = 24.dp)
        ) {
            if (viewModel.answer.count { it.value } != 11) {
                Text(
                    text = stringResource(R.string.grade_advice_not_good),
                    textAlign = TextAlign.Justify,
                    modifier = modifier.padding(bottom = 24.dp)
                )
            } else {
                Text(text = stringResource(R.string.grade_passed))
            }
            Button(
                onClick = {
                    onClick()
                },
                modifier = modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(bottom = 24.dp)
            ) {
                Text(text = stringResource(R.string.back_to_home))
            }
        }
    }
}