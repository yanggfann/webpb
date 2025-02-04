/*
 * Copyright (c) 2020 linqu.tech, All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package tech.linqu.webpb.commons;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class ParamGroupTest {

    @Test
    void shouldCreateSuccessWhenPathIsNull() {
        // given
        String path = null;

        // when
        ParamGroup group = ParamGroup.of(path);

        // then
        assertTrue(group.getParams().isEmpty());
        assertEquals("", group.getSuffix());
    }

    @Test
    void shouldCreateSuccessWhenPathIsEmpty() {
        // given
        String path = "";

        // when
        ParamGroup group = ParamGroup.of(path);

        // then
        assertTrue(group.getParams().isEmpty());
        assertEquals("", group.getSuffix());
    }

    @Test
    void shouldCreateSuccessWhenPathIsRoot() {
        // given
        String path = "/";

        // when
        ParamGroup group = ParamGroup.of(path);

        // then
        assertTrue(group.getParams().isEmpty());
        assertEquals("/", group.getSuffix());
    }

    @Test
    void shouldCreateSuccessWhenPathWithParams() {
        // given
        String path = "/{a}/b{c}/{d}e/f{g.h}i/j";

        // when
        ParamGroup group = ParamGroup.of(path);

        // then
        assertEquals(4, group.getParams().size());
        assertEquals("i/j", group.getSuffix());
        // 1
        assertEquals("/", group.getParams().get(0).getPrefix());
        assertNull(group.getParams().get(0).getKey());
        assertEquals("a", group.getParams().get(0).getAccessor());
        // 2
        assertEquals("/b", group.getParams().get(1).getPrefix());
        assertNull(group.getParams().get(1).getKey());
        assertEquals("c", group.getParams().get(1).getAccessor());
        // 3
        assertEquals("/", group.getParams().get(2).getPrefix());
        assertNull(group.getParams().get(2).getKey());
        assertEquals("d", group.getParams().get(2).getAccessor());
        // 4
        assertEquals("e/f", group.getParams().get(3).getPrefix());
        assertNull(group.getParams().get(3).getKey());
        assertEquals("g.h", group.getParams().get(3).getAccessor());
    }

    @Test
    void shouldCreateSuccessWhenPathWithQueries() {
        // given
        String path = "a={b}&c={d.e}";

        // when
        ParamGroup group = ParamGroup.of(path);

        // then
        assertEquals(2, group.getParams().size());
        assertEquals("", group.getSuffix());
        // 1
        assertEquals("", group.getParams().get(0).getPrefix());
        assertEquals("a", group.getParams().get(0).getKey());
        assertEquals("b", group.getParams().get(0).getAccessor());
        // 2
        assertEquals("", group.getParams().get(1).getPrefix());
        assertEquals("c", group.getParams().get(1).getKey());
        assertEquals("d.e", group.getParams().get(1).getAccessor());
    }

    @Test
    void shouldCreateSuccessWhenPathWithParamsAndQueries() {
        // given
        String path = "a/{b}/c?e={f}";

        // when
        ParamGroup group = ParamGroup.of(path);

        // then
        assertEquals(2, group.getParams().size());
        assertEquals("", group.getSuffix());
        // 1
        assertEquals("a/", group.getParams().get(0).getPrefix());
        assertNull(group.getParams().get(0).getKey());
        assertEquals("b", group.getParams().get(0).getAccessor());
        // 2
        assertEquals("/c?", group.getParams().get(1).getPrefix());
        assertEquals("e", group.getParams().get(1).getKey());
        assertEquals("f", group.getParams().get(1).getAccessor());
    }

    @Test
    void shouldGroupIsEmpty() {
        ParamGroup group = ParamGroup.of("/");
        assertTrue(group.isEmpty());
    }
}
