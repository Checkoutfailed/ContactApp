package com.example.contactapp.ui.contacts.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.contactapp.data.database.ContactEntity
import com.example.contactapp.ui.theme.HyperBlue
import com.example.contactapp.ui.theme.SystemDarkGrey
import com.example.contactapp.ui.theme.SystemGrey
import kotlinx.coroutines.launch

@Composable
private fun CategoryHeader (
    name: String,
) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Color.White)
            .drawBehind {

                val strokeWidth = 0.5.sp.toPx() * density
                val y = size.height - strokeWidth / 2

                drawLine(
                    SystemGrey,
                    Offset(0f, y),
                    Offset(size.width, y),
                    strokeWidth
                )

            },
        contentAlignment = Alignment.CenterStart
    ) {
        Text(
            text = name,
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = 25.dp,
                    bottom = 5.dp,
                    start = 10.dp,
                    end = 10.dp
                ),
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            color = SystemDarkGrey
        )
    }
}

private data class Category(
    val name: String,
    val contacts: List<ContactEntity>
)


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ContactList(
    contacts: List<ContactEntity>,
    onContactSelected: (ContactEntity) -> Unit,
    modifier: Modifier = Modifier,
    scrollState: LazyListState = rememberLazyListState()
) {

    val names = contacts.groupBy { it.name[0].uppercase() }.toSortedMap()
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val columnWidth = screenWidth * 0.9f
    val scope = rememberCoroutineScope()

    val categories = names.map{
        Category(
            name = it.key.toString(),
            contacts = it.value
        )
    }

    val alphabet = ('A'..'Z').toList()

   Row() {
       LazyColumn (
           modifier = modifier
               .background(color=Color.White)
               .widthIn(max = columnWidth)
           ,
           state = scrollState,
           horizontalAlignment = Alignment.CenterHorizontally,
       ) {
           categories.forEach { category ->
               stickyHeader {
                   CategoryHeader(category.name.uppercase())
               }
               items(
                   count = category.contacts.size,
               ) { index ->
                   val contact = category.contacts[index]
                   ContactListItem(
                       contact = contact,
                       onClick = { onContactSelected(contact) },
                       modifier = Modifier
                           .widthIn(max = 700.dp)
                           .fillMaxWidth()
                   )
               }
           }

           item{
               Spacer(modifier = Modifier.height(50.dp))
               Text(
                   text = "${contacts.size} Contacts",
                   fontWeight = FontWeight.Bold
               )
               Spacer(modifier = Modifier.height(50.dp))
           }
       }

       Surface(
           modifier = Modifier
               .fillMaxWidth()
               .fillMaxHeight()
               .align(Alignment.CenterVertically)
           ,
           color = Color.White
       ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                alphabet.forEach{ letter ->
                    Text(
                        text = letter.toString(),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = HyperBlue,
                        modifier = Modifier
                            .clickable(onClick = {
                                val index = getContactIndexByCategory(categories, letter.toString())
                                if(index !== null) {
                                    scope.launch {
                                        scrollState.animateScrollToItem(index)
                                    }
                                }
                            })
                            .height(18.dp)
                    )
                }
            }
       }
   }

}


private fun getContactIndexByCategory(categories: List<Category>, categoryName: String): Int? {
    var runningIndex = 0

    for (category in categories) {
        if (category.name == categoryName) {
            return runningIndex
        }
        runningIndex += category.contacts.size + 1
    }

    return null
}
