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
import space.zuoqiu.siyuanhelper.data.Tag

/**
 * An static data store of [Subject]s. This includes both [Subject]s owned by the current user and
 * all [Subject]s of the current user's contacts.
 */
object LocalTagDataProvider {

    val allTags = listOf(
        Tag(
            id = 0L,
            name = "Default Tag",
            description = "default tag",
        ),
    )

    /**
     * Whether or not the given [Subject.id] id is an account owned by the current user.
     */
    fun has(id: Long): Boolean = allTags.any { it.id == id }

    /**
     * Get the contact of the current user with the given [id].
     */
    fun getTabById(id: Long): Tag? {
        return allTags.firstOrNull { it.id == id }
    }
    /**
     * Get the default subject
     */
    fun getDefaultTag(): Tag {
        return allTags.first()
    }
}
