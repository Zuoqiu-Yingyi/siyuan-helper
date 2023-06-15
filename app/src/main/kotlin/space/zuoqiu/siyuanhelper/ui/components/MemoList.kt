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

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import space.zuoqiu.siyuanhelper.data.Memo
import space.zuoqiu.siyuanhelper.data.Subject
import space.zuoqiu.siyuanhelper.data.local.LocalMemosDataProvider
import space.zuoqiu.siyuanhelper.data.local.LocalSubjectDataProvider
import space.zuoqiu.siyuanhelper.ui.theme.HelperTheme
import space.zuoqiu.siyuanhelper.ui.utils.HelperContentType

@Composable
fun MemoList(
    memos: List<Memo>,
    subject: Subject,
    modifier: Modifier = Modifier.fillMaxSize(),
    isFullScreen: Boolean = true,
    onBackPressed: () -> Unit = {}
) {
    LazyColumn(
        modifier = modifier
            .background(MaterialTheme.colorScheme.inverseOnSurface)
    ) {
        item {
            SubjectDetailAppBar(
                memos = memos,
                subject = subject,
                isFullScreen = isFullScreen,
            ) {
                onBackPressed()
            }
        }
        items(items = memos, key = { it.id }) { memo ->
            MemoItem(
                memo = memo,
                toggleStar = {
                    memo.isStarred = !memo.isStarred
                },
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MemoListPreview() {
    HelperTheme {
        MemoList(
            memos = LocalMemosDataProvider.allMemos,
            subject = LocalSubjectDataProvider.getDefaultSubject(),
        )
    }
}
