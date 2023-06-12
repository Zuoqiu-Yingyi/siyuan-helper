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

package space.zuoqiu.siyuanhelper.data

import space.zuoqiu.siyuanhelper.data.local.LocalSubjectDataProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SubjectsRepositoryImpl : SubjectsRepository {

    override fun getDefaultSubject(): Flow<Subject> = flow {
        val subject = LocalSubjectDataProvider.getDefaultSubject()
        emit(subject)
    }

    override fun getAllSubjects(): Flow<List<Subject>> = flow {
        val subjects = LocalSubjectDataProvider.allSubjects
        emit(subjects)
    }

    override fun getSubjectById(id: Long): Flow<Subject?> = flow {
        val subject = LocalSubjectDataProvider.getSubjectById(id)
        emit(subject)
    }
}
