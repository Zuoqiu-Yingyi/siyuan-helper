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

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import space.zuoqiu.siyuanhelper.data.Memo
import space.zuoqiu.siyuanhelper.data.Subject
import space.zuoqiu.siyuanhelper.data.local.LocalSubjectDataProvider
import space.zuoqiu.siyuanhelper.ui.theme.HelperTheme
import space.zuoqiu.siyuanhelper.ui.utils.HelperContentType

@Composable
fun SubjectList(
    memos: List<Memo>,
    subjects: List<Subject>,
    openedSubject: Subject?,
    selectedSubjectIds: Set<Long>,
    toggleSubjectSelection: (Long) -> Unit,
    lazyListState: LazyListState,
    modifier: Modifier = Modifier,
    navigateToDetail: (Long, HelperContentType) -> Unit
) {
    Box(modifier = modifier) {
        DockedSearchBar(
            memos = memos,
            onSearchItemSelected = { searchedMemo ->
                navigateToDetail(searchedMemo.id, HelperContentType.SINGLE_PANE)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )

        LazyColumn(
            modifier = modifier
                .fillMaxWidth()
                .padding(top = 84.dp),
            state = lazyListState
        ) {
            items(items = subjects, key = { it.id }) { subject ->
                SubjectItem(
                    subject = subject,
                    navigateToDetail = { subjectId ->
                        navigateToDetail(subjectId, HelperContentType.SINGLE_PANE)
                    },
                    toggleSelection = toggleSubjectSelection,
                    isOpened = openedSubject?.id == subject.id,
                    isSelected = selectedSubjectIds.contains(subject.id)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SubjectListPreview() {
    HelperTheme {
        SubjectList(
            memos = emptyList(),
            subjects = LocalSubjectDataProvider.allSubjects,
            openedSubject = null,
            selectedSubjectIds = emptySet(),
            toggleSubjectSelection = {},
            lazyListState = LazyListState(),
            navigateToDetail = { _, _ ->},
        )
    }
}
