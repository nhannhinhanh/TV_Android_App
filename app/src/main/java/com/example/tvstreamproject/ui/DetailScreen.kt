package com.example.tvstreamproject.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.tvstreamproject.data.model.TvDetail
import com.example.tvstreamproject.data.model.Season
import com.example.tvstreamproject.utils.ImageUtil
import com.example.tvstreamproject.viewmodel.TvDetailViewModel
import com.example.tvstreamproject.viewmodel.UiState
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun DetailScreen(
    seriesId: Int,
    onBack: () -> Unit,
    viewModel: TvDetailViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var selectedSeason by remember { mutableStateOf<Season?>(null) }
    var isDropdownExpanded by remember { mutableStateOf(false) }

    LaunchedEffect(seriesId) {
        viewModel.loadTvDetail(seriesId)
    }

    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color(0xFFE3F2FD))) { // Nền xanh dương nhạt
        when (val state = uiState) {
            is UiState.Loading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }

            is UiState.Error -> {
                Text(
                    "Không thể tải chi tiết TV",
                    color = Color.Red,
                    modifier = Modifier.align(Alignment.Center),
                    fontFamily = FontFamily.SansSerif
                )
            }

            is UiState.Success -> {
                val tv = state.data
                if (selectedSeason == null && tv.seasons.isNotEmpty()) {
                    selectedSeason = tv.seasons.last()
                }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    // Nút Quay lại
                    Text(
                        "← Quay lại",
                        color = Color.Blue,
                        modifier = Modifier
                            .clickable { onBack() }
                            .padding(bottom = 8.dp),
                        fontFamily = FontFamily.SansSerif
                    )

                    // Thông tin chính
                    Row(modifier = Modifier.fillMaxWidth()) {
                        // Poster
                        AsyncImage(
                            model = ImageUtil.buildImageUrl(tv.posterPath, "w300"),
                            contentDescription = tv.name,
                            modifier = Modifier
                                .width(120.dp)
                                .height(180.dp)
                                .clip(RoundedCornerShape(8.dp))
                        )

                        Spacer(modifier = Modifier.width(16.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                tv.name,
                                color = Color.Black,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.SansSerif
                            )
                            Text(
                                tv.firstAirDate.take(4),
                                color = Color.DarkGray,
                                fontFamily = FontFamily.SansSerif
                            )
                            Text(
                                tv.networks.firstOrNull()?.name ?: "Nhà phát hành không xác định",
                                color = Color.DarkGray,
                                fontFamily = FontFamily.SansSerif
                            )
                            Text(
                                "⭐ ${tv.voteAverage}",
                                color = Color(0xFFFFA000),
                                fontFamily = FontFamily.SansSerif
                            )
                            Text(
                                "Tạo bởi: ${tv.createdBy.joinToString { it.name }}",
                                color = Color.Black,
                                fontSize = 12.sp,
                                fontFamily = FontFamily.SansSerif
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        tv.overview,
                        color = Color.Black,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(vertical = 8.dp),
                        fontFamily = FontFamily.SansSerif
                    )

                    // Dropdown chọn mùa
                    Text("Chọn mùa:", color = Color.Black, fontFamily = FontFamily.SansSerif)
                    Box {
                        OutlinedTextField(
                            value = selectedSeason?.name ?: "",
                            onValueChange = {},
                            modifier = Modifier.fillMaxWidth(),
                            enabled = false,
                            readOnly = true,
                            trailingIcon = {
                                Icon(
                                    Icons.Default.ArrowDropDown,
                                    contentDescription = null,
                                    modifier = Modifier.clickable {
                                        isDropdownExpanded = true
                                    }
                                )
                            },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color.Blue,
                                unfocusedBorderColor = Color.Gray
                            )
                        )

                        DropdownMenu(
                            expanded = isDropdownExpanded,
                            onDismissRequest = { isDropdownExpanded = false }
                        ) {
                            tv.seasons.forEach { season ->
                                DropdownMenuItem(
                                    text = { Text(season.name, fontFamily = FontFamily.SansSerif) },
                                    onClick = {
                                        selectedSeason = season
                                        isDropdownExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    selectedSeason?.let { season ->
                        Spacer(modifier = Modifier.height(16.dp))

                        Row(modifier = Modifier.fillMaxWidth()) {
                            AsyncImage(
                                model = ImageUtil.buildImageUrl(season.posterPath, "w300"),
                                contentDescription = season.name,
                                modifier = Modifier
                                    .width(100.dp)
                                    .height(150.dp)
                                    .clip(RoundedCornerShape(8.dp))
                            )

                            Spacer(modifier = Modifier.width(12.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    season.name,
                                    color = Color.Black,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    fontFamily = FontFamily.SansSerif
                                )
                                Text(
                                    "Ngày phát sóng: ${season.airDate ?: "Không xác định"}",
                                    color = Color.DarkGray,
                                    fontFamily = FontFamily.SansSerif
                                )
                                Text(
                                    "Số tập: ${season.episodeCount}",
                                    color = Color.DarkGray,
                                    fontFamily = FontFamily.SansSerif
                                )
                                Text(
                                    "Đánh giá: ⭐ ${season.voteAverage}",
                                    color = Color(0xFFFFA000),
                                    fontFamily = FontFamily.SansSerif
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            season.overview,
                            color = Color.Black,
                            fontSize = 14.sp,
                            fontFamily = FontFamily.SansSerif
                        )
                    }
                }
            }
        }
    }
}

