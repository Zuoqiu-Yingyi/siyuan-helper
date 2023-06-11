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

package space.zuoqiu.siyuanhelper.data.local

import space.zuoqiu.siyuanhelper.data.Subject
import java.time.LocalDateTime
import java.time.ZoneId

/**
 * An static data store of [Subject]s. This includes both [Subject]s owned by the current user and
 * all [Subject]s of the current user's contacts.
 */
object LocalSubjectDataProvider {

    val allSubjects = listOf(
        Subject(
            id = 0L,
            title = "default subject title",
            description = "default subject description",
            updated = LocalDateTime.of(2023, 3, 2, 3, 4, 5).atZone(ZoneId.systemDefault()),
        ),
    )

    /**
     * Whether or not the given [Subject.id] id is an account owned by the current user.
     */
    fun isSubject(uid: Long): Boolean = allSubjects.any { it.id == uid }

    /**
     * Get the contact of the current user with the given [id].
     */
    fun getSubjectById(id: Long): Subject? {
        return allSubjects.firstOrNull { it.id == id }
    }
    /**
     * Get the default subject
     */
    fun getDefaultSubject(): Subject {
        return allSubjects.first()
    }
}
