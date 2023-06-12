// Copyright (C) 2023 Zuoqiu Yingyi
// 
// This program is free software: you can redistribute it and/or modify
// it under the terms of the GNU Affero General Public License as
// published by the Free Software Foundation, either version 3 of the
// License, or (at your option) any later version.
// 
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU Affero General Public License for more details.
// 
// You should have received a copy of the GNU Affero General Public License
// along with this program.  If not, see <http://www.gnu.org/licenses/>.

package space.zuoqiu.siyuanhelper.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DockedSearchBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import space.zuoqiu.siyuanhelper.R
import space.zuoqiu.siyuanhelper.data.Memo
import space.zuoqiu.siyuanhelper.data.Subject

/**
 * 搜索栏
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DockedSearchBar(
    memos: List<Memo>,
    onSearchItemSelected: (Memo) -> Unit,
    modifier: Modifier = Modifier
) {
    var query by remember { mutableStateOf("") }
    var active by remember { mutableStateOf(false) }
    val searchResults = remember { mutableStateListOf<Memo>() }

    LaunchedEffect(query) {
        searchResults.clear()
        if (query.isNotEmpty()) {
            searchResults.addAll(
                memos.filter {
                    it.title.startsWith(
                        prefix = query,
                        ignoreCase = true
                    ) || it.description.startsWith(
                        prefix = query,
                        ignoreCase = true,
                    ) || it.content.startsWith(
                        prefix = query,
                        ignoreCase = true,
                    ) || it.subject.title.startsWith(
                        prefix = query,
                        ignoreCase = true,
                    ) || it.subject.description.startsWith(
                        prefix = query,
                        ignoreCase = true,
                    )
                }
            )
        }
    }

    /* 搜索输入框 */
    DockedSearchBar(
        modifier = modifier,
        query = query,
        onQueryChange = {
            query = it
        },
        onSearch = { active = false },
        active = active,
        onActiveChange = {
            active = it
        },
        placeholder = { Text(text = stringResource(id = R.string.search_memos)) },
        leadingIcon = {
            if (active) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = stringResource(id = R.string.back_button),
                    modifier = Modifier
                        .padding(start = 16.dp)
                        .clickable {
                            active = false
                            query = ""
                        },
                )
            } else {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = stringResource(id = R.string.search),
                    modifier = Modifier.padding(start = 16.dp),
                )
            }
        },
        trailingIcon = {
            ProfileImage(
                drawableResource = R.drawable.ic_launcher,
                description = stringResource(id = R.string.profile),
                modifier = Modifier
                    .padding(12.dp)
                    .size(32.dp)
            )
        },
    ) {
        if (searchResults.isNotEmpty()) {
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(items = searchResults, key = { it.id }) { email ->
                    ListItem(
                        headlineContent = { Text(email.description) },
                        supportingContent = { Text(email.subject.title) },
                        // leadingContent = {
                        //     ProfileImage(
                        //         drawableResource = email.subject.avatar,
                        //         description = stringResource(id = R.string.profile),
                        //         modifier = Modifier
                        //             .size(32.dp)
                        //     )
                        // },
                        modifier = Modifier.clickable {
                            onSearchItemSelected.invoke(email)
                            query = ""
                            active = false
                        }
                    )
                }
            }
        } else if (query.isNotEmpty()) {
            Text(
                text = stringResource(id = R.string.no_item_found),
                modifier = Modifier.padding(16.dp)
            )
        } else
            Text(
                text = stringResource(id = R.string.no_search_history),
                modifier = Modifier.padding(16.dp)
            )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubjectDetailAppBar(
    memos: List<Memo>,
    subject: Subject,
    isFullScreen: Boolean,
    modifier: Modifier = Modifier,
    onBackPressed: () -> Unit
) {
    TopAppBar(
        modifier = modifier,
        colors = topAppBarColors(
            containerColor = MaterialTheme.colorScheme.inverseOnSurface,
        ),
        title = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = if (isFullScreen) Alignment.CenterHorizontally
                else Alignment.Start
            ) {
                Text(
                    text = subject.title,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    modifier = Modifier.padding(top = 4.dp),
                    text = "${memos.size} ${stringResource(id = R.string.memos)}",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.outline
                )
            }
        },
        navigationIcon = {
            if (isFullScreen) {
                FilledIconButton(
                    onClick = onBackPressed,
                    modifier = Modifier.padding(8.dp),
                    colors = IconButtonDefaults.filledIconButtonColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                        contentColor = MaterialTheme.colorScheme.onSurface
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = stringResource(id = R.string.back_button),
                        modifier = Modifier.size(14.dp)
                    )
                }
            }
        },
        actions = {
            IconButton(
                onClick = { /*TODO*/ },
            ) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = stringResource(id = R.string.more_options_button),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    )
}
