package com.example.contactapp.ui.contacts.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.contactapp.data.database.ContactEntity


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SearchList(
    searchResults: List<ContactEntity>,
    onContactSelected: (ContactEntity) -> Unit,
    modifier: Modifier = Modifier,
    scrollState: LazyListState = rememberLazyListState()
) {

    LazyColumn (
        modifier = modifier
            .background(color= Color.White)
            .fillMaxWidth()
            .padding(10.dp)
            .fillMaxHeight()
        ,
        state = scrollState,
    ) {
        stickyHeader {
            Text(
                text = "Search Results",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 10.dp),
                color = Color.Black
            )
        }
        items(searchResults.size) { index ->
            val contact = searchResults[index]
            SearchListItem(
                contact = contact,
                onClick = { onContactSelected(contact) },
            )
        }
        item{
            Spacer(modifier = Modifier.height(50.dp))
        }
    }
}
