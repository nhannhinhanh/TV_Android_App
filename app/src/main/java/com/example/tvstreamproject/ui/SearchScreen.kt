package com.example.tvstreamproject.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.tvstreamproject.data.model.TvSeries
import com.example.tvstreamproject.utils.ImageUtil
import com.example.tvstreamproject.viewmodel.SearchViewModel
import com.example.tvstreamproject.viewmodel.UiState

@Composable
fun SearchScreen(
    onSelectSeries: (Int) -> Unit,
    viewModel: SearchViewModel = viewModel()
) {
    var query by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize().padding(20.dp)) { // padding tăng lên
        OutlinedTextField(
            value = query,
            onValueChange = {
                query = it
                viewModel.search(it)
            },
            label = { Text("Search for a movie", color = Color.DarkGray) }, // đổi label text và màu
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color(0xFF0D47A1), // màu xanh đậm
                unfocusedTextColor = Color.Gray,
                cursorColor = Color(0xFF0D47A1),
                focusedBorderColor = Color(0xFF0D47A1),
                unfocusedBorderColor = Color.LightGray,
                focusedLabelColor = Color(0xFF0D47A1),
                unfocusedLabelColor = Color.Gray
            )
        )

        Spacer(modifier = Modifier.height(20.dp)) // tăng khoảng cách

        val uiState by viewModel.uiState.collectAsState()

        when (uiState) {
            is UiState.Loading -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = Color(0xFF0D47A1)) // đổi màu spinner
                }
            }

            is UiState.Error -> {
                Text(
                    "Oops! Search failed. Please try again.",
                    color = Color.Red,
                    fontSize = 16.sp
                )
            }

            is UiState.Success -> {
                val results = (uiState as UiState.Success<List<TvSeries>>).data
                LazyColumn(verticalArrangement = Arrangement.spacedBy(14.dp)) { // khoảng cách item lớn hơn
                    items(results) { tv ->
                        TvSearchItem(tvSeries = tv, onClick = { onSelectSeries(tv.id) })
                    }
                }
            }
        }
    }
}

@Composable
fun TvSearchItem(tvSeries: TvSeries, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(10.dp) // padding tăng
    ) {
        AsyncImage(
            model = ImageUtil.buildImageUrl(tvSeries.posterPath, "w250"), // tăng kích thước ảnh
            contentDescription = tvSeries.name,
            modifier = Modifier.size(110.dp)
        )

        Spacer(modifier = Modifier.width(14.dp))

        Column {
            Text(tvSeries.name, fontSize = 20.sp, color = Color(0xFF37474F)) // chữ lớn, màu xanh xám
            Text(tvSeries.firstAirDate.take(4), fontSize = 15.sp, color = Color.Gray)
            Text(
                tvSeries.overview.take(120) + "...",
                fontSize = 13.sp,
                color = Color(0xFF455A64)
            ) // màu tối hơn
        }
    }
}
