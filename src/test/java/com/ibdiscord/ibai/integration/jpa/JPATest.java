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

package com.ibdiscord.ibai.integration.jpa;

import com.ibdiscord.ibai.entities.*;
import com.ibdiscord.ibai.repositories.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static com.ibdiscord.ibai.integration.jpa.JPAConstants.*;

/**
 * Represents a base test class that can be extended to test JPA functionality.
 * This will use an in-memory H2 instance to test any database connectivity
 * This is slightly overkill, but will ensure that JPA performs accordingly.
 */
@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class JPATest {

    @Autowired
    private FilterRepository filterRepository;

    @Autowired
    private GuildSettingsRepository guildSettingsRepository;

    @Autowired
    private TagsRepository tagsRepository;

    @Autowired
    private UserJoinOverrideRepository userJoinOverrideRepository;

    @Autowired
    private UserOptRepository userOptRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Test
    void filterNonExistent() {
        assertEquals(0, filterRepository.count());
    }

    @Test
    void filterCreate() {
        filterRepository.save(new Filter(GUILD_1));
        filterRepository.save(new Filter(GUILD_2, true, false, Arrays.asList(new FilterEntry("value"))));
        // to verify that default of boolean attributes is false
        assertFalse(filterRepository.findById(GUILD_1).get().isFiltering());
        assertFalse(filterRepository.findById(GUILD_1).get().isRemoval());
        assertEquals(new FilterEntry("value", false), filterRepository.findById(GUILD_2).get().getFilterEntries().get(0));
        assertEquals(2, filterRepository.count());
    }

    @Test
    void filterCollision() {
        List<FilterEntry> entries = Arrays.asList(new FilterEntry("entry1"), new FilterEntry("entry2"));
        filterRepository.save(new Filter(GUILD_1));
        // verifies empty list of filter entries before collision
        assertEquals(0, filterRepository.findById(GUILD_1).get().getFilterEntries().size());
        filterRepository.save(new Filter(GUILD_2));
        filterRepository.save(new Filter(GUILD_1, true, true, entries));
        assertEquals(2, filterRepository.count());
        // collision so latest entry should be kept which has list of filter entries with size 2
        assertEquals(2, filterRepository.findById(GUILD_1).get().getFilterEntries().size());
    }

    @Test
    void filterDelete() {
        filterRepository.save(new Filter(GUILD_1));
        filterRepository.save(new Filter(GUILD_2));
        assertEquals(2, filterRepository.count());
        filterRepository.delete(filterRepository.findById(GUILD_1).get());
        assertEquals(1, filterRepository.count());
        assertFalse(filterRepository.findById(GUILD_1).isPresent());
    }

    @Test
    void filterEditEntry() {
        List<FilterEntry> entries = Arrays.asList(new FilterEntry("e1"), new FilterEntry("e2"), new FilterEntry("e3"));
        filterRepository.save(new Filter(GUILD_1, entries));
        Filter filter = filterRepository.findById(GUILD_1).get();
        // first entry edited
        filter.getFilterEntries().get(0).setNotify(true);
        // last entry removed
        filter.getFilterEntries().remove(2);
        // new entry created and added
        filter.getFilterEntries().add(new FilterEntry("added entry", true));
        filterRepository.save(filter);
        assertEquals(filterRepository.findById(GUILD_1).get().getFilterEntries().size(), 3);
        // checks that last entry is the added entry (not the original e3 entry that was removed)
        assertEquals(filterRepository.findById(GUILD_1).get().getFilterEntries().get(2), new FilterEntry("added entry", true));
        // checks that edited entry is up to date
        assertTrue(filterRepository.findById(GUILD_1).get().getFilterEntries().get(0).isNotify());
    }

    @Test
    void guildNonExistent() {
        assertFalse(guildSettingsRepository.findById(GUILD_1).isPresent());
    }

    @Test
    void guildExistent() {
        GuildSettings guildSettings = new GuildSettings(
            GUILD_1,
            "&",
            -1,
            -1,
            -1,
            -1,
            -1,
            -1
        );
        guildSettingsRepository.save(guildSettings);
        Optional<GuildSettings> retrieved = guildSettingsRepository.findById(GUILD_1);
        assertTrue(retrieved.isPresent());
        assertEquals("&", retrieved.get().getPrefix());
    }

    @Test
    void guildUpdate() {
        GuildSettings guildSettings = new GuildSettings(
            GUILD_1,
            "&",
            -1,
            -1,
            -1,
            -1,
            -1,
            -1
        );
        guildSettingsRepository.save(guildSettings);
        guildSettings.setStaff(1);
        guildSettingsRepository.save(guildSettings);
        Optional<GuildSettings> retrieved = guildSettingsRepository.findById(GUILD_1);
        assertTrue(retrieved.isPresent());
        assertEquals(1, retrieved.get().getStaff());
    }

    @Test
    void overrideNonExistent() {
        assertFalse(userJoinOverrideRepository.findById(USER_1).isPresent());
    }

    @Test
    void overrideExistent() {
        userJoinOverrideRepository.save(new UserJoinOverride(USER_1, 1337));
        userJoinOverrideRepository.save(new UserJoinOverride(USER_2, 7331));
        Optional<UserJoinOverride> override = userJoinOverrideRepository.findById(USER_1);
        assertTrue(override.isPresent());
        assertEquals(1337, override.get().getOverride());
    }

    @Test
    void optNonExistent() {
        assertEquals(0, userOptRepository.findByUser(USER_1).size());
    }

    @Test
    void optExistent() {
        userOptRepository.save(new UserOpt(USER_1, CHANNEL_1));
        userOptRepository.save(new UserOpt(USER_1, CHANNEL_2));
        userOptRepository.save(new UserOpt(USER_2, CHANNEL_1));
        List<UserOpt> opts = Arrays.asList(
            new UserOpt(USER_1, CHANNEL_1),
            new UserOpt(USER_1, CHANNEL_2)
        );
        assertEquals(opts, userOptRepository.findByUser(USER_1));
    }

    @Test
    void optCollision() {
        // Explicitly make sure that duplicates are treated properly.
        userOptRepository.save(new UserOpt(USER_1, CHANNEL_1));
        userOptRepository.save(new UserOpt(USER_1, CHANNEL_1));
        Iterable<UserOpt> opts = userOptRepository.findAll();
        Iterator<UserOpt> iterator = opts.iterator();
        assertTrue(iterator.hasNext());
        assertEquals(CHANNEL_1, iterator.next().getChannel());
        assertFalse(iterator.hasNext());
    }

    @Test
    void optDelete() {
        userOptRepository.save(new UserOpt(USER_1, CHANNEL_1));
        userOptRepository.save(new UserOpt(USER_1, CHANNEL_2));
        userOptRepository.save(new UserOpt(USER_2, CHANNEL_1));
        userOptRepository.deleteByUser(USER_1);
        assertEquals(0, userOptRepository.findByUser(USER_1).size());
    }

    @Test
    void roleNonExistent() {
        assertEquals(0, userRoleRepository.findByUser(USER_1).size());
    }

    @Test
    void roleExistent() {
        userRoleRepository.save(new UserRole(USER_1, ROLE_1));
        userRoleRepository.save(new UserRole(USER_1, ROLE_2));
        userRoleRepository.save(new UserRole(USER_2, ROLE_1));
        List<UserRole> roles = Arrays.asList(
            new UserRole(USER_1, ROLE_1),
            new UserRole(USER_1, ROLE_2)
        );
        assertEquals(roles, userRoleRepository.findByUser(USER_1));
    }

    @Test
    void roleCollision() {
        // Explicitly make sure that duplicates are treated properly.
        userRoleRepository.save(new UserRole(USER_1, ROLE_1));
        userRoleRepository.save(new UserRole(USER_1, ROLE_1));
        Iterable<UserRole> roles = userRoleRepository.findAll();
        Iterator<UserRole> iterator = roles.iterator();
        assertTrue(iterator.hasNext());
        assertEquals(ROLE_1, iterator.next().getRole());
        assertFalse(iterator.hasNext());
    }

    @Test
    void roleDelete() {
        userRoleRepository.save(new UserRole(USER_1, ROLE_1));
        userRoleRepository.save(new UserRole(USER_1, ROLE_2));
        userRoleRepository.save(new UserRole(USER_2, ROLE_1));
        userRoleRepository.deleteByUser(USER_1);
        assertEquals(0, userRoleRepository.findByUser(USER_1).size());
    }

    @Test
    void tagsNonExistent() {
        assertEquals(0, tagsRepository.count());
    }

    @Test
    void tagsCreate() {
        Tags.CompositePK pk1 = new Tags.CompositePK(GUILD_1, "name");
        Tags.CompositePK pk2 = new Tags.CompositePK(GUILD_2, "name");
        tagsRepository.save(new Tags(GUILD_1, "name", "output"));
        tagsRepository.save(new Tags(GUILD_1, "different name", "different output"));
        tagsRepository.save(new Tags(GUILD_2, "name", "abc123"));
        // to verify if default of boolean disabled is false
        assertFalse(tagsRepository.findById(pk1).get().isDisabled());
        assertEquals(3, tagsRepository.count());
        assertEquals(Optional.of(new Tags(GUILD_2, "name", "abc123")), tagsRepository.findById(pk2));
    }

    @Test
    void tagsCollision() {
        tagsRepository.save(new Tags(GUILD_1, "name", "output"));
        tagsRepository.save(new Tags(GUILD_1, "name", "different output"));
        assertEquals(1, tagsRepository.count());
        Iterable<Tags> tags = tagsRepository.findAll();
        Iterator<Tags> iterator = tags.iterator();
        assertTrue(iterator.hasNext());
        assertEquals(iterator.next().getOutput(), "different output");
    }

    @Test
    void tagsDelete() {
        Tags.CompositePK pk = new Tags.CompositePK(GUILD_1, "name 1");
        tagsRepository.save(new Tags(GUILD_1, "name 1", "output 1"));
        tagsRepository.save(new Tags(GUILD_1, "name 2", "output 2"));
        tagsRepository.save(new Tags(GUILD_2, "name 3", "output 3"));
        tagsRepository.deleteById(pk);
        assertEquals(2, tagsRepository.count());
        assertFalse(tagsRepository.findById(pk).isPresent());
    }

    @Test
    void tagsSetDisabled() {
        tagsRepository.save(new Tags(GUILD_1, "name 1", "output 1", false));
        tagsRepository.save(new Tags(GUILD_2, "name 3", "output 3"));
        Tags t = tagsRepository.findById(new Tags.CompositePK(GUILD_1, "name 1")).get();
        assertFalse(t.isDisabled());
        t.setDisabled(true);
        tagsRepository.save(t);
        assertTrue(tagsRepository.findById(new Tags.CompositePK(GUILD_1, "name 1")).get().isDisabled());
    }
}
