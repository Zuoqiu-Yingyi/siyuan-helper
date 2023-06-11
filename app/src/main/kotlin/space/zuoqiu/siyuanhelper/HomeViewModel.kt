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

package space.zuoqiu.siyuanhelper.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import space.zuoqiu.siyuanhelper.data.Memo
import space.zuoqiu.siyuanhelper.data.MemosRepository
import space.zuoqiu.siyuanhelper.data.MemosRepositoryImpl
import space.zuoqiu.siyuanhelper.ui.utils.HelperContentType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import space.zuoqiu.siyuanhelper.data.Subject
import space.zuoqiu.siyuanhelper.data.SubjectsRepository
import space.zuoqiu.siyuanhelper.data.SubjectsRepositoryImpl

class HomeViewModel(
    private val subjectsRepository: SubjectsRepository = SubjectsRepositoryImpl(),
    private val memosRepository: MemosRepository = MemosRepositoryImpl(),
) :
    ViewModel() {

    // UI state exposed to the UI
    private val _uiState = MutableStateFlow(HelperHomeUIState(loading = true))
    val uiState: StateFlow<HelperHomeUIState> = _uiState

    init {
        observe()
    }

    private fun observe() {
        viewModelScope.launch {
            subjectsRepository.getAllSubjects()
                .catch { ex ->
                    _uiState.value = HelperHomeUIState(error = ex.message)
                }
                .collect { subjects ->
                    memosRepository.getAllMemos()
                        .catch { ex ->
                            _uiState.value = HelperHomeUIState(error = ex.message)
                        }
                        .collect { memos ->
                            val defaultSubject = subjects.first()
                            val defaultMemos = memos.filter { it.subject.id == defaultSubject.id }
                            _uiState.value = HelperHomeUIState(
                                defaultSubject = defaultSubject,
                                defaultMemos = defaultMemos,

                                memos = memos,
                                subjects = subjects,
                            )
                        }
                }
        }
    }

    fun setOpenedSubject(subjectId: Long, contentType: HelperContentType) {
        /**
         * We only set isDetailOnlyOpen to true when it's only single pane layout
         */
        val subject = uiState.value.subjects.find { it.id == subjectId }
        val memos = uiState.value.memos.filter { it.subject.id == subjectId }
        _uiState.value = _uiState.value.copy(
            openedSubject = subject,
            openedMemos = memos,
            isSubjectOnlyOpen = contentType == HelperContentType.SINGLE_PANE,
        )
    }

    fun toggleSelectedSubject(subjectId: Long) {
        val currentSelection = uiState.value.selectedSubjects
        _uiState.value = _uiState.value.copy(
            selectedSubjects = if (currentSelection.contains(subjectId))
                currentSelection.minus(subjectId) else currentSelection.plus(subjectId)
        )
    }

    fun closeDetailScreen() {
        _uiState.value = _uiState
            .value.copy(
                isSubjectOnlyOpen = false,
                openedSubject = _uiState.value.subjects.first()
            )
    }
}

data class HelperHomeUIState(
    val defaultSubject: Subject? = null,
    val defaultMemos: List<Memo>? = null,

    val subjects: List<Subject> = emptyList(),
    val memos: List<Memo> = emptyList(),

    val openedSubject: Subject? = null,
    val openedMemos: List<Memo>? = null,

    val selectedSubjects: Set<Long> = emptySet(),
    val isSubjectOnlyOpen: Boolean = false,


    val loading: Boolean = false,
    val error: String? = null
)
