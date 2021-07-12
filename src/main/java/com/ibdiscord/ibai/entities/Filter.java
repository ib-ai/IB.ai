package com.ibdiscord.ibai.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "filters")
public final class Filter {

    @Id
    @Column(name = "guild_id")
    private long guild_id;

    @Column(name = "filtering")
    private boolean filtering;

    @Column(name = "removal")
    private boolean removal;

    @ElementCollection
    @CollectionTable(name = "filterEntries", joinColumns = @JoinColumn(name="guild_id"))
    private List<FilterEntry> filterEntries;

    public Filter(long guild_id, boolean filtering, boolean removal) {
        this.guild_id = guild_id;
        this.filtering = filtering;
        this.removal = removal;
        this.filterEntries = new ArrayList<>();
    }

    public Filter(long guild_id, List<FilterEntry> filterEntries) {
        this.guild_id = guild_id;
        this.filterEntries = filterEntries;
    }

    public Filter(long guild_id) {
        this.guild_id = guild_id;
        this.filterEntries = new ArrayList<>();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Filter filter = (Filter) o;
        return guild_id == filter.guild_id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(guild_id);
    }
}
