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

import space.zuoqiu.siyuanhelper.data.local.LocalMemosDataProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class MemosRepositoryImpl : MemosRepository {

    override fun getAllMemos(): Flow<List<Memo>> = flow {
        val memos = LocalMemosDataProvider.allMemos
        emit(memos)
    }

    override fun getCategoryMemos(category: MemoType): Flow<List<Memo>> = flow {
        val memos = LocalMemosDataProvider.allMemos.filter { it.memoType == category }
        emit(memos)
    }

    override fun getMemoFromId(id: Long): Flow<Memo?> = flow {
        val memo = LocalMemosDataProvider.getMemoById(id)
        emit(memo)
    }
}
