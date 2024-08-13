package com.example.petapp.feature.detail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.petapp.R
import com.example.petapp.domain.model.Animal
import com.example.petapp.ui.components.ErrorScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    detailViewModel: DetailViewModel,
    onNavigateBack: () -> Unit
) {
    val state by detailViewModel.detailUiState.collectAsState()
    var title by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = title) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                            contentDescription = "Navigate back"
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )

        }
    ) { contentPadding ->
        when (state) {
            is DetailUiState.Error -> {
                ErrorScreen(
                    modifier = Modifier
                        .padding(contentPadding)
                        .fillMaxSize(),
                    onRetry = { detailViewModel.getAnimalById() },
                    error = stringResource(id = R.string.error_text)
                )
            }

            is DetailUiState.Loaded -> {
                title = (state as DetailUiState.Loaded).animal.name


                DetailContent(
                    modifier = Modifier.padding(contentPadding),
                    animal = (state as DetailUiState.Loaded).animal
                )
            }

            DetailUiState.Loading -> {
                Box(
                    modifier = Modifier
                        .padding(contentPadding)
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(Modifier.size(64.dp))
                }
            }
        }
    }
}

@Composable
fun DetailContent(
    modifier: Modifier = Modifier,
    animal: Animal
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        AsyncImage(
            model = animal.photo.firstOrNull()?.large,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .clip(MaterialTheme.shapes.medium),
            placeholder = painterResource(id = R.drawable.pet_placeholder),
            error = painterResource(id = R.drawable.pet_placeholder),
        )

        PropertyComponent(title = stringResource(id = R.string.breed), text = animal.breeds.primary)

        val mixedText = if (animal.breeds.mixed) {
            stringResource(id = R.string.mixed_breed)
        } else {
            stringResource(id = R.string.pure_breed)
        }
        Text(
            text = mixedText,
            style = MaterialTheme.typography.bodyMedium
        )

        PropertyComponent(title = stringResource(id = R.string.size), text = animal.size)
        PropertyComponent(title = stringResource(id = R.string.gender), text = animal.gender)
        PropertyComponent(title = stringResource(id = R.string.status), text = animal.status)

        val distanceText = if (animal.distance == null) {
            stringResource(id = R.string.no_distance)
        } else {
            animal.distance.toString()
        }
        PropertyComponent(title = stringResource(id = R.string.distance), text = distanceText)


    }

}

@Composable
fun PropertyComponent(title: String, text: String) {
    Column {
        Spacer(Modifier.size(16.dp))
        Header(title = title)
        Spacer(modifier = Modifier.size(4.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(modifier = Modifier.size(4.dp))
    }
}

@Composable
fun Header(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleLarge
    )
}