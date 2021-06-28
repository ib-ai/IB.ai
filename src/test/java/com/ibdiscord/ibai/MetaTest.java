/* Copyright 2017-2021 Arraying
 *
 * This file is part of IB.ai.
 *
 * IB.ai is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * IB.ai is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with IB.ai. If not, see http://www.gnu.org/licenses/.
 */

package com.ibdiscord.ibai;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Contains metadata that can be accessed at run-time.
 */
@SpringBootTest
@ActiveProfiles({ "dev", "test" })
@ExtendWith(SpringExtension.class)
public class MetaTest {

    @Autowired private Meta meta;

    @Test
    void testMeta() throws IOException {
        assertEquals(extractVersionFromPom(), meta.getVersion());
    }

    /**
     * This will extract the version from the pom.xml file.
     * It is not an ideal method, however, will suffice for the scope of this.
     * @return The version, or null if it could not be found.
     * @throws IOException If there was an error reading the file.
     */
    private String extractVersionFromPom() throws IOException  {
        File file = new File("pom.xml");
        String contents = new String(Files.readAllBytes(file.toPath()));
        // We are working with the first occurrence of the version tag, so this is fine.
        int start = contents.indexOf("<version>");
        int end = contents.indexOf("</version>");
        if (start < 0 || end < 0) {
            return null;
        }
        return contents.substring(start + "<version>".length(), end);
    }

}
