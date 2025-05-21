package com.example.tvstreamproject.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.*
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.tvstreamproject.data.model.TvSeries
import com.example.tvstreamproject.utils.ImageUtil
import com.example.tvstreamproject.viewmodel.HomeViewModel
import com.example.tvstreamproject.viewmodel.UiState

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = viewModel(),
    onSelectSeries: (Int) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val selectedId = remember { mutableStateOf<Int?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE3F2FD)) // Nền xanh dương nhạt
            .padding(16.dp)
    ) {
        Text(
            text = "Danh sách Series",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            fontFamily = FontFamily.SansSerif
        )

        Spacer(modifier = Modifier.height(16.dp))

        when (uiState) {
            is UiState.Loading -> Box(
                modifier = Modifier.fillMaxWidth().height(200.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color.Blue)
            }

            is UiState.Error -> Box(
                modifier = Modifier.fillMaxWidth().height(200.dp),
                contentAlignment = Alignment.Center
            ) {
                Text("Không thể tải dữ liệu.", color = Color.Red, fontFamily = FontFamily.SansSerif)
            }

            is UiState.Success -> {
                val tvList = (uiState as UiState.Success<List<TvSeries>>).data

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(tvList) { tv ->
                        TvPosterStyled(
                            tvSeries = tv,
                            isSelected = selectedId.value == tv.id,
                            onFocus = { selectedId.value = tv.id },
                            onClick = { onSelectSeries(tv.id) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TvPosterStyled(
    tvSeries: TvSeries,
    isSelected: Boolean,
    onFocus: () -> Unit,
    onClick: () -> Unit
) {
    val scale by animateFloatAsState(if (isSelected) 1.05f else 1.0f)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .graphicsLayer(scaleX = scale, scaleY = scale)
            .clickable {
                onClick()
                onFocus()
            }
            .padding(8.dp)
    ) {
        AsyncImage(
            model = ImageUtil.buildImageUrl(tvSeries.posterPath, "w500"),
            contentDescription = tvSeries.name,
            modifier = Modifier
                .size(100.dp)
                .clip(RoundedCornerShape(8.dp))
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column {
            Text(
                tvSeries.name,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                fontFamily = FontFamily.SansSerif
            )
            Text(
                tvSeries.firstAirDate.take(4),
                fontSize = 14.sp,
                color = Color.DarkGray,
                fontFamily = FontFamily.SansSerif
            )
        }
    }
}
