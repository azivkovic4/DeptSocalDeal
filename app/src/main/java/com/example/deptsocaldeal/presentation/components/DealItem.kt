package com.example.deptsocaldeal.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.deptsocaldeal.R
import com.example.deptsocaldeal.data.models.DealPresentation

@Composable
fun DealItem(
    modifier: Modifier = Modifier,
    deal: DealPresentation,
    onItemClicked: () -> Unit = {},
    onFavoriteClicked: (Boolean) -> Unit = {},
) {
    var isFavorite by remember {
        mutableStateOf(deal.isFavorite)
    }
    Column(
        modifier = modifier.padding(16.dp)
    ) {
        Box {
            AsyncImage(
                modifier = Modifier
                    .clickable(onClick = onItemClicked)
                    .padding(bottom = 5.dp)
                    .clip(RoundedCornerShape(5)),
                model = deal.image,
                contentDescription = "Sample image",
                placeholder = painterResource(R.drawable.ic_social_deals),
                error = painterResource(R.drawable.ic_social_deals),
                contentScale = ContentScale.Crop
            )
            Icon(
                modifier = Modifier
                    .padding(20.dp)
                    .align(Alignment.BottomEnd)
                    .clickable {
                        isFavorite = !isFavorite
                        onFavoriteClicked(isFavorite)
                    },
                painter = painterResource(id = if (!isFavorite) R.drawable.ic_favorite_not_checked else R.drawable.ic_favorite_checked),
                contentDescription = "Favorite Icon",
                tint = Color.White
            )
        }
        Text(
            modifier = Modifier.padding(bottom = 5.dp),
            text = deal.title,
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            overflow = TextOverflow.Ellipsis,
            maxLines = 1
        )
        Text(
            text = deal.company,
            style = MaterialTheme.typography.bodyMedium.copy(color = Color.Gray)
        )
        Text(text = deal.city, style = MaterialTheme.typography.bodyMedium.copy(color = Color.Gray))
        Row(
            modifier = Modifier.padding(top = 5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = deal.soldLabel,
                style = MaterialTheme.typography.bodyMedium.copy(color = Color.Blue)
            )
            Spacer(Modifier.weight(1f))
            deal.discountPrice?.let {
                Currency(
                    amount = it,
                    symbol = deal.symbol,
                    isDiscount = true
                )
            }
            Spacer(Modifier.width(5.dp))
            Currency(
                amount = deal.currentPrice,
                symbol = deal.symbol
            )
        }

        deal.description?.let {
            Text(
                modifier = Modifier.padding(top = 5.dp),
                text = it,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
private fun Currency(
    modifier: Modifier = Modifier,
    isDiscount: Boolean = false,
    amount: String,
    symbol: String
) {
    val color = if (isDiscount) Color.DarkGray else Color.Green
    val textDecoration = if (isDiscount) TextDecoration.LineThrough else TextDecoration.None
    val textStyle =
        if (isDiscount) MaterialTheme.typography.bodySmall else MaterialTheme.typography.titleMedium
    Text(
        modifier = modifier,
        text = "$symbol$amount",
        style = textStyle.copy(
            color = color,
            textDecoration = textDecoration
        )
    )
}

@Preview
@Composable
private fun DealItemPreview() =
    DealItem(
        deal = DealPresentation(
            id = "1",
            title = "Sample Deal",
            company = "Sample Company",
            city = "Sample City",
            image = "https://via.placeholder.com/150",
            currentPrice = "100",
            discountPrice = "80",
            symbol = "$",
            soldLabel = "10 Sold",
            description = "This is a sample description for the deal.",
            isFavorite = false
        )
    )